package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.CategoryController;
import com.devyforu.pibanks.Model.Category;
import com.devyforu.pibanks.Model.CategoryType;
import com.devyforu.pibanks.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private List<Category> categories;

    @BeforeEach
    public void setUp() {
        categories = Arrays.asList(
                new Category("1", "Food", CategoryType.OUTCOME),
                new Category("2", "Transport", CategoryType.INCOME)
        );

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAllCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/category")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Food"))
                .andExpect(jsonPath("$[0].type").value("OUTCOME"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Transport"))
                .andExpect(jsonPath("$[1].type").value("INCOME"));
    }

    @Test
    public void testSaveCategory() throws Exception {
        Category newCategory = new Category("3", "Entertainment", CategoryType.OUTCOME);
        when(categoryService.save(any(Category.class))).thenReturn(newCategory);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"3\",\"name\":\"Entertainment\",\"type\":\"OUTCOME\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Entertainment"))
                .andExpect(jsonPath("$.type").value("OUTCOME"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteById(anyString());

        mockMvc.perform(delete("/category/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetById() throws Exception {
        Category category = new Category("1", "Food", CategoryType.OUTCOME);
        when(categoryService.getById(anyString())).thenReturn(category);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Food"))
                .andExpect(jsonPath("$.type").value("OUTCOME"));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(categoryService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/category/999"))
                .andExpect(status().isNotFound());
    }
}
