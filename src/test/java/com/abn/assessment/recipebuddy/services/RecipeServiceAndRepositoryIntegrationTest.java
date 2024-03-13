package com.abn.assessment.recipebuddy.services;

import com.abn.assessment.recipebuddy.entities.Ingredient;
import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.entities.RecipeIngredient;
import com.abn.assessment.recipebuddy.repositories.IngredientRepository;
import com.abn.assessment.recipebuddy.repositories.RecipeRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        //create recipe
        //save recipe in service
        //retreive recipe in service
        //verify equals

        Recipe recipe = new Recipe();
        recipe.setName("Pasta Recipe");
        recipe.setInstructions("Step 1: do this \nStep 2: do that");
        recipe.setServings(4);
        recipe.setPreparationTime(30); // in minutes
        recipe.setVegetarian(true);

        Ingredient cheese = new Ingredient();
        cheese.setName("cheese");
        ingredientRepository.save(cheese);
        Ingredient spaghetti = new Ingredient();
        spaghetti.setName("spaghetti");
        ingredientRepository.save(spaghetti);

        RecipeIngredient cheeseForPasta = new RecipeIngredient();
        cheeseForPasta.setIngredient_id(cheese);

        RecipeIngredient spaghettiForPasta = new RecipeIngredient();
        spaghettiForPasta.setIngredient_id(spaghetti);

        Set<RecipeIngredient> pastaIngredients = new HashSet<>();
        pastaIngredients.add(cheeseForPasta);
        pastaIngredients.add(spaghettiForPasta);
        recipe.setRecipeIngredients(pastaIngredients);

        recipeService.createRecipe(recipe);
        List<Recipe> recipeList =  recipeService.readRecipeByName("Pasta Recipe");

        assertEquals(1, recipeList.size());


    }
}