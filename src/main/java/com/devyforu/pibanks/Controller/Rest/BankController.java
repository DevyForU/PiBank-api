package com.devyforu.pibanks.Controller.Rest;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bank")
public class BankController {
    private BankService service;

    @GetMapping
    public ResponseEntity<List<Bank>> findAll() {
        List<Bank> bank = service.findAll();
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Bank> save(@RequestBody Bank toSave) {
        Bank savedAccount = service.save(toSave);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bank> getById(@PathVariable String id) {
        Bank bank = service.getById(id);
        if (bank != null) {
            return new ResponseEntity<>(bank, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
