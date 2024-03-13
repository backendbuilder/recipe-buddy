package com.abn.assessment.recipebuddy.repositories;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {

}
