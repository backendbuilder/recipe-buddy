package com.abn.assessment.recipebuddy.controllers;

import com.abn.assessment.recipebuddy.exceptions.EntityNotFoundException;
import com.abn.assessment.recipebuddy.services.RecipeService;
import com.abn.assessment.recipebuddy.entities.Recipe;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @GetMapping()
    public ResponseEntity<List<Recipe>> getRecipe(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) boolean isVegetarian,
                                                  @RequestParam(required = false, defaultValue = "0") int servings,
                                                  @RequestParam(required = false) String instructionsContain,
                                                  @RequestParam(required = false) Optional<List<String>> includedIngredients,
                                                  @RequestParam(required = false) Optional<List<String>> excludedIngredients
                                                    ) {
        List<String> included = includedIngredients.orElse(Collections.emptyList());
        List<String> excluded = excludedIngredients.orElse(Collections.emptyList());
        List<Recipe> recipes = recipeService.readRecipes(name, isVegetarian, servings, instructionsContain, included, excluded);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe updatedRecipe) throws EntityNotFoundException {
        Recipe updated = recipeService.updateRecipe(updatedRecipe);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteRecipe(@RequestParam String id) throws EntityNotFoundException {
        recipeService.deleteRecipe(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}