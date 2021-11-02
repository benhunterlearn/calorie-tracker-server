package com.swf.calorietrackerserver;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/food")
@CrossOrigin("http://localhost:3000")
public class FoodController {

    private FoodRepository foodRepository;

    FoodController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @GetMapping("")
    public Iterable<Food> getAllFood() {
        return this.foodRepository.findAll();
    }

    @GetMapping("{id}")
    public Food getFoodById(@PathVariable Long id) {
        return this.foodRepository.findById(id).get();
    }

    @PostMapping("")
    public Food postCreateFood(@RequestBody Food newFood) {
        return this.foodRepository.save(newFood);
    }

    @PatchMapping("{id}")
    public Food patchUpdateFood(@PathVariable Long id, @RequestBody Food updatedFood) {
        Food currentFood = this.foodRepository.findById(id).get();
        currentFood.patch(updatedFood);
        currentFood = this.foodRepository.save(currentFood);
        return currentFood;
    }

    @DeleteMapping("{id}")
    public String deleteFoodFromRepository(@PathVariable Long id) {
        this.foodRepository.deleteById(id);
        return "SUCCESS";
    }
}
