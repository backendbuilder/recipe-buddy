package com.abn.assessment.recipebuddy.services;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.exceptions.EntityNotFoundException;
import com.abn.assessment.recipebuddy.repositories.IngredientRepository;
import com.abn.assessment.recipebuddy.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private static final String NOT_FOUND = "Recipe not found";

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Creates a new recipe by saving it to the database and updating the associated ingredients.
     *
     * @param recipe The recipe object to be created.
     * @return The saved recipe object.
     */
    public Recipe createRecipe(Recipe recipe) {
        Recipe savedRecipe = recipeRepository.save(recipe);
        List<Ingredient> ingredients = updateIngredientsWithRecipe(savedRecipe);
        ingredientRepository.saveAll(ingredients);
        return savedRecipe;
    }

    /**
     * Reads recipes based on the specified filters.
     *
     * @param name                The name of the recipe (optional).
     * @param isVegetarian        Indicates if the recipe should be vegetarian (optional).
     * @param servings            The number of servings (optional).
     * @param instructionsContain The keyword that the recipe instructions should contain (optional).
     * @param inclusedIngredients The list of included ingredients to filter on (optional).
     * @param excludedIngredients The list of excluded ingredients to filter on (optional).
     * @return The list of filtered recipes.
     */
    public List<Recipe> readRecipes(String name, boolean isVegetarian, int servings, String instructionsContain, List<String> inclusedIngredients, List<String> excludedIngredients) {
        List<Recipe> recipes = recipeRepository.findWithFilters(name, isVegetarian, servings, instructionsContain);
        recipes = filterOnIncludedIngredients(recipes, inclusedIngredients);
        recipes = filterOnExcludedIngredients(recipes, excludedIngredients);

        return recipes;
    }

    /**
     * Updates a recipe by saving it to the database and updating the associated ingredients.
     *
     * @param recipe The recipe object to be updated.
     * @return The updated recipe object.
     * @throws EntityNotFoundException if the recipe does not exist in the database.
     */
    public Recipe updateRecipe(Recipe recipe) throws EntityNotFoundException {
        if (recipeRepository.existsById(recipe.getId())) {
            List<Ingredient> ingredients = updateIngredientsWithRecipe(recipe);
            ingredientRepository.saveAll(ingredients);
            return recipeRepository.save(recipe);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }


    /**
     * Deletes a recipe from the database based on the provided ID.
     *
     * @param id The ID of the recipe to be deleted.
     * @throws EntityNotFoundException if the recipe does not exist in the database.
     */
    public void deleteRecipe(String id) throws EntityNotFoundException {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    /**
     * Updates the ingredients of a recipe with the provided recipe object.
     *
     * @param recipe The recipe object containing the new ingredients.
     * @return The updated list of ingredients.
     */
    private List<Ingredient> updateIngredientsWithRecipe(Recipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredients.forEach(ingredient -> ingredient.setRecipe(recipe));
        return ingredients;
    }

    /**
     * Filters a list of recipes based on included ingredients.
     *
     * @param recipes            The list of recipes to filter.
     * @param includedIngredients The list of included ingredients.
     * @return The filtered list of recipes.
     */
    private List<Recipe> filterOnIncludedIngredients(List<Recipe> recipes, List<String> includedIngredients) {
        return recipes.stream()
                .filter(recipe -> new HashSet<>(recipe.getIngredients().stream()
                        .map(Ingredient::getName)
                        .toList())
                        .containsAll(includedIngredients)
                ).toList();
    }

    /**
     * Filters a list of recipes based on excluded ingredients.
     *
     * @param recipes            The list of recipes to filter.
     * @param excludedIngredients The list of excluded ingredients.
     * @return The filtered list of recipes.
     */
    private List<Recipe> filterOnExcludedIngredients(List<Recipe> recipes, List<String> excludedIngredients) {
        return recipes.stream()
                .filter(recipe ->
                        recipe.getIngredients().stream()
                                .map(Ingredient::getName)
                                .noneMatch(excludedIngredients::contains)
                )
                .collect(Collectors.toList());
    }

}
