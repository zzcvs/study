package com.restaurant.controller;

import com.restaurant.entity.Menu;
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
import java.util.List;

@Controller
@RequestMapping("/kitchen")
public class KitchenController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private MenuService menuService;
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"KITCHEN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        List<OrderInfo> pendingOrders = orderService.getOrdersByStatus("PENDING");
        model.addAttribute("pendingOrders", pendingOrders);
        
        List<OrderInfo> cookingOrders = orderService.getOrdersByStatus("COOKING");
        model.addAttribute("cookingOrders", cookingOrders);
        
        List<Menu> todayMenu = menuService.getTodayMenu(LocalDate.now());
        model.addAttribute("todayMenu", todayMenu);
        
        return "kitchen/home";
    }
    
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"KITCHEN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<OrderInfo> orders = orderService.getOrdersByDate(LocalDate.now());
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        
        return "kitchen/orders";
    }
    
    @PostMapping("/order/startCooking")
    @ResponseBody
    public String startCooking(@RequestParam Long orderId) {
        orderService.updateOrderStatus(orderId, "COOKING");
        return "success";
    }
    
    @PostMapping("/order/complete")
    @ResponseBody
    public String completeOrder(@RequestParam Long orderId) {
        orderService.updateOrderStatus(orderId, "READY");
        return "success";
    }
}
