package com.abn.assessment.recipebuddy.services;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.exceptions.EntityNotFoundException;
import com.abn.assessment.recipebuddy.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private static final String NOT_FOUND = "Recipe not found";

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * CREATE (add) Recipe
     * @param recipe
     * @return created Recipe
     */
    public Recipe createRecipe(Recipe recipe){

        addRecipeIdToIngredients(recipe);
        return recipeRepository.save(recipe);
    }

    private void addRecipeIdToIngredients(Recipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredients.forEach(ingredient -> ingredient.setRecipe(recipe));
    }

    /**
     * READ (find) Recipe's by name
     * @param name
     * @return list of Recipe with given name
     */
    public List<Recipe> readRecipeByName(String name){
        return recipeRepository.findByNameIgnoreCase(name);
    }

    /**
     * UPDATE Recipe
     * @param recipe
     * @return updated Recipe
     */
    public Recipe updateRecipe(Recipe recipe) throws EntityNotFoundException {
        if (recipeRepository.existsById(recipe.getId())) {
            return  recipeRepository.save(recipe);
        } else throw new EntityNotFoundException(NOT_FOUND);

        /*TODO remove all unused ingredients that are still in the database after updating a recipe*/

    }

    /**
     * DELETE Recipe by id
     * @param id
     */
    public void deleteRecipe(String id) throws EntityNotFoundException {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

}
