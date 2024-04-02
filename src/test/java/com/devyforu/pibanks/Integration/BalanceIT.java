package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.BalanceController;
import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.BalanceHistory;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Service.BalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.sql.Timestamp;
import java.time.Instant;
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
@WebMvcTest(BalanceController.class)
public class BalanceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceService balanceService;

    private List<BalanceHistory> balanceHistories;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));

        balanceHistories = Arrays.asList(
                new BalanceHistory("1", 1000.0, 500.0, 10.0, Instant.now(), account1),
                new BalanceHistory("2", 2000.0, 600.0, 15.0, Instant.now(), account2)
        );

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testFindAllBalances() throws Exception {
        when(balanceService.findAll()).thenReturn(balanceHistories);

        mockMvc.perform(get("/balance")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].mainBalance").value(1000.0))
                .andExpect(jsonPath("$[0].loans").value(500.0))
                .andExpect(jsonPath("$[0].loansInterest").value(10.0))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].mainBalance").value(2000.0))
                .andExpect(jsonPath("$[1].loans").value(600.0))
                .andExpect(jsonPath("$[1].loansInterest").value(15.0));
    }

    @Test
    public void testSaveBalance() throws Exception {
        Account account = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        BalanceHistory balanceHistory = new BalanceHistory("1", 1000.0, 500.0, 10.0, Instant.now(), account);
        when(balanceService.save(any(BalanceHistory.class))).thenReturn(balanceHistory);

        mockMvc.perform(post("/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(balanceHistory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.mainBalance").value(1000.0))
                .andExpect(jsonPath("$.loans").value(500.0))
                .andExpect(jsonPath("$.loansInterest").value(10.0));
    }

    @Test
    public void testDeleteBalanceById() throws Exception {
        doNothing().when(balanceService).deleteById(anyString());

        mockMvc.perform(delete("/balance/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetBalanceById() throws Exception {
        Account account = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        BalanceHistory balanceHistory = new BalanceHistory("1", 1000.0, 500.0, 10.0, Instant.now(), account);
        when(balanceService.getById(anyString())).thenReturn(balanceHistory);

        mockMvc.perform(get("/balance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.mainBalance").value(1000.0))
                .andExpect(jsonPath("$.loans").value(500.0))
                .andExpect(jsonPath("$.loansInterest").value(10.0));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(balanceService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/balance/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAccountBalanceHistoryByAccountNumber() throws Exception {
        Account account = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        BalanceHistory balanceHistory = new BalanceHistory("1", 1000.0, 500.0, 10.0, Instant.now(), account);
        when(balanceService.getAccountBalanceHistoryByAccountNumber(anyString())).thenReturn(balanceHistory);

        mockMvc.perform(get("/balance/history/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.mainBalance").value(1000.0))
                .andExpect(jsonPath("$.loans").value(500.0))
                .andExpect(jsonPath("$.loansInterest").value(10.0));
    }

}
