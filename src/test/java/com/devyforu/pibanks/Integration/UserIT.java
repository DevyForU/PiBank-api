package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.UserController;
import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        // Set default timezone to UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        // Your existing setup code
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        users = Arrays.asList(
                new User("1", "John", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 3000.0),
                new User("2", "Jane", "Doe", Timestamp.valueOf("1995-05-15 00:00:00"), 3500.0)
        );
    }

    @Test
    public void testFindAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthdayDate").value("2000-01-01T00:00:00.000+00:00"))
                .andExpect(jsonPath("$[0].netMonthSalary").value(3000.0))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].birthdayDate").value("1995-05-15T00:00:00.000+00:00"))
                .andExpect(jsonPath("$[1].netMonthSalary").value(3500.0));
    }

    @Test
    public void testSaveUser() throws Exception {
        User newUser = new User("3", "Alice", "Smith", Timestamp.valueOf("2005-06-20 00:00:00"), 4000.0);
        when(userService.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.birthdayDate").value("2005-06-20T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.netMonthSalary").value(4000.0));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(anyString());

        mockMvc.perform(delete("/user/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetById() throws Exception {
        User user = new User("1", "John", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 3000.0);
        when(userService.getById(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthdayDate").value("2000-01-01T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.netMonthSalary").value(3000.0));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(userService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNetMonthSalaryByName() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        double newNetMonthSalary = 4500.0;

        doNothing().when(userService).updateNetMonthSalaryByName(firstName, lastName, newNetMonthSalary);

        mockMvc.perform(put("/user")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("netMonthSalary", String.valueOf(newNetMonthSalary))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Salary successfully updated"));

        verify(userService).updateNetMonthSalaryByName(firstName, lastName, newNetMonthSalary);
    }

}
