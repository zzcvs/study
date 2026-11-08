package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuService extends IService<Menu> {
    
    List<Menu> getTodayMenu(LocalDate date);
    
    List<Menu> getMenuByDateAndMealType(LocalDate date, String mealType);

    /**
     * 查询指定日期、餐别的菜单；当天还没有菜单时，按星期排班自动生成一份并返回。
     */
    List<Menu> getOrCreateMenu(LocalDate date, String mealType);
    
    boolean updateStock(Long menuId, Integer quantity);
}
