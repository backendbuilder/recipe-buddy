package com.abn.assessment.recipebuddy.repositories;

import com.abn.assessment.recipebuddy.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    List<Recipe> findByNameIgnoreCase(String name);

    boolean existsByName(String name);
}
