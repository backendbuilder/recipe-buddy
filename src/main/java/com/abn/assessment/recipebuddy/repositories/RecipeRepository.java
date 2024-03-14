package com.abn.assessment.recipebuddy.repositories;

import com.abn.assessment.recipebuddy.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    @Query("""
            SELECT r FROM Recipe r
            WHERE (:name IS NULL OR r.name = :name)
            AND (:servings = 0 OR r.servings = :servings)
            AND (:isVegetarian IS false OR r.isVegetarian = :isVegetarian)
            AND (:contains IS NULL OR r.instructions LIKE %:contains%) """)
    List<Recipe> findWithFilters(@Param ("name") String name,
                                 @Param ("isVegetarian") boolean isVegetarian,
                                 @Param ("servings") int servings,
                                 @Param ("contains") String contains );
}
