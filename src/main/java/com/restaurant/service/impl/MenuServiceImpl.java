package com.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Menu;
import com.restaurant.mapper.MenuMapper;
import com.restaurant.service.MenuService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    
    @Override
    public List<Menu> getTodayMenu(LocalDate date) {
        return baseMapper.getTodayMenu(date);
    }
    
    @Override
    public List<Menu> getMenuByDateAndMealType(LocalDate date, String mealType) {
        return baseMapper.getMenuWithRecipe(date, mealType);
    }
    
    @Override
    public boolean updateStock(Long menuId, Integer quantity) {
        Menu menu = getById(menuId);
        if (menu != null && menu.getStock() >= quantity) {
            menu.setStock(menu.getStock() - quantity);
            menu.setSoldCount(menu.getSoldCount() + quantity);
            return updateById(menu);
        }
        return false;
    }
}
