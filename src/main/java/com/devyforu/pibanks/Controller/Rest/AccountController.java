package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private AccountService service;

    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        List<Account> accounts = service.findAll();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Account> save(@RequestBody Account toSave) {
        Account savedAccount = service.save(toSave);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteById(@PathVariable String accountNumber) {
        service.deleteById(accountNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getById(@PathVariable String accountNumber) {
        Account account = service.getById(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> makeWithdrawal (@PathVariable String accountNumber,@RequestParam double amount){
        try {
            service.makeWithdrawal(accountNumber, amount);
            return ResponseEntity.ok("Withdrawal successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds to make the withdrawal.");
        }
    }
}

