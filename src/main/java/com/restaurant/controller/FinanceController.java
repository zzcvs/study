package com.restaurant.controller;

import com.restaurant.entity.FinanceRecord;
import com.restaurant.entity.User;
import com.restaurant.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/finance")
public class FinanceController {
    
    @Autowired
    private FinanceService financeService;
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FINANCE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        Map<String, Object> dailyStats = financeService.getDailyStatistics(LocalDate.now());
        model.addAttribute("dailyStats", dailyStats);
        
        Map<String, Object> monthlyStats = financeService.getMonthlyStatistics(
            LocalDate.now().getYear(), 
            LocalDate.now().getMonthValue()
        );
        model.addAttribute("monthlyStats", monthlyStats);
        
        return "finance/home";
    }
    
    @GetMapping("/daily")
    public String dailyReport(@RequestParam(required = false) String date, 
                              HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FINANCE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        LocalDate queryDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Map<String, Object> dailyStats = financeService.getDailyStatistics(queryDate);
        
        model.addAttribute("dailyStats", dailyStats);
        model.addAttribute("queryDate", queryDate);
        model.addAttribute("user", user);
        
        return "finance/daily";
    }
    
    @GetMapping("/monthly")
    public String monthlyReport(@RequestParam(required = false) Integer year,
                                @RequestParam(required = false) Integer month,
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FINANCE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        int queryYear = year != null ? year : LocalDate.now().getYear();
        int queryMonth = month != null ? month : LocalDate.now().getMonthValue();
        
        Map<String, Object> monthlyStats = financeService.getMonthlyStatistics(queryYear, queryMonth);
        
        model.addAttribute("monthlyStats", monthlyStats);
        model.addAttribute("queryYear", queryYear);
        model.addAttribute("queryMonth", queryMonth);
        model.addAttribute("user", user);
        
        return "finance/monthly";
    }
    
    @GetMapping("/records")
    public String records(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FINANCE".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<FinanceRecord> records = financeService.list();
        model.addAttribute("records", records);
        model.addAttribute("user", user);
        
        return "finance/records";
    }
}
