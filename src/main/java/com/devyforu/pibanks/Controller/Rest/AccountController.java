package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Service.AccountService;
import lombok.AllArgsConstructor;
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
   public List<Account> findAll(){
        return service.findAll();
    }
    @PostMapping
    public Account save(@RequestBody Account toSave){
        return service.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable String id){
        return service.getById(id);
    }



}

