package com.abn.assessment.recipebuddy.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    Recipe recipe_id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredient_id;

    String Unit;

    int quantity;

}
