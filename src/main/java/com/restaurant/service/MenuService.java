package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuService extends IService<Menu> {
    
    List<Menu> getTodayMenu(LocalDate date);
    
    List<Menu> getMenuByDateAndMealType(LocalDate date, String mealType);
    
    boolean updateStock(Long menuId, Integer quantity);
}
