package com.abn.assessment.recipebuddy.repositories;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {

}
