package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService extends IService<Recipe> {
    
    List<Recipe> getRecipesByCategory(String category);
    
    List<Recipe> getAvailableRecipes();
    
    boolean uploadRecipeImage(Long recipeId, MultipartFile file);
}
