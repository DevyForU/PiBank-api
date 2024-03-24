package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Balance;
import com.devyforu.pibanks.Service.BalanceService;
import com.devyforu.pibanks.Service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
@AllArgsConstructor
public class BalanceController {
    private BalanceService service;

    @GetMapping
    public List<Balance> findAll(){
        return service.findAll();
    }
    @PostMapping
    public Balance save(@RequestBody Balance toSave){
        return service.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Balance getById(@PathVariable String id){
        return service.getById(id);
    }


}
