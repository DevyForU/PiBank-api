package com.devyforu.pibanks.Controller.Rest;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bank")
public class BankController {
    private BankService service;

    @GetMapping
    public List<Bank> findAll(){
        return service.findAll();
    }
    @PostMapping
    public Bank save(@RequestBody Bank toSave){
        return service.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Bank getById(@PathVariable String id){
        return service.getById(id);
    }

}
