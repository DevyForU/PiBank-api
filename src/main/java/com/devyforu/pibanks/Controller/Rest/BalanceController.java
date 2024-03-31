package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.BalanceHistory;
import com.devyforu.pibanks.Service.BalanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
@AllArgsConstructor
public class BalanceController {
    private BalanceService service;

    @GetMapping
    public ResponseEntity<List<BalanceHistory>> findAll() {
        List<BalanceHistory> balanceHistory = service.findAll();
        return new ResponseEntity<>(balanceHistory, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BalanceHistory> save(@RequestBody BalanceHistory toSave) {
        BalanceHistory savedAccount = service.save(toSave);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BalanceHistory> getById(@PathVariable String id) {
        BalanceHistory balanceHistory = service.getById(id);
        if (balanceHistory != null) {
            return new ResponseEntity<>(balanceHistory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
