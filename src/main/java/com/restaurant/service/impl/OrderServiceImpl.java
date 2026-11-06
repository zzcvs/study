package com.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Menu;
import com.restaurant.entity.OrderDetail;
import com.restaurant.entity.OrderInfo;
import com.restaurant.entity.Recipe;
import com.restaurant.mapper.OrderDetailMapper;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.service.MenuService;
import com.restaurant.service.OrderService;
import com.restaurant.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MenuService menuService;
    
    @Override
    @Transactional
    public OrderInfo createOrder(OrderInfo orderInfo, List<OrderDetail> orderDetails) {
        String orderNo = generateOrderNo();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setOrderStatus("PENDING");
        orderInfo.setPaymentStatus("UNPAID");
        orderInfo.setDistributeStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, Integer> requiredQuantities = new LinkedHashMap<>();
        Map<Long, String> recipeNames = new HashMap<>();
        for (OrderDetail detail : orderDetails) {
            Recipe recipe = recipeService.getById(detail.getRecipeId());
            if (recipe == null) {
                throw new RuntimeException("菜品不存在");
            }
            detail.setRecipeName(recipe.getName());
            detail.setPrice(recipe.getPrice());
            detail.setSubtotal(recipe.getPrice().multiply(new BigDecimal(detail.getQuantity())));
            totalAmount = totalAmount.add(detail.getSubtotal());
            requiredQuantities.merge(detail.getRecipeId(), detail.getQuantity(), Integer::sum);
            recipeNames.put(detail.getRecipeId(), recipe.getName());
        }
        orderInfo.setTotalAmount(totalAmount);

        List<Menu> menus = menuService.getMenuByDateAndMealType(orderInfo.getOrderDate(), orderInfo.getMealType());
        for (Map.Entry<Long, Integer> entry : requiredQuantities.entrySet()) {
            int available = 0;
            for (Menu menu : menus) {
                if (entry.getKey().equals(menu.getRecipeId())) {
                    available += menu.getStock() == null ? 0 : menu.getStock();
                }
            }
            if (available < entry.getValue()) {
                throw new RuntimeException("菜品「" + recipeNames.get(entry.getKey()) + "」库存不足，当前库存 " + available);
            }
        }

        save(orderInfo);

        for (OrderDetail detail : orderDetails) {
            detail.setOrderId(orderInfo.getId());
            orderDetailMapper.insert(detail);
        }

        for (Map.Entry<Long, Integer> entry : requiredQuantities.entrySet()) {
            int remaining = entry.getValue();
            for (Menu menu : menus) {
                if (!entry.getKey().equals(menu.getRecipeId())) {
                    continue;
                }
                int stock = menu.getStock() == null ? 0 : menu.getStock();
                int deduct = Math.min(stock, remaining);
                if (deduct > 0) {
                    menuService.updateStock(menu.getId(), deduct);
                    remaining -= deduct;
                }
                if (remaining <= 0) {
                    break;
                }
            }
        }

        return orderInfo;
    }
    
    @Override
    public OrderInfo getOrderByOrderNo(String orderNo) {
        return baseMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<OrderInfo>()
                .eq("order_no", orderNo)
        );
    }
    
    @Override
    public List<OrderInfo> getOrdersByDate(LocalDate date) {
        return baseMapper.getOrdersByDate(date);
    }
    
    @Override
    public List<OrderInfo> getOrdersByUserId(Long userId) {
        return baseMapper.getOrdersByUserId(userId);
    }
    
    @Override
    public List<OrderInfo> getOrdersByStatus(String status) {
        return baseMapper.getOrdersByStatus(status);
    }

    @Override
    public List<OrderInfo> getOrdersByDistributeStatus(String status) {
        return baseMapper.getOrdersByDistributeStatus(status);
    }

    @Override
    public List<OrderInfo> getReadyOrdersForDistribute() {
        return baseMapper.getReadyOrdersForDistribute();
    }
    
    @Override
    public boolean updateOrderStatus(Long orderId, String status) {
        OrderInfo orderInfo = getById(orderId);
        if (orderInfo != null) {
            orderInfo.setOrderStatus(status);
            return updateById(orderInfo);
        }
        return false;
    }
    
    @Override
    public boolean updateDistributeStatus(Long orderId, String status) {
        OrderInfo orderInfo = getById(orderId);
        if (orderInfo != null) {
            orderInfo.setDistributeStatus(status);
            return updateById(orderInfo);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean cancelOrder(Long orderId) {
        OrderInfo orderInfo = getById(orderId);
        if (orderInfo == null || !"PENDING".equals(orderInfo.getOrderStatus())) {
            return false;
        }

        List<OrderDetail> details = getOrderDetails(orderId);
        List<Menu> menus = menuService.getMenuByDateAndMealType(orderInfo.getOrderDate(), orderInfo.getMealType());
        for (OrderDetail detail : details) {
            for (Menu menu : menus) {
                if (!detail.getRecipeId().equals(menu.getRecipeId())) {
                    continue;
                }
                Menu current = menuService.getById(menu.getId());
                if (current != null) {
                    int stock = current.getStock() == null ? 0 : current.getStock();
                    int sold = current.getSoldCount() == null ? 0 : current.getSoldCount();
                    current.setStock(stock + detail.getQuantity());
                    current.setSoldCount(Math.max(0, sold - detail.getQuantity()));
                    menuService.updateById(current);
                }
                break;
            }
        }

        return updateOrderStatus(orderId, "CANCELLED");
    }
    
    @Override
    public List<OrderDetail> getOrderDetails(Long orderId) {
        return orderDetailMapper.getOrderDetailsByOrderId(orderId);
    }
    
    private String generateOrderNo() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Random random = new Random();
        int randomNum = 1000 + random.nextInt(9000);
        return "ORD" + dateStr + randomNum;
    }
}
