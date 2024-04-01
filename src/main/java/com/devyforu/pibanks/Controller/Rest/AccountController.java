package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Transfer;
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
        Account account = service.getByAccountNumber(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<Double> getBalanceByAccountNumber(@PathVariable String accountNumber) {
        Account account = service.getByAccountNumber(accountNumber);
        double balance = service.getBalanceByAccountNumber(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{accountNumber}/withdrawal")
    public ResponseEntity<String> makeWithdrawal(@PathVariable String accountNumber, @RequestParam(name = "amount") double amount) {
        try {
            double balance = service.getBalanceByAccountNumber(accountNumber);
            if (amount > balance) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds to make the withdrawal.");
            } else {
                service.makeWithdrawal(accountNumber, amount);
                return ResponseEntity.ok("Withdrawal successful.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Withdrawal unsuccessfully.");
        }
    }
    @PostMapping("/credit")
    public ResponseEntity<String> creditAccount(
            @RequestParam(name = "accountNumber") String accountNumber,
            @RequestParam(name = "amount") double amount
    ) {
        try {
            service.creditAnAccount(accountNumber, amount);
            return ResponseEntity.ok("Credit successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while crediting the account.");
        }
    }

    @PostMapping("/performTransfer")
    public ResponseEntity<String> performTransfer(@RequestBody Transfer transfer) {
        try {
            service.performTransfer(
                    transfer.getAccountSender().getAccountNumber(),
                    transfer.getAccountReceiver().getAccountNumber(),
                    transfer.getAmount(),
                    transfer.getTransferReason(),
                    transfer.getEffectiveDate(),
                    transfer.getRegistrationDate());
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transfer failed: " + e.getMessage());
        }
    }
}

