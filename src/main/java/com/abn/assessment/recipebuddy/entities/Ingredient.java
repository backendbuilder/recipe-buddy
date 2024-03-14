package com.abn.assessment.recipebuddy.entities;

import com.abn.assessment.recipebuddy.enums.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String name;

    @Schema(description = "The weight or size unit of the ingredient",
            implementation = Unit.class)
    @Enumerated(EnumType.STRING)
    private Unit Unit;

    private int quantity;

}
