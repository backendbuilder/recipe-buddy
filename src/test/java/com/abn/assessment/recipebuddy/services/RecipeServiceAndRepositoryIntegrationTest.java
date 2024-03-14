package com.abn.assessment.recipebuddy.services;

import com.abn.assessment.recipebuddy.enums.Unit;
import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.repositories.RecipeRepository;
import com.abn.assessment.recipebuddy.testHelper.TestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@Import(RecipeService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeServiceAndRepositoryIntegrationTest {

    @Container
    public static DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose_test.yml"))
                    .withExposedService("recipe-buddy-db", 5434);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:///recipe-buddy-db" ); //
        registry.add("spring.datasource.username", () -> "recipe-buddy-user");
        registry.add("spring.datasource.password", () -> "recipe-buddy-password");
    }

    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeRepository recipeRepository;

    @BeforeAll
    static void setup() {

    }

    @Test
    void createAndRetrieveRecipe() {

        //prepare
        Recipe recipe = new Recipe();
        recipe.setName("Pasta Recipe");
        recipe.setInstructions("Step 1: do this \nStep 2: do that");
        recipe.setServings(4);
        recipe.setPreparationTime(30); // in minutes
        recipe.setVegetarian(true);

        Ingredient cheese = new Ingredient();
        cheese.setName("cheese");
        cheese.setUnit(Unit.GRAM);
        cheese.setQuantity(80);
        Ingredient spaghetti = new Ingredient();
        spaghetti.setName("spaghetti");
        spaghetti.setUnit(Unit.CUP);
        spaghetti.setQuantity(2);

        List<Ingredient> pastaIngredients = new ArrayList<>();
        pastaIngredients.add(cheese);
        pastaIngredients.add(spaghetti);
        recipe.setIngredients(pastaIngredients);

        //execute
        recipeService.createRecipe(recipe);
        List<Recipe> recipeList =  recipeService.readRecipes("Pasta Recipe", false, 0, null, Collections.emptyList(), Collections.emptyList());

        //verify
        assertEquals(1, recipeList.size());
        assertEquals("Pasta Recipe", recipeList.get(0).getName());
        assertEquals("Step 1: do this \nStep 2: do that", recipeList.get(0).getInstructions());
        assertEquals(4, recipeList.get(0).getServings());
        assertEquals(30, recipeList.get(0).getPreparationTime());
        assertTrue(recipeList.get(0).isVegetarian());
        assertEquals(2, recipeList.get(0).getIngredients().size());
        assertEquals("cheese", recipeList.get(0).getIngredients().get(0).getName());
        assertEquals(Unit.GRAM, recipeList.get(0).getIngredients().get(0).getUnit());
        assertEquals(80, recipeList.get(0).getIngredients().get(0).getQuantity());

    }

    @Test
    void createAndRetrieveRecipesWithFilters() {

        //prepare
        Recipe recipe = new Recipe();
        recipe.setName("Pasta Recipe");
        recipe.setInstructions("Step 1: do this \nStep 2: do that");
        recipe.setServings(4);
        recipe.setPreparationTime(30); // in minutes
        recipe.setVegetarian(true);

        Ingredient cheese = new Ingredient();
        cheese.setName("cheese");
        cheese.setUnit(Unit.GRAM);
        cheese.setQuantity(80);
        Ingredient spaghetti = new Ingredient();
        spaghetti.setName("spaghetti");
        spaghetti.setUnit(Unit.CUP);
        spaghetti.setQuantity(2);

        List<Ingredient> pastaIngredients = new ArrayList<>();
        pastaIngredients.add(cheese);
        pastaIngredients.add(spaghetti);
        recipe.setIngredients(pastaIngredients);

        //execute
        recipeService.createRecipe(recipe);
        List<Recipe> recipes = recipeRepository.findWithFilters("Pasta Recipe", true, 4, "Step");
        assertEquals(1, recipes.size());
    }
    @Test
    void createAndRetrieveRecipesWithFilters2(){
        //prepare
        String name = TestHelper.getUnsavedRecipe().getName();
        boolean isVegetarian = TestHelper.getUnsavedRecipe().isVegetarian();
        int servings = TestHelper.getUnsavedRecipe().getServings();
        String containsWord = "do";

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("included1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("included2");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("excluded1");
        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("excluded2");

        Recipe recipe1 = TestHelper.cloneRecipe(TestHelper.getUnsavedRecipe());
        recipe1.getIngredients().add(ingredient1);
        recipe1.getIngredients().add(ingredient2);
        recipe1.getIngredients().add(ingredient3);
        recipe1.getIngredients().add(ingredient4);
        Recipe recipe2 = TestHelper.cloneRecipe(TestHelper.getUnsavedRecipe());
        recipe2.getIngredients().add(ingredient1);
        recipe2.getIngredients().add(ingredient2);
        recipe2.getIngredients().add(ingredient4);
        Recipe recipe3 = TestHelper.cloneRecipe(TestHelper.getUnsavedRecipe());
        recipe3.getIngredients().add(ingredient1);
        Recipe recipe4 = TestHelper.cloneRecipe(TestHelper.getUnsavedRecipe());
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

        recipeService.createRecipe(recipe1);
        recipeService.createRecipe(recipe2);
        recipeService.createRecipe(recipe3);
        recipeService.createRecipe(recipe4);

        List<Recipe> filteredRecipes = recipeService.readRecipes(name, isVegetarian, servings, containsWord, included, excluded);
        assertEquals(1, filteredRecipes.size());

    }


}