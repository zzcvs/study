package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Recipe;
import com.restaurant.mapper.RecipeMapper;
import com.restaurant.service.RecipeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {
    
    @Value("${upload.path:uploads}")
    private String uploadPath;
    
    @Override
    public List<Recipe> getRecipesByCategory(String category) {
        QueryWrapper<Recipe> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        queryWrapper.eq("status", 1);
        return list(queryWrapper);
    }
    
    @Override
    public List<Recipe> getAvailableRecipes() {
        QueryWrapper<Recipe> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return list(queryWrapper);
    }
    
    @Override
    public boolean uploadRecipeImage(Long recipeId, MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile);
            
            Recipe recipe = getById(recipeId);
            recipe.setImageUrl("/uploads/" + newFilename);
            return updateById(recipe);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
