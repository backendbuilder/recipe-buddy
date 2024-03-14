package com.abn.assessment.recipebuddy.services;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.exceptions.EntityNotFoundException;
import com.abn.assessment.recipebuddy.repositories.IngredientRepository;
import com.abn.assessment.recipebuddy.repositories.RecipeRepository;
import com.abn.assessment.recipebuddy.testHelper.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe unsavedRecipe;
    private Recipe savedRecipeWithoutUpdatedIngredients;
    private Recipe savedRecipe;

    @BeforeEach
    void setUp() {
        unsavedRecipe = TestHelper.getUnsavedRecipe();
        savedRecipeWithoutUpdatedIngredients = TestHelper.getSavedRecipeWithoutUpdatedIngredients();
        savedRecipe = TestHelper.getCompletelySavedRecipe();
    }

    @Test
    void createRecipe_ShouldUpdateIngredientIds_AndSave_AndReturn() {
        //prepare
        when(recipeRepository.save(unsavedRecipe)).thenReturn(savedRecipeWithoutUpdatedIngredients);

        //perform
        Recipe actualRecipe = recipeService.createRecipe(unsavedRecipe);

        //verify
        assertEquals(savedRecipeWithoutUpdatedIngredients, actualRecipe);
        assertEquals(savedRecipe.getId(), actualRecipe.getId());
        assertEquals(savedRecipe.getIngredients().size(), actualRecipe.getIngredients().size());
        assertEquals(savedRecipe.getIngredients().get(0).getId(), actualRecipe.getIngredients().get(0).getId());
        assertEquals(savedRecipe.getIngredients().get(1).getId(), actualRecipe.getIngredients().get(1).getId());
        assertEquals(savedRecipeWithoutUpdatedIngredients, actualRecipe.getIngredients().get(0).getRecipe());
        assertEquals(savedRecipeWithoutUpdatedIngredients, actualRecipe.getIngredients().get(1).getRecipe());
        verify(recipeRepository).save(unsavedRecipe);
        verify(ingredientRepository).saveAll(savedRecipeWithoutUpdatedIngredients.getIngredients());
    }

    @Test
    void readRecipes_ShouldReturnFilteredRecipes() {
        //prepare
        String name = "recipe1";
        boolean isVegetarian = true;
        int servings = 2;
        String containsWord = "Boil";

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("included1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("included2");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("excluded1");
        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("excluded2");

        Recipe recipe1 = TestHelper.cloneRecipe(savedRecipe);
        recipe1.getIngredients().add(ingredient1);
        recipe1.getIngredients().add(ingredient2);
        recipe1.getIngredients().add(ingredient3);
        recipe1.getIngredients().add(ingredient4);
        Recipe recipe2 = TestHelper.cloneRecipe(savedRecipe);
        recipe2.getIngredients().add(ingredient1);
        recipe2.getIngredients().add(ingredient2);
        recipe2.getIngredients().add(ingredient4);
        Recipe recipe3 = TestHelper.cloneRecipe(savedRecipe);
        recipe3.getIngredients().add(ingredient1);
        Recipe recipe4 = TestHelper.cloneRecipe(savedRecipe);
        recipe4.getIngredients().add(ingredient1);
        recipe4.getIngredients().add(ingredient2);

        List<String> included = new ArrayList<>();
        included.add("included1");
        included.add("included2");

        List<String> excluded = new ArrayList<>();
        excluded.add("excluded1");
        excluded.add("excluded2");

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);
        recipes.add(recipe4);

        when(recipeRepository.findWithFilters(name, isVegetarian, servings, containsWord)).thenReturn(recipes);

        //perform
        List<Recipe> actualRecipes = recipeService.readRecipes(name, isVegetarian, servings, containsWord, included, excluded);

        //verify
        assertEquals(1, actualRecipes.size());
        assertEquals(List.of(recipe4), actualRecipes);

        List<Ingredient> ingredients = actualRecipes.get(0).getIngredients();
        for(String ingredient : included){
           assertTrue(ingredients.stream().anyMatch(i -> i.getName().equals(ingredient)));
        }
        for(String ingredient : excluded){
            assertTrue(ingredients.stream().noneMatch(i -> i.getName().equals(ingredient)));
        }
    }

    @Test
    void updateRecipe_ShouldSaveRecipe_IfAlreadyExists() throws EntityNotFoundException {
        //prepare
        Recipe toBeUpdatedRecipe = TestHelper.getCompletelySavedRecipe2();
        when(recipeRepository.existsById(toBeUpdatedRecipe.getId())).thenReturn(true);
        when(recipeRepository.save(toBeUpdatedRecipe)).thenReturn(savedRecipe);

        //perform
        Recipe actualRecipe = recipeService.updateRecipe(toBeUpdatedRecipe);

        //verify
        assertEquals(savedRecipe, actualRecipe);
        assertEquals(savedRecipe.getId(), actualRecipe.getId());
        assertEquals(savedRecipe.getIngredients().size(), actualRecipe.getIngredients().size());
        assertEquals(savedRecipe.getIngredients().get(0).getId(), actualRecipe.getIngredients().get(0).getId());
        assertEquals(savedRecipe.getIngredients().get(1).getId(), actualRecipe.getIngredients().get(1).getId());
        assertEquals(savedRecipe, actualRecipe.getIngredients().get(0).getRecipe());
        assertEquals(savedRecipe, actualRecipe.getIngredients().get(1).getRecipe());
        verify(recipeRepository).save(toBeUpdatedRecipe);
        verify(ingredientRepository).saveAll(toBeUpdatedRecipe.getIngredients());
    }

    @Test
    void updateRecipe_ShouldThrowException_IfNotYetExists() {
        //prepare
        Recipe toBeUpdatedRecipe = TestHelper.getCompletelySavedRecipe2();
        when(recipeRepository.existsById(toBeUpdatedRecipe.getId())).thenReturn(false);

        //Perform and verify
        assertThrows(EntityNotFoundException.class, () -> recipeService.updateRecipe(toBeUpdatedRecipe));
    }

    @Test
    void deleteRecipe_ShouldBePerformedOnce_IfIdExists() throws EntityNotFoundException {
        //Prepare
        String id = "ID4";
        when(recipeRepository.existsById(id)).thenReturn(true);

        //Perform
        recipeService.deleteRecipe(id);

        //Verify
        verify(recipeRepository).deleteById(id);
    }

    @Test
    void deleteRecipe_ShouldThrowEntityNotFoundException_IfIdNotExists() {
        //Prepare
        String id = "ID4";
        when(recipeRepository.existsById(id)).thenReturn(false);

        //Verify
        assertThrows(EntityNotFoundException.class, () -> recipeService.deleteRecipe(id));
    }
}