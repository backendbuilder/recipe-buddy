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
    String id;
    String name;
    String instructions;
    int servings;
    int preparationTime;
    boolean isVegetarian;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Ingredient> ingredients;

}
