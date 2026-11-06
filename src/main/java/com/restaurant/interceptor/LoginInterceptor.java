package com.restaurant.interceptor;

import com.restaurant.common.RememberMeToken;
import com.restaurant.entity.User;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            user = restoreFromRememberMeCookie(request, response);
        }

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    private User restoreFromRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (RememberMeToken.COOKIE_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null || token.isEmpty()) {
            return null;
        }

        String username = RememberMeToken.verify(token);
        User user = username == null ? null : userService.getUserByUsername(username);
        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            Cookie expired = new Cookie(RememberMeToken.COOKIE_NAME, "");
            expired.setPath("/");
            expired.setMaxAge(0);
            expired.setHttpOnly(true);
            response.addCookie(expired);
            return null;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("realName", user.getRealName());
        session.setAttribute("role", user.getRole());
        return user;
    }
}
