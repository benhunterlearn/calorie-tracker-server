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

    public void patch(Food updatedFood) {
        if (updatedFood.getName() != null) {
            this.name = updatedFood.getName();
        }
        if (updatedFood.getCalories() != null) {
            this.calories = updatedFood.getCalories();
        }
    }
}
