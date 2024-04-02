package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.AccountStatement;
import com.devyforu.pibanks.Repository.AccountStatementDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountStatementService {
    private AccountStatementDAO account;

    public AccountStatement getStatementAccountByAccountNumber(String accountNumber) {
        return account.getStatementAccountByAccountNumber(accountNumber);
    }
}
