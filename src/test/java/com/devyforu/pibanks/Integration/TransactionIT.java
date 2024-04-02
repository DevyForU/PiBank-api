package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.TransactionController;
import com.devyforu.pibanks.Model.*;
import com.devyforu.pibanks.Service.TransactionService;
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
@WebMvcTest(TransactionController.class)
public class TransactionIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private List<Transaction> transactions;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));
        Transfer transfer1 = new Transfer("1", "Ref1", "Reason1", 100.0, "Label1", Instant.now(), Instant.now(), false, account1, account2);
        Transfer transfer2 = new Transfer("2", "Ref2", "Reason2", 200.0, "Label2", Instant.now(), Instant.now(), false, account1, account2);

        transactions = Arrays.asList(
                new Transaction("1", transfer1, account1, new Category("1", "Category1", CategoryType.INCOME), TransactionType.CREDIT, Instant.now()),
                new Transaction("2", transfer2, account2, new Category("2", "Category2",CategoryType.OUTCOME), TransactionType.DEBIT, Instant.now())
        );

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAllTransactions() throws Exception {
        when(transactionService.findAll()).thenReturn(transactions);

        mockMvc.perform(get("/transaction")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].transfer.id").value("1"))
                .andExpect(jsonPath("$[0].account.id").value("1"))
                .andExpect(jsonPath("$[0].category.id").value("1"))
                .andExpect(jsonPath("$[0].type").value("CREDIT"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].transfer.id").value("2"))
                .andExpect(jsonPath("$[1].account.id").value("2"))
                .andExpect(jsonPath("$[1].category.id").value("2"))
                .andExpect(jsonPath("$[1].type").value("DEBIT"));
    }

    @Test
    public void testSaveTransaction() throws Exception {
        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));
        Transfer transfer = new Transfer("1", "Ref1", "Reason1", 100.0, "Label1", Instant.now(), Instant.now(), false, account1, account2);
        Transaction transaction = new Transaction("1", transfer, account1, new Category("1", "Category1", CategoryType.INCOME), TransactionType.CREDIT, Instant.now());

        when(transactionService.save(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.transfer.id").value("1"))
                .andExpect(jsonPath("$.account.id").value("1"))
                .andExpect(jsonPath("$.category.id").value("1"))
                .andExpect(jsonPath("$.type").value("CREDIT"));
    }

    @Test
    public void testDeleteTransactionById() throws Exception {
        String id = "1";

        doNothing().when(transactionService).deleteById(id);

        mockMvc.perform(delete("/transaction/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetTransactionById() throws Exception {
        String id = "1";
        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));
        Transfer transfer = new Transfer("1", "Ref1", "Reason1", 100.0, "Label1", Instant.now(), Instant.now(), false, account1, account2);
        Transaction transaction = new Transaction("1", transfer, account1, new Category("1", "Category1", CategoryType.OUTCOME), TransactionType.CREDIT, Instant.now());

        when(transactionService.getById(id)).thenReturn(transaction);

        mockMvc.perform(get("/transaction/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.transfer.id").value("1"))
                .andExpect(jsonPath("$.account.id").value("1"))
                .andExpect(jsonPath("$.category.id").value("1"))
                .andExpect(jsonPath("$.type").value("CREDIT"));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(transactionService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/transaction/999"))
                .andExpect(status().isNotFound());
    }

}
