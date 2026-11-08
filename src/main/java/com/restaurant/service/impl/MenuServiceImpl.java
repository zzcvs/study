package com.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Menu;
import com.restaurant.entity.Recipe;
import com.restaurant.mapper.MenuMapper;
import com.restaurant.service.MenuService;
import com.restaurant.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /** 自动生成菜单时的默认库存 */
    private static final int DEFAULT_STOCK = 100;

    /** 早餐只安排主食、甜点、饮料 */
    private static final Set<String> BREAKFAST_CATEGORIES =
            new HashSet<>(Arrays.asList("主食", "甜点", "饮料"));

    /**
     * 周一~周日的固定排班：每天 6 道（热菜、凉菜、汤品、主食、甜点、饮料各一道），
     * 7 天两两不重复，每周循环。
     */
    private static final Map<DayOfWeek, List<String[]>> WEEKLY_PLAN = new HashMap<>();

    static {
        WEEKLY_PLAN.put(DayOfWeek.MONDAY, plan(
                dish("热菜", "红烧肉"), dish("凉菜", "凉拌黄瓜"), dish("汤品", "西红柿鸡蛋汤"),
                dish("主食", "蛋炒饭"), dish("甜点", "银耳莲子汤"), dish("饮料", "可乐")));
        WEEKLY_PLAN.put(DayOfWeek.TUESDAY, plan(
                dish("热菜", "宫保鸡丁"), dish("凉菜", "拍黄瓜"), dish("汤品", "紫菜蛋花汤"),
                dish("主食", "扬州炒饭"), dish("甜点", "绿豆汤"), dish("饮料", "雪碧")));
        WEEKLY_PLAN.put(DayOfWeek.WEDNESDAY, plan(
                dish("热菜", "清蒸鲈鱼"), dish("凉菜", "凉拌黄瓜"), dish("汤品", "紫菜蛋花汤"),
                dish("主食", "蛋炒饭"), dish("甜点", "绿豆汤"), dish("饮料", "可乐")));
        WEEKLY_PLAN.put(DayOfWeek.THURSDAY, plan(
                dish("热菜", "红烧肉"), dish("凉菜", "拍黄瓜"), dish("汤品", "西红柿鸡蛋汤"),
                dish("主食", "扬州炒饭"), dish("甜点", "银耳莲子汤"), dish("饮料", "雪碧")));
        WEEKLY_PLAN.put(DayOfWeek.FRIDAY, plan(
                dish("热菜", "宫保鸡丁"), dish("凉菜", "凉拌黄瓜"), dish("汤品", "西红柿鸡蛋汤"),
                dish("主食", "蛋炒饭"), dish("甜点", "绿豆汤"), dish("饮料", "雪碧")));
        WEEKLY_PLAN.put(DayOfWeek.SATURDAY, plan(
                dish("热菜", "清蒸鲈鱼"), dish("凉菜", "拍黄瓜"), dish("汤品", "紫菜蛋花汤"),
                dish("主食", "扬州炒饭"), dish("甜点", "银耳莲子汤"), dish("饮料", "可乐")));
        WEEKLY_PLAN.put(DayOfWeek.SUNDAY, plan(
                dish("热菜", "红烧肉"), dish("凉菜", "凉拌黄瓜"), dish("汤品", "紫菜蛋花汤"),
                dish("主食", "蛋炒饭"), dish("甜点", "银耳莲子汤"), dish("饮料", "雪碧")));
    }

    private static String[] dish(String category, String name) {
        return new String[]{category, name};
    }

    private static List<String[]> plan(String[]... dishes) {
        return Arrays.asList(dishes);
    }

    @Autowired
    private RecipeService recipeService;

    @Override
    public List<Menu> getTodayMenu(LocalDate date) {
        return baseMapper.getTodayMenu(date);
    }

    @Override
    public List<Menu> getMenuByDateAndMealType(LocalDate date, String mealType) {
        return baseMapper.getMenuWithRecipe(date, mealType);
    }

    @Override
    public List<Menu> getOrCreateMenu(LocalDate date, String mealType) {
        List<Menu> menus = getMenuByDateAndMealType(date, mealType);
        if (!menus.isEmpty()) {
            return menus;
        }
        generateMenu(date, mealType);
        return getMenuByDateAndMealType(date, mealType);
    }

    /**
     * 按星期排班为该日期、该餐别生成菜单。只在当天该餐别没有任何菜单时调用，
     * 已存在的菜单（含库存、已售）不会被覆盖。当天若某道菜已被删除或下架，
     * 会从同分类里轮换一道顶替；整个分类都没有菜时跳过该道，不会报错。
     */
    private void generateMenu(LocalDate date, String mealType) {
        List<String[]> plan = WEEKLY_PLAN.get(date.getDayOfWeek());
        if (plan == null) {
            return;
        }

        List<Recipe> available = recipeService.getAvailableRecipes();
        if (available == null || available.isEmpty()) {
            return;
        }

        Map<String, List<Recipe>> byCategory = new LinkedHashMap<>();
        for (Recipe recipe : available) {
            byCategory.computeIfAbsent(recipe.getCategory(), key -> new ArrayList<>()).add(recipe);
        }

        boolean breakfast = "早餐".equals(mealType);
        int rotationStart = date.getDayOfWeek().getValue() - 1;
        Set<Long> usedRecipeIds = new HashSet<>();
        List<Menu> menus = new ArrayList<>();
        int slot = 0;

        for (String[] item : plan) {
            String category = item[0];
            String name = item[1];
            int currentSlot = slot++;

            if (breakfast && !BREAKFAST_CATEGORIES.contains(category)) {
                continue;
            }
            List<Recipe> candidates = byCategory.get(category);
            if (candidates == null || candidates.isEmpty()) {
                continue;
            }

            Recipe picked = findByName(candidates, name);
            if (picked == null || usedRecipeIds.contains(picked.getId())) {
                picked = pickByRotation(candidates, rotationStart + currentSlot, usedRecipeIds);
            }
            if (picked == null) {
                continue;
            }
            usedRecipeIds.add(picked.getId());

            Menu menu = new Menu();
            menu.setMenuDate(date);
            menu.setMealType(mealType);
            menu.setRecipeId(picked.getId());
            menu.setStock(DEFAULT_STOCK);
            menu.setSoldCount(0);
            menu.setStatus(1);
            menus.add(menu);
        }

        if (menus.isEmpty()) {
            return;
        }

        try {
            saveBatch(menus);
        } catch (DuplicateKeyException e) {
            // 并发请求已经生成了同一天的菜单，忽略即可
        }
    }

    private Recipe findByName(List<Recipe> candidates, String name) {
        for (Recipe recipe : candidates) {
            if (name.equals(recipe.getName())) {
                return recipe;
            }
        }
        return null;
    }

    private Recipe pickByRotation(List<Recipe> candidates, int start, Set<Long> usedRecipeIds) {
        int size = candidates.size();
        for (int i = 0; i < size; i++) {
            Recipe candidate = candidates.get(Math.floorMod(start + i, size));
            if (!usedRecipeIds.contains(candidate.getId())) {
                return candidate;
            }
        }
        return null;
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
