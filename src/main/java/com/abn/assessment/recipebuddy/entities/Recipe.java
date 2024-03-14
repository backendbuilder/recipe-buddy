package com.abn.assessment.recipebuddy.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String instructions;
    private int servings;
    private int preparationTime;
    private boolean isVegetarian;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;

}
