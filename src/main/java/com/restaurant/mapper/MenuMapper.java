package com.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuWithRecipe(@Param("menuDate") LocalDate menuDate, @Param("mealType") String mealType);

    List<Menu> getTodayMenu(@Param("menuDate") LocalDate menuDate);
}
