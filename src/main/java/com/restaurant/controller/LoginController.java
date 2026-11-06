package com.restaurant.controller;

import com.restaurant.common.RememberMeToken;
import com.restaurant.common.Result;
import com.restaurant.entity.User;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    @ResponseBody
    public Result<User> login(@RequestParam String username, 
                              @RequestParam String password,
                              @RequestParam(value = "remember", defaultValue = "false") Boolean remember,
                              HttpSession session,
                              HttpServletResponse response) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("realName", user.getRealName());
            session.setAttribute("role", user.getRole());

            if (Boolean.TRUE.equals(remember)) {
                Cookie cookie = new Cookie(RememberMeToken.COOKIE_NAME, RememberMeToken.create(user.getUsername()));
                cookie.setPath("/");
                cookie.setMaxAge(RememberMeToken.MAX_AGE_SECONDS);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            } else {
                clearRememberMeCookie(response);
            }
            
            return Result.success("登录成功", user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        clearRememberMeCookie(response);
        return "redirect:/login";
    }

    private void clearRememberMeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(RememberMeToken.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
    
    @GetMapping("/home")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        String role = user.getRole();
        switch (role) {
            case "MANAGER":
                return "redirect:/manager/home";
            case "KITCHEN":
                return "redirect:/kitchen/home";
            case "DISTRIBUTOR":
                return "redirect:/distributor/home";
            case "FINANCE":
                return "redirect:/finance/home";
            case "EMPLOYEE":
                return "redirect:/employee/home";
            default:
                return "redirect:/login";
        }
    }
}
