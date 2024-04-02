package com.devyforu.pibanks.Integration;

import com.devyforu.pibanks.Controller.Rest.AccountController;
import com.devyforu.pibanks.Model.*;
import com.devyforu.pibanks.Service.AccountService;
import com.devyforu.pibanks.Service.AccountStatementService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountStatementService accountStatementService;

    private List<Account> accounts;

    @BeforeEach
    public void setUp() {
        User user1 = new User("1", "John", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 5000.0);
        User user2 = new User("2", "Jane", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 6000.0);
        Bank bank1 = new Bank("1", "Bank1", "Ref1");
        Bank bank2 = new Bank("2", "Bank2", "Ref2");

        accounts = Arrays.asList(
                new Account("1", "123456", 1000.0, 0.0, 0.0, 500.0, false, 0.01, 0.02, user1, bank1),
                new Account("2", "789012", 2000.0, 0.0, 0.0, 1000.0, false, 0.01, 0.02, user2, bank2)
        );

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAllAccounts() throws Exception {
        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/account")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[1].accountNumber").value("789012"));
    }

    @Test
    public void testSaveAccount() throws Exception {
        User user = new User("3", "Alice", "Smith", Timestamp.valueOf("2005-06-20 00:00:00"), 4000.0);
        Bank bank = new Bank("3", "Bank3", "Ref3");

        Account newAccount = new Account("3", "345678", 500.0, 0.0, 0.0, 250.0, false, 0.01, 0.02, user, bank);
        when(accountService.save(any(Account.class))).thenReturn(newAccount);

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"345678\",\"mainBalance\":500.0,\"loans\":0.0,\"interestLoans\":0.0,\"creditAllow\":250.0,\"overDraftLimit\":false,\"interestRateBefore7Days\":0.01,\"interestRateAfter7Days\":0.02}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("345678"));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteById(anyString());

        mockMvc.perform(delete("/account/123456"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetById() throws Exception {
        User user = new User("1", "John", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 5000.0);
        Bank bank = new Bank("1", "Bank1", "Ref1");

        Account account = new Account("1", "123456", 1000.0, 0.0, 0.0, 500.0, false, 0.01, 0.02, user, bank);
        when(accountService.getByAccountNumber(anyString())).thenReturn(account);

        mockMvc.perform(get("/account/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456"));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(accountService.getByAccountNumber(anyString())).thenReturn(null);

        mockMvc.perform(get("/account/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBalanceByAccountNumber() throws Exception {
        User user = new User("1", "John", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 5000.0);
        Bank bank = new Bank("1", "Bank1", "Ref1");
        Account account = new Account("1", "123456", 1000.0, 0.0, 0.0, 500.0, false, 0.01, 0.02, user, bank);

        when(accountService.getByAccountNumber(anyString())).thenReturn(account);
        when(accountService.getBalanceByAccountNumber("123456")).thenReturn(1000.0);

        mockMvc.perform(get("/account/123456/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1000.0));
    }

    @Test
    public void testMakeWithdrawal() throws Exception {
        when(accountService.getBalanceByAccountNumber(anyString())).thenReturn(1000.0);

        mockMvc.perform(post("/account/123456/withdrawal")
                        .param("amount", "500.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Withdrawal successful."));
    }

    @Test
    public void testCreditAccount() throws Exception {
        doNothing().when(accountService).creditAnAccount(anyString(), anyDouble());

        mockMvc.perform(post("/account/credit")
                        .param("accountNumber", "123456")
                        .param("amount", "500.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Credit successful."));
    }

    @Test
    public void testPerformTransfer() throws Exception {
        User user1 = new User("1", "John", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 5000.0);
        User user2 = new User("2", "Jane", "Doe", Timestamp.valueOf("2000-01-01 00:00:00"), 6000.0);
        Bank bank1 = new Bank("1", "Bank1", "Ref1");
        Bank bank2 = new Bank("2", "Bank2", "Ref2");

        Account accountSender = new Account("123456", "1000.0", 0.0, 0.0, 0.0, 500.0, false, 0.01, 0.02, user1, bank1);
        Account accountReceiver = new Account("789012", "2000.0", 0.0, 0.0, 0.0, 1000.0, false, 0.01, 0.02, user2, bank2);

        when(accountService.getByAccountNumber(anyString())).thenReturn(accountSender, accountReceiver);
        doNothing().when(accountService).performTransfer(any(), any(), anyDouble(), anyString(), any(), any());
        doNothing().when(accountService).performTransfer(any(), any(), anyDouble(), anyString(), any(), any());

        mockMvc.perform(post("/account/performTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountSender\":{\"accountNumber\":\"123456\"},\"accountReceiver\":{\"accountNumber\":\"789012\"},\"amount\":100.0,\"transferReason\":\"Test Transfer\",\"effectiveDate\":\"2024-04-02T10:00:00Z\",\"registrationDate\":\"2024-04-02T10:00:00Z\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }

    @Test
    public void testGetStatementAccountByAccountNumber() throws Exception {
        AccountStatement accountStatement = new AccountStatement(Instant.now(), "Statement1", "Test Statement", 100.0, 50.0, 150.0);
        when(accountStatementService.getStatementAccountByAccountNumber(anyString())).thenReturn(accountStatement);

        mockMvc.perform(get("/account/123456/statement")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value("Statement1"))
                .andExpect(jsonPath("$.reason").value("Test Statement"));
    }


}