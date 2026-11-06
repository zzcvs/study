package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.entity.OrderInfo;
import com.restaurant.entity.Recipe;
import com.restaurant.entity.User;
import com.restaurant.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private FinanceService financeService;
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        List<Recipe> recipes = recipeService.getAvailableRecipes();
        model.addAttribute("recipes", recipes);
        
        List<OrderInfo> todayOrders = orderService.getOrdersByDate(LocalDate.now());
        model.addAttribute("todayOrders", todayOrders);
        
        Map<String, Object> dailyStats = financeService.getDailyStatistics(LocalDate.now());
        model.addAttribute("dailyStats", dailyStats);
        
        List<User> users = userService.list();
        model.addAttribute("users", users);
        
        return "manager/home";
    }
    
    @GetMapping("/users")
    public String users(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<User> users = userService.list();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        
        return "manager/users";
    }
    
    @GetMapping("/recipes")
    public String recipes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<Recipe> recipes = recipeService.list();
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        
        return "manager/recipes";
    }
    
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<OrderInfo> orders = orderService.list();
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        
        return "manager/orders";
    }
    
    @GetMapping("/finance")
    public String finance(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Map<String, Object> dailyStats = financeService.getDailyStatistics(LocalDate.now());
        model.addAttribute("dailyStats", dailyStats);
        model.addAttribute("user", user);
        
        return "manager/finance";
    }

    @GetMapping("/finance/export")
    public void exportFinance(@RequestParam(required = false) String date,
                              HttpSession session,
                              HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            response.sendRedirect("/login");
            return;
        }

        LocalDate queryDate;
        try {
            queryDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : LocalDate.now();
        } catch (DateTimeParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日期格式不正确");
            return;
        }

        List<OrderInfo> orders = orderService.getOrdersByDate(queryDate);
        Map<String, Object> dailyStats = financeService.getDailyStatistics(queryDate);

        response.setContentType("text/csv;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"finance-" + queryDate + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.write('\ufeff');
        writer.println("日期,订单号,用户,部门,餐别,金额,订单状态,支付状态");
        for (OrderInfo order : orders) {
            writer.println(String.join(",",
                    csv(queryDate.toString()),
                    csv(order.getOrderNo()),
                    csv(order.getUserName()),
                    csv(order.getDepartment()),
                    csv(order.getMealType()),
                    csv(order.getTotalAmount() == null ? "0.00" : order.getTotalAmount().toPlainString()),
                    csv(order.getOrderStatus()),
                    csv(order.getPaymentStatus())));
        }
        writer.println();
        writer.println("汇总项,数值");
        writer.println("订单数," + dailyStats.get("totalOrders"));
        writer.println("总营收," + dailyStats.get("totalAmount"));
        writer.println("已支付," + dailyStats.get("paidAmount"));
        writer.println("待支付," + dailyStats.get("unpaidAmount"));
        writer.flush();
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @PostMapping("/user/delete/{id}")
    @ResponseBody
    public Result<Void> deleteUser(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return Result.error("无权限");
        }
        userService.removeById(id);
        return Result.success("用户删除成功", null);
    }

    @PostMapping("/recipe/delete/{id}")
    @ResponseBody
    public Result<Void> deleteRecipe(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return Result.error("无权限");
        }
        recipeService.removeById(id);
        return Result.success("菜品删除成功", null);
    }

    @PostMapping("/order/delete/{id}")
    @ResponseBody
    public Result<Void> deleteOrder(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"MANAGER".equals(user.getRole())) {
            return Result.error("无权限");
        }
        orderService.removeById(id);
        return Result.success("订单删除成功", null);
    }
}
