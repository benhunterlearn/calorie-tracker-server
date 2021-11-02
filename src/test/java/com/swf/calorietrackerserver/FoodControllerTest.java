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
                .setCalories(300))
                .setCarbs(5)
                .setFat(4)
                .setProtein(3);
        this.lunch = this.foodRepository.save(new Food()
                .setName("lunch")
                .setCalories(500))
                .setCarbs(8)
                .setFat(7)
                .setProtein(6);
    }

    @Test
    public void getAllFood() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/food")
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())

                // Check this.breakfast.
                .andExpect(jsonPath("$[0].id", is(this.breakfast.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(this.breakfast.getName())))
                .andExpect(jsonPath("$[0].calories", is(this.breakfast.getCalories())))
                .andExpect(jsonPath("$[0].carbs", is(this.breakfast.getCarbs())))
                .andExpect(jsonPath("$[0].fat", is(this.breakfast.getFat())))
                .andExpect(jsonPath("$[0].protein", is(this.breakfast.getProtein())))

                // Check this.lunch.
                .andExpect(jsonPath("$[1].id", is(this.lunch.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(this.lunch.getName())))
                .andExpect(jsonPath("$[1].calories", is(this.lunch.getCalories())))
                .andExpect(jsonPath("$[1].carbs", is(this.lunch.getCarbs())))
                .andExpect(jsonPath("$[1].fat", is(this.lunch.getFat())))
                .andExpect(jsonPath("$[1].protein", is(this.lunch.getProtein())));
    }

    @Test
    public void getFoodById() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/food/" + this.breakfast.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.breakfast.getId().intValue())))
                .andExpect(jsonPath("$.name", is(this.breakfast.getName())))
                .andExpect(jsonPath("$.calories", is(this.breakfast.getCalories())))
                .andExpect(jsonPath("$.carbs", is(this.breakfast.getCarbs())))
                .andExpect(jsonPath("$.fat", is(this.breakfast.getFat())))
                .andExpect(jsonPath("$.protein", is(this.breakfast.getProtein())));
    }

    @Test
    public void postCreateFoodWithValidData() throws Exception {
        Food dinner = new Food()
                .setName("dinner")
                .setCalories(1000000)
                .setCarbs(34)
                .setFat(43)
                .setProtein(78);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dinner))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is(dinner.getName())))
                .andExpect(jsonPath("$.calories", is(dinner.getCalories())))
                .andExpect(jsonPath("$.carbs", is(dinner.getCarbs())))
                .andExpect(jsonPath("$.fat", is(dinner.getFat())))
                .andExpect(jsonPath("$.protein", is(dinner.getProtein())));
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
                .andExpect(jsonPath("$.calories", is(this.breakfast.getCalories())))
                .andExpect(jsonPath("$.carbs", is(this.breakfast.getCarbs())))
                .andExpect(jsonPath("$.fat", is(this.breakfast.getFat())))
                .andExpect(jsonPath("$.protein", is(this.breakfast.getProtein())));
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
                .andExpect(jsonPath("$.calories", is(breakfastWithNewCalories.getCalories())))
                .andExpect(jsonPath("$.carbs", is(this.breakfast.getCarbs())))
                .andExpect(jsonPath("$.fat", is(this.breakfast.getFat())))
                .andExpect(jsonPath("$.protein", is(this.breakfast.getProtein())));
    }

    @Test
    public void deleteValidFoodRemovesFromRepository() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/food/" + this.breakfast.getId());

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk());
        assertTrue(this.foodRepository.findById(this.breakfast.getId()).isEmpty());
    }
}