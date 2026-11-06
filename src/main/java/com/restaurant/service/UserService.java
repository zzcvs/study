package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.User;

public interface UserService extends IService<User> {
    
    User login(String username, String password);
    
    User getUserByUsername(String username);
    
    boolean checkPassword(String rawPassword, String encodedPassword);
}
