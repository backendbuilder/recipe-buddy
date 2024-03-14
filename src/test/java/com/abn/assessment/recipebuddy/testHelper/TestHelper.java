package com.abn.assessment.recipebuddy.testHelper;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.enums.Unit;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    private static final String ID_1 = "ID-1";
    private static final String PASTA_RECIPE = "Pasta Recipe";
    private static final String STEPS = "Step 1: do this. Step 2: do that";
    private static final String ID_2 = "ID-2";
    private static final String CHEESE = "cheese";
    private static final String ID_3 = "ID-3";
    private static final String SPAGHETTI = "spaghetti";
    public static final int CHEESE_QUANTITY = 80;
    public static final int SPAGHETTI_QUANTITY = 2;
    public static final int PREPARATION_TIME = 30;
    public static final int SERVINGS = 4;

    public static Recipe getCompletelySavedRecipe(){
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(ID_1);
        savedRecipe.setName(PASTA_RECIPE);
        savedRecipe.setInstructions(STEPS);
        savedRecipe.setServings(SERVINGS);
        savedRecipe.setPreparationTime(PREPARATION_TIME); // in minutes
        savedRecipe.setVegetarian(true);

        Ingredient savedCheese = new Ingredient();
        savedCheese.setId(ID_2);
        savedCheese.setRecipe(savedRecipe);
        savedCheese.setName(CHEESE);
        savedCheese.setUnit(Unit.GRAM);
        savedCheese.setQuantity(CHEESE_QUANTITY);

        Ingredient savedSpaghetti = new Ingredient();
        savedSpaghetti.setId(ID_3);
        savedSpaghetti.setRecipe(savedRecipe);
        savedSpaghetti.setName(SPAGHETTI);
        savedSpaghetti.setUnit(Unit.CUP);
        savedSpaghetti.setQuantity(SPAGHETTI_QUANTITY);

        List<Ingredient> savedPastaIngredients = new ArrayList<>();
        savedPastaIngredients.add(savedCheese);
        savedPastaIngredients.add(savedSpaghetti);
        savedRecipe.setIngredients(savedPastaIngredients);
        return savedRecipe;
    }

    public static Recipe getSavedRecipeWithoutUpdatedIngredients(){
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(ID_1);
        savedRecipe.setName(PASTA_RECIPE);
        savedRecipe.setInstructions(STEPS);
        savedRecipe.setServings(SERVINGS);
        savedRecipe.setPreparationTime(PREPARATION_TIME); // in minutes
        savedRecipe.setVegetarian(true);

        Ingredient savedCheese = new Ingredient();
        savedCheese.setId(ID_2);
        savedCheese.setRecipe(savedRecipe);
        savedCheese.setName(CHEESE);
        savedCheese.setUnit(Unit.GRAM);
        savedCheese.setQuantity(CHEESE_QUANTITY);

        Ingredient savedSpaghetti = new Ingredient();
        savedSpaghetti.setId(ID_3);
        savedSpaghetti.setRecipe(savedRecipe);
        savedSpaghetti.setName(SPAGHETTI);
        savedSpaghetti.setUnit(Unit.CUP);
        savedSpaghetti.setQuantity(SPAGHETTI_QUANTITY);

        List<Ingredient> savedPastaIngredients = new ArrayList<>();
        savedPastaIngredients.add(savedCheese);
        savedPastaIngredients.add(savedSpaghetti);
        savedRecipe.setIngredients(savedPastaIngredients);
        return savedRecipe;
    }

    public static Recipe getUnsavedRecipe(){
        Recipe savedRecipe = new Recipe();

        savedRecipe.setName(PASTA_RECIPE);
        savedRecipe.setInstructions(STEPS);
        savedRecipe.setServings(SERVINGS);
        savedRecipe.setPreparationTime(PREPARATION_TIME); // in minutes
        savedRecipe.setVegetarian(true);

        Ingredient savedCheese = new Ingredient();
        savedCheese.setName(CHEESE);
        savedCheese.setUnit(Unit.GRAM);
        savedCheese.setQuantity(CHEESE_QUANTITY);

        Ingredient savedSpaghetti = new Ingredient();
        savedSpaghetti.setName(SPAGHETTI);
        savedSpaghetti.setUnit(Unit.CUP);
        savedSpaghetti.setQuantity(SPAGHETTI_QUANTITY);

        List<Ingredient> savedPastaIngredients = new ArrayList<>();
        savedPastaIngredients.add(savedCheese);
        savedPastaIngredients.add(savedSpaghetti);
        savedRecipe.setIngredients(savedPastaIngredients);
        return savedRecipe;
    }

    public static Recipe getCompletelySavedRecipe2(){
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(ID_1);
        savedRecipe.setName(PASTA_RECIPE);
        savedRecipe.setInstructions(STEPS);
        savedRecipe.setServings(SERVINGS);
        savedRecipe.setPreparationTime(PREPARATION_TIME); // in minutes
        savedRecipe.setVegetarian(true);

        Ingredient savedCheese = new Ingredient();
        savedCheese.setId(ID_2);
        savedCheese.setRecipe(savedRecipe);
        savedCheese.setName(CHEESE);
        savedCheese.setUnit(Unit.GRAM);
        savedCheese.setQuantity(CHEESE_QUANTITY);

        Ingredient savedSpaghetti = new Ingredient();
        savedSpaghetti.setId(ID_3);
        savedSpaghetti.setRecipe(savedRecipe);
        savedSpaghetti.setName(SPAGHETTI);
        savedSpaghetti.setUnit(Unit.CUP);
        savedSpaghetti.setQuantity(SPAGHETTI_QUANTITY);

        List<Ingredient> savedPastaIngredients = new ArrayList<>();
        savedPastaIngredients.add(savedCheese);
        savedPastaIngredients.add(savedSpaghetti);
        savedRecipe.setIngredients(savedPastaIngredients);
        return savedRecipe;
    }

    public static Recipe cloneRecipe(Recipe originalRecipe){
        Recipe clonedRecipe = new Recipe();
        clonedRecipe.setId(originalRecipe.getId());
        clonedRecipe.setName(originalRecipe.getName());
        clonedRecipe.setInstructions(originalRecipe.getInstructions());
        clonedRecipe.setServings(originalRecipe.getServings());
        clonedRecipe.setPreparationTime(originalRecipe.getPreparationTime());
        clonedRecipe.setVegetarian(originalRecipe.isVegetarian());

        List<Ingredient> clonedIngredients = new ArrayList<>();
        for(Ingredient originalIngredient : originalRecipe.getIngredients()){
            Ingredient clonedIngredient = new Ingredient();
            clonedIngredient.setId(originalIngredient.getId());
            clonedIngredient.setRecipe(clonedRecipe);
            clonedIngredient.setName(originalIngredient.getName());
            clonedIngredient.setUnit(originalIngredient.getUnit());
            clonedIngredient.setQuantity(originalIngredient.getQuantity());
            clonedIngredients.add(clonedIngredient);
        }

        clonedRecipe.setIngredients(clonedIngredients);
        return clonedRecipe;
    }


}