package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.BalanceHistory;
import com.devyforu.pibanks.Service.BalanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
@AllArgsConstructor
public class BalanceController {
    private BalanceService service;

    @GetMapping
    public List<BalanceHistory> findAll(){
        return service.findAll();
    }
    @PostMapping
    public BalanceHistory save(@RequestBody BalanceHistory toSave){
        return service.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public BalanceHistory getById(@PathVariable String id){
        return service.getById(id);
    }


}
