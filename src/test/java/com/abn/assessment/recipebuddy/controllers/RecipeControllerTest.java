package com.abn.assessment.recipebuddy.controllers;

import com.abn.assessment.recipebuddy.entities.Recipe;
import com.abn.assessment.recipebuddy.services.RecipeService;
import com.abn.assessment.recipebuddy.testHelper.TestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    private static Recipe savedRecipe;

    @BeforeAll
    static void setup() {
        savedRecipe = TestHelper.getCompletelySavedRecipe();
    }

    @Test
    void createRecipe_WithValidInput_AndExpectStatusCreated() throws Exception {
        //prepare
        when(recipeService.createRecipe(any(Recipe.class))).thenReturn(savedRecipe);

        // perform and validate
        mockMvc.perform(
                post("/recipes")
                        .content("""
                                {
                                "name": "Pasta Recipe",
                                "instructions": "Step 1: do this. Step 2: do that",
                                "servings": 4,
                                "preparationTime": 30,
                                "vegetarian": true,
                                "ingredients": [{
                                    "name": "cheese",
                                    "unit": "GRAM",
                                    "quantity": 80
                                }, {
                                    "name": "spaghetti",
                                    "unit": "CUP",
                                    "quantity": 2
                                }]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        
                        {
                            "id": "ID-1",
                            "name": "Pasta Recipe",
                            "instructions": "Step 1: do this. Step 2: do that",
                            "servings": 4,
                            "preparationTime": 30,
                            "vegetarian": true,
                            "ingredients": [{
                                "id": "ID-2",
                                "name": "cheese",
                                "unit": "GRAM",
                                "quantity": 80
                            }, {
                                "id": "ID-3",
                                "name": "spaghetti",
                                "unit": "CUP",
                                "quantity": 2
                            }]
                        }
                        """));
    }

    @Test
    void createRecipe_WithInvalidInput_AndExpectStatusBadRequest() throws Exception {
        //prepare
        when(recipeService.createRecipe(any(Recipe.class))).thenReturn(savedRecipe);

        // perform and validate
        mockMvc.perform(
                        post("/recipes")
                                .content("""
                                {
                                "name": "Pasta Recipe",
                                "instructions": "Step 1: do this. Step 2: do that",
                                "servings": 4,
                                "preparationTime": "WRONG DATATYPE",
                                "vegetarian": true,
                                "ingredients": [{
                                    "name": "cheese",
                                    "unit": "GRAM",
                                    "quantity": 80
                                }, {
                                    "name": "spaghetti",
                                    "unit": "CUP",
                                    "quantity": 2
                                }]
                                }
                                """)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRecipe_WithValidInput_AndExpectStatusStatusOk() throws Exception {
        //prepare
        when(recipeService.updateRecipe(any(Recipe.class))).thenReturn(savedRecipe);

        // perform and validate
        mockMvc.perform(
                        put("/recipes")
                                .content("""
                                {
                                "name": "Pasta Recipe",
                                "instructions": "Step 1: do this. Step 2: do that",
                                "servings": 4,
                                "preparationTime": 30,
                                "vegetarian": true,
                                "ingredients": [{
                                    "name": "cheese",
                                    "unit": "GRAM",
                                    "quantity": 80
                                }, {
                                    "name": "spaghetti",
                                    "unit": "CUP",
                                    "quantity": 2
                                }]
                                }
                                """)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        
                        {
                            "id": "ID-1",
                            "name": "Pasta Recipe",
                            "instructions": "Step 1: do this. Step 2: do that",
                            "servings": 4,
                            "preparationTime": 30,
                            "vegetarian": true,
                            "ingredients": [{
                                "id": "ID-2",
                                "name": "cheese",
                                "unit": "GRAM",
                                "quantity": 80
                            }, {
                                "id": "ID-3",
                                "name": "spaghetti",
                                "unit": "CUP",
                                "quantity": 2
                            }]
                        }
                        """));
    }

    @Test
    void updateRecipe_WithInvalidInput_AndExpectStatusStatusBadRequest() throws Exception {
        //prepare
        when(recipeService.updateRecipe(any(Recipe.class))).thenReturn(savedRecipe);

        // perform and validate
        mockMvc.perform(
                        put("/recipes")
                                .content("""
                                {
                                "name": "Pasta Recipe",
                                "instructions": "Step 1: do this. Step 2: do that",
                                "servings": 4,
                                "preparationTime": "WRONG DATATYPE",
                                "vegetarian": true,
                                "ingredients": [{
                                    "name": "cheese",
                                    "unit": "GRAM",
                                    "quantity": 80
                                }, {
                                    "name": "spaghetti",
                                    "unit": "CUP",
                                    "quantity": 2
                                }]
                                }
                                """)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipe_WithValidParams_AndExpectValidResponse() throws Exception {
        //prepare
        when(recipeService.readRecipes(anyString(), anyBoolean(), anyInt(), anyString(), anyList(), anyList())).thenReturn(List.of(savedRecipe));

        // perform and validate
        mockMvc.perform(
                        get("/recipes")
                                .param("name", "Pasta Recipe")
                                .param("isVegetarian", "true")
                                .param("servings", "4")
                                .param("instructionsContain", "Boil"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        
                        [{
                            "id": "ID-1",
                            "name": "Pasta Recipe",
                            "instructions": "Step 1: do this. Step 2: do that",
                            "servings": 4,
                            "preparationTime": 30,
                            "vegetarian": true,
                            "ingredients": [{
                                "id": "ID-2",
                                "name": "cheese",
                                "unit": "GRAM",
                                "quantity": 80
                            }, {
                                "id": "ID-3",
                                "name": "spaghetti",
                                "unit": "CUP",
                                "quantity": 2
                            }]
                        }]
                        """));
    }
    @Test
    void getRecipe_WithNoParams_AndExpectValidResponse() throws Exception {
        //prepare
        when(recipeService.readRecipes(null, false, 0, null, Collections.emptyList(), Collections.emptyList())).thenReturn(List.of(savedRecipe));

        // perform and validate
        mockMvc.perform(
                        get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        
                        [{
                            "id": "ID-1",
                            "name": "Pasta Recipe",
                            "instructions": "Step 1: do this. Step 2: do that",
                            "servings": 4,
                            "preparationTime": 30,
                            "vegetarian": true,
                            "ingredients": [{
                                "id": "ID-2",
                                "name": "cheese",
                                "unit": "GRAM",
                                "quantity": 80
                            }, {
                                "id": "ID-3",
                                "name": "spaghetti",
                                "unit": "CUP",
                                "quantity": 2
                            }]
                        }]
                        """));
    }

    @Test
    void getRecipe_WithWrongParam_AndExpectBadRequest() throws Exception {
        //prepare
        when(recipeService.readRecipes(null, false, 0, null, Collections.emptyList(), Collections.emptyList())).thenReturn(List.of(savedRecipe));

        // perform and validate
        mockMvc.perform(
                        get("/recipes")

                                .param("servings", "wrong datatype"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteRecipe_WithValidId_AndExpectStatusOk() throws Exception {
        // perform and validate
        mockMvc.perform(
                        delete("/recipes")
                                .param("id", "ID"))
                .andExpect(status().isNoContent());
    }
}