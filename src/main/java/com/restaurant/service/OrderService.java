package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.OrderInfo;
import com.restaurant.entity.OrderDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    
    OrderInfo createOrder(OrderInfo orderInfo, List<OrderDetail> orderDetails);
    
    OrderInfo getOrderByOrderNo(String orderNo);
    
    List<OrderInfo> getOrdersByDate(LocalDate date);
    
    List<OrderInfo> getOrdersByUserId(Long userId);
    
    List<OrderInfo> getOrdersByStatus(String status);

    List<OrderInfo> getOrdersByDistributeStatus(String status);

    List<OrderInfo> getReadyOrdersForDistribute();
    
    boolean updateOrderStatus(Long orderId, String status);
    
    boolean updateDistributeStatus(Long orderId, String status);
    
    boolean cancelOrder(Long orderId);
    
    List<OrderDetail> getOrderDetails(Long orderId);
}
