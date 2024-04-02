package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.TransferController;
import com.devyforu.pibanks.Model.*;
import com.devyforu.pibanks.Service.CategoryTotalAmountsService;
import com.devyforu.pibanks.Service.FinancialSummaryService;
import com.devyforu.pibanks.Service.TransferService;
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

import java.sql.Date;
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
@WebMvcTest(TransferController.class)
public class TransferIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @MockBean
    private CategoryTotalAmountsService categoryService;

    @MockBean
    private FinancialSummaryService financialService;


    private List<Transfer> transfers;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));

        transfers = Arrays.asList(
                new Transfer("1", "Ref1", "Reason1", 100.0, "Label1", Instant.now(), Instant.now(), false, account1, account2),
                new Transfer("2", "Ref2", "Reason2", 200.0, "Label2", Instant.now(), Instant.now(), false, account1, account2)
        );

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAllTransfers() throws Exception {
        when(transferService.findAll()).thenReturn(transfers);

        mockMvc.perform(get("/transfer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].reference").value("Ref1"))
                .andExpect(jsonPath("$[0].transferReason").value("Reason1"))
                .andExpect(jsonPath("$[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].label").value("Label1"))
                .andExpect(jsonPath("$[0].effectiveDate").exists())
                .andExpect(jsonPath("$[0].registrationDate").exists())
                .andExpect(jsonPath("$[0].accountSender.id").value("1"))
                .andExpect(jsonPath("$[0].accountReceiver.id").value("2"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].reference").value("Ref2"))
                .andExpect(jsonPath("$[1].transferReason").value("Reason2"))
                .andExpect(jsonPath("$[1].amount").value(200.0))
                .andExpect(jsonPath("$[1].label").value("Label2"))
                .andExpect(jsonPath("$[1].effectiveDate").exists())
                .andExpect(jsonPath("$[1].registrationDate").exists())
                .andExpect(jsonPath("$[1].accountSender.id").value("1"))
                .andExpect(jsonPath("$[1].accountReceiver.id").value("2"));
    }

    @Test
    public void testSaveTransfer() throws Exception {
        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));
        Transfer transfer = new Transfer("1", "Ref1", "Reason1", 100.0, "Label1", Instant.now(), Instant.now(), false, account1, account2);
        when(transferService.save(any(Transfer.class))).thenReturn(transfer);

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.reference").value("Ref1"))
                .andExpect(jsonPath("$.transferReason").value("Reason1"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.label").value("Label1"))
                .andExpect(jsonPath("$.effectiveDate").exists())
                .andExpect(jsonPath("$.registrationDate").exists())
                .andExpect(jsonPath("$.accountSender.id").value("1"))
                .andExpect(jsonPath("$.accountReceiver.id").value("2"));
    }

    @Test
    public void testDeleteTransferById() throws Exception {
        doNothing().when(transferService).deleteById(anyString());

        mockMvc.perform(delete("/transfer/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetTransferById() throws Exception {
        Account account1 = new Account("1", "123456789", 1000.0, 500.0, 10.0, 20.0, false, 0.01, 0.02, new User("1", "John", "Doe", Timestamp.from(Instant.now()), 3000.0), new Bank("1", "Bank1", "Ref1"));
        Account account2 = new Account("2", "987654321", 2000.0, 600.0, 15.0, 30.0, true, 0.02, 0.03, new User("2", "Jane", "Doe", Timestamp.from(Instant.now()), 3500.0), new Bank("2", "Bank2", "Ref2"));

        Transfer transfer = new Transfer("1", "Ref1", "Reason1", 100.0, "Label1", Instant.now(), Instant.now(), false, account1, account2);
        when(transferService.getById(anyString())).thenReturn(transfer);

        mockMvc.perform(get("/transfer/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.reference").value("Ref1"))
                .andExpect(jsonPath("$.transferReason").value("Reason1"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.label").value("Label1"))
                .andExpect(jsonPath("$.effectiveDate").exists())
                .andExpect(jsonPath("$.registrationDate").exists())
                .andExpect(jsonPath("$.accountSender.id").value("1"))
                .andExpect(jsonPath("$.accountReceiver.id").value("2"));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(transferService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/transfer/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAmountCategoryBetweenDates() throws Exception {
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-01-31");
        CategoryTotalAmounts categoryTotalAmounts = new CategoryTotalAmounts("Food", 500.0);
        when(categoryService.getAmountCategoryBetweenDates(startDate, endDate)).thenReturn(categoryTotalAmounts);

        mockMvc.perform(get("/transfer/amounts/category")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category_name").value("Food"))
                .andExpect(jsonPath("$.total_amount").value(500.0));
    }
    @Test
    public void testGetFinancialSummaryBetweenDates() throws Exception {
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-01-31");
        FinancialSummary financialSummary = new FinancialSummary(2024, 1, 1000.0, 2000.0);
        when(financialService.getFinancialSummaryBetweenDates(startDate, endDate)).thenReturn(financialSummary);

        mockMvc.perform(get("/transfer/financial/summary")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.month").value(1))
                .andExpect(jsonPath("$.total_expenses").value(1000.0))
                .andExpect(jsonPath("$.total_income").value(2000.0));
    }


}
