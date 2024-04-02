package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.BalanceHistory;
import com.devyforu.pibanks.Repository.BalanceHistoryDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BalanceService {
    private BalanceHistoryDAO balance;

    public List<BalanceHistory> findAll() {
        return balance.findAll();
    }

    public BalanceHistory save(BalanceHistory toSave) {
        return balance.save(toSave);
    }

    public void deleteById(String id) {
        balance.deleteById(id);
    }

    public BalanceHistory getById(String id) {
        return balance.getById(id);
    }

    public BalanceHistory getAccountBalanceHistoryByAccountNumber(String accountNumber) {
        return balance.getAccountBalanceHistoryByAccountNumber(accountNumber);
    }

}
