package com.abn.assessment.recipebuddy.entities;

import com.abn.assessment.recipebuddy.enums.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    String name;

    @Enumerated(EnumType.STRING)
    Unit Unit;

    int quantity;

}
