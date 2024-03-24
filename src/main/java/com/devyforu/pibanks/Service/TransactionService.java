package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Transaction;
import com.devyforu.pibanks.Repository.TransactionDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private TransactionDAO transaction;

    public List<Transaction> findAll() {
        return transaction.findAll();
    }

    public Transaction save(Transaction toSave) {
        return transaction.save(toSave);
    }

    public void deleteById(String id) {
        transaction.deleteById(id);
    }

    public Transaction getById(String id) {
        return transaction.getById(id);
    }
}
