package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.BankController;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
@WebMvcTest(BankController.class)
public class BankIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    private List<Bank> banks;

    @BeforeEach
    public void setUp() {
        banks = Arrays.asList(
                new Bank("1", "Bank1", "Ref1"),
                new Bank("2", "Bank2", "Ref2")
        );
    }

    @Test
    public void testFindAllBanks() throws Exception {
        when(bankService.findAll()).thenReturn(banks);

        mockMvc.perform(get("/bank")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Bank1"))
                .andExpect(jsonPath("$[0].reference").value("Ref1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Bank2"))
                .andExpect(jsonPath("$[1].reference").value("Ref2"));
    }

    @Test
    public void testSaveBank() throws Exception {
        Bank newBank = new Bank("3", "Bank3", "Ref3");
        when(bankService.save(any(Bank.class))).thenReturn(newBank);

        mockMvc.perform(post("/bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newBank)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Bank3"))
                .andExpect(jsonPath("$.reference").value("Ref3"));
    }

    @Test
    public void testDeleteBank() throws Exception {
        doNothing().when(bankService).deleteById(anyString());

        mockMvc.perform(delete("/bank/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetById() throws Exception {
        Bank bank = new Bank("1", "Bank1", "Ref1");
        when(bankService.getById(anyString())).thenReturn(bank);

        mockMvc.perform(get("/bank/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Bank1"))
                .andExpect(jsonPath("$.reference").value("Ref1"));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(bankService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/bank/999"))
                .andExpect(status().isNotFound());
    }
}
