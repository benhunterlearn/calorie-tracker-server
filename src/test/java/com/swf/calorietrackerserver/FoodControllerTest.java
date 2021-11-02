package com.swf.calorietrackerserver;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FoodControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private FoodRepository foodRepository;

    private Food breakfast;
    private Food lunch;

    private ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @BeforeEach
    void setUp() {
        this.breakfast = this.foodRepository.save(new Food()
                .setName("breakfast")
                .setCalories(300));
        this.lunch = this.foodRepository.save(new Food()
                .setName("lunch")
                .setCalories(500));
    }

    @Test
    public void getAllFood() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/food")
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(this.breakfast.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(this.breakfast.getName())))
                .andExpect(jsonPath("$[0].calories", is(this.breakfast.getCalories())))
                .andExpect(jsonPath("$[1].id", is(this.lunch.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(this.lunch.getName())))
                .andExpect(jsonPath("$[1].calories", is(this.lunch.getCalories())));
    }

    @Test
    public void getFoodById() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/food/" + this.breakfast.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.breakfast.getId().intValue())))
                .andExpect(jsonPath("$.name", is(this.breakfast.getName())))
                .andExpect(jsonPath("$.calories", is(this.breakfast.getCalories())));
    }

    @Test
    public void postCreateFoodWithValidData() throws Exception {
        Food dinner = new Food()
                .setName("dinner")
                .setCalories(1000000);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dinner))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is(dinner.getName())))
                .andExpect(jsonPath("$.calories", is(dinner.getCalories())));
    }

    @Test
    public void patchUpdateFoodWithNewName() throws Exception {
        Food breakfastWithNewName = new Food()
                .setName("oops");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/food/" + this.breakfast.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(breakfastWithNewName))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.breakfast.getId().intValue())))
                .andExpect(jsonPath("$.name", is(breakfastWithNewName.getName())))
                .andExpect(jsonPath("$.calories", is(this.breakfast.getCalories())));
    }

    @Test
    public void patchUpdateFoodWithNewCalories() throws Exception {
        Food breakfastWithNewCalories = new Food()
                .setCalories(7234);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/food/" + this.breakfast.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(breakfastWithNewCalories))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.breakfast.getId().intValue())))
                .andExpect(jsonPath("$.name", is(this.breakfast.getName())))
                .andExpect(jsonPath("$.calories", is(breakfastWithNewCalories.getCalories())));
    }

    @Test
    public void deleteValidFoodRemovesFromRepository() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/food/" + this.breakfast.getId());

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk());
        assertTrue(this.foodRepository.findById(this.breakfast.getId()).isEmpty());
    }
}