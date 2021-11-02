package com.swf.calorietrackerserver;

import org.springframework.data.repository.CrudRepository;

public interface FoodRepository extends CrudRepository<Food, Long> {
}
