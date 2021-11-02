package com.swf.calorietrackerserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Food {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private Integer calories;
    private Integer carbs;
    private Integer fat;
    private Integer protein;

    public void patch(@org.jetbrains.annotations.NotNull Food updatedFood) {
        if (updatedFood.getName() != null) {
            this.name = updatedFood.getName();
        }
        if (updatedFood.getCalories() != null) {
            this.calories = updatedFood.getCalories();
        }
        if (updatedFood.getCarbs() != null) {
            this.carbs = updatedFood.getCarbs();
        }
        if (updatedFood.getFat() != null) {
            this.fat = updatedFood.getFat();
        }
        if (updatedFood.getProtein() != null) {
            this.protein = updatedFood.getProtein();
        }
    }
}
