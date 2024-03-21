package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private AccountService service;

    @PostMapping("/withdrawal")
    public ResponseEntity<String> makeWithDrawal(@RequestParam String id,@RequestParam double amount){
        try {
            service.makeWithdrawal(id,amount);
            return ResponseEntity.ok("Withdrawal successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
