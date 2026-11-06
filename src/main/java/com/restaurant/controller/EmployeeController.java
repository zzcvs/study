package com.restaurant.controller;

import com.restaurant.entity.Menu;
import com.restaurant.entity.OrderDetail;
import com.restaurant.entity.OrderInfo;
import com.restaurant.entity.User;
import com.restaurant.service.MenuService;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        List<Menu> lunchMenu = menuService.getMenuByDateAndMealType(LocalDate.now(), "午餐");
        model.addAttribute("lunchMenu", lunchMenu);
        
        List<OrderInfo> myOrders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute("myOrders", myOrders);
        
        return "employee/home";
    }
    
    @GetMapping("/menu")
    public String menu(@RequestParam(required = false) String mealType,
                       @RequestParam(required = false) String date,
                       HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        LocalDate queryDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        String queryMealType = mealType != null ? mealType : "午餐";
        
        List<Menu> menuList = menuService.getMenuByDateAndMealType(queryDate, queryMealType);
        model.addAttribute("menuList", menuList);
        model.addAttribute("queryDate", queryDate);
        model.addAttribute("queryMealType", queryMealType);
        model.addAttribute("user", user);
        
        return "employee/menu";
    }
    
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<OrderInfo> myOrders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute("myOrders", myOrders);
        model.addAttribute("user", user);
        
        return "employee/orders";
    }
    
    @GetMapping("/order/detail")
    public String orderDetail(@RequestParam Long orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        OrderInfo order = orderService.getById(orderId);
        List<OrderDetail> details = orderService.getOrderDetails(orderId);
        
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        model.addAttribute("user", user);
        
        return "employee/order-detail";
    }
    
    @PostMapping("/order/create")
    @ResponseBody
    public Map<String, Object> createOrder(@RequestParam Map<String, String> params,
                                            HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "请先登录");
                return result;
            }
            
            String recipeIdsParam = params.get("recipeIds");
            String quantitiesParam = params.get("quantities");
            if (recipeIdsParam == null || quantitiesParam == null || recipeIdsParam.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择菜品");
                return result;
            }

            LocalDate orderDate = LocalDate.now();
            String dateParam = params.get("date");
            if (dateParam != null && !dateParam.isEmpty()) {
                try {
                    orderDate = LocalDate.parse(dateParam);
                } catch (DateTimeParseException e) {
                    result.put("success", false);
                    result.put("message", "日期格式不正确");
                    return result;
                }
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserId(user.getId());
            orderInfo.setUserName(user.getRealName());
            orderInfo.setDepartment(user.getDepartment());
            orderInfo.setOrderDate(orderDate);
            orderInfo.setMealType(params.get("mealType"));
            orderInfo.setRemark(params.get("remark"));
            
            List<OrderDetail> orderDetails = new ArrayList<>();
            String[] recipeIds = recipeIdsParam.split(",");
            String[] quantities = quantitiesParam.split(",");
            
            for (int i = 0; i < recipeIds.length; i++) {
                if (!quantities[i].equals("0")) {
                    OrderDetail detail = new OrderDetail();
                    detail.setRecipeId(Long.parseLong(recipeIds[i]));
                    detail.setQuantity(Integer.parseInt(quantities[i]));
                    orderDetails.add(detail);
                }
            }
            
            if (orderDetails.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择菜品");
                return result;
            }
            
            OrderInfo createdOrder = orderService.createOrder(orderInfo, orderDetails);
            
            result.put("success", true);
            result.put("message", "订单创建成功");
            result.put("orderNo", createdOrder.getOrderNo());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "订单创建失败：" + e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/order/cancel")
    @ResponseBody
    public Map<String, Object> cancelOrder(@RequestParam Long orderId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            OrderInfo order = orderService.getById(orderId);
            if (order != null && "PENDING".equals(order.getOrderStatus())) {
                orderService.cancelOrder(orderId);
                result.put("success", true);
                result.put("message", "订单已取消");
            } else {
                result.put("success", false);
                result.put("message", "订单状态不允许取消");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消订单失败：" + e.getMessage());
        }
        
        return result;
    }
}
