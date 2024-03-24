package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Transaction;
import com.devyforu.pibanks.Service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private TransactionService service;

    @GetMapping
    public List<Transaction> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Transaction save(@RequestBody Transaction toSave) {
        return service.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable String id) {
        return service.getById(id);
    }
}
