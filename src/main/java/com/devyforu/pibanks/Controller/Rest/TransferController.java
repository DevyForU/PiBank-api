package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Transfer;
import com.devyforu.pibanks.Service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/transfer")
public class TransferController {
    private TransferService service;

    @GetMapping
    public ResponseEntity<List<Transfer>> findAll() {
        List<Transfer> transfer = service.findAll();
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transfer> save(@RequestBody Transfer toSave) {
        Transfer savedAccount = service.save(toSave);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getById(@PathVariable String id) {
        Transfer transfer = service.getById(id);
        if (transfer != null) {
            return new ResponseEntity<>(transfer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
