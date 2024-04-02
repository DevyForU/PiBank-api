package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Repository.BankDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BankService {
    private BankDAO bank;

    public List<Bank> findAll() {
        return bank.findAll();
    }

    public Bank save(Bank toSave) {
        return bank.save(toSave);
    }

    public void deleteById(String id) {
        bank.deleteById(id);
    }

    public Bank getById(String id) {
        return bank.getById(id);
    }
}
