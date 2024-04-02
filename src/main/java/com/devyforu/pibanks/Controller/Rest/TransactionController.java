package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Transaction;
import com.devyforu.pibanks.Service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private TransactionService service;

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll() {
        List<Transaction> transaction = service.findAll();
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> save(@RequestBody Transaction toSave) {
        Transaction savedAccount = service.save(toSave);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable String id) {
        Transaction transaction = service.getById(id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
