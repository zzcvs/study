package com.restaurant.controller;

import com.restaurant.entity.OrderDetail;
import com.restaurant.entity.OrderInfo;
import com.restaurant.entity.User;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/distributor")
public class DistributorController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"DISTRIBUTOR".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        List<OrderInfo> readyOrders = orderService.getReadyOrdersForDistribute();
        model.addAttribute("readyOrders", readyOrders);

        List<OrderInfo> distributingOrders = orderService.getOrdersByDistributeStatus("DISTRIBUTING");
        model.addAttribute("distributingOrders", distributingOrders);
        
        return "distributor/home";
    }
    
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"DISTRIBUTOR".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<OrderInfo> orders = orderService.getOrdersByDate(LocalDate.now());
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        
        return "distributor/orders";
    }
    
    @GetMapping("/order/detail")
    public String orderDetail(@RequestParam Long orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"DISTRIBUTOR".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        OrderInfo order = orderService.getById(orderId);
        List<OrderDetail> details = orderService.getOrderDetails(orderId);
        
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        model.addAttribute("user", user);
        
        return "distributor/order-detail";
    }
    
    @PostMapping("/order/startDistribute")
    @ResponseBody
    public String startDistribute(@RequestParam Long orderId) {
        orderService.updateDistributeStatus(orderId, "DISTRIBUTING");
        return "success";
    }
    
    @PostMapping("/order/completeDistribute")
    @ResponseBody
    public String completeDistribute(@RequestParam Long orderId) {
        orderService.updateDistributeStatus(orderId, "COMPLETED");
        orderService.updateOrderStatus(orderId, "COMPLETED");
        return "success";
    }
}
