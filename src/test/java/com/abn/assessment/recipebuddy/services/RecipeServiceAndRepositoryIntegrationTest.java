package com.abn.assessment.recipebuddy.services;

import com.abn.assessment.recipebuddy.enums.Unit;
import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.repositories.IngredientRepository;
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
    IngredientRepository ingredientRepository;

    @Test
    void createRecipe() {

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
        List<Recipe> recipeList =  recipeService.readRecipeByName("Pasta Recipe");

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
}