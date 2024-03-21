package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.Transfer;
import com.devyforu.pibanks.Service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/tranfer")
public class TransferController {
    private TransferService transferService;

    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferService.findAll();
    }

    @PostMapping
    public Transfer save(@RequestBody Transfer toSave){
        return transferService.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id){
        transferService.deleteById(id);
    }

    @PutMapping("/{id}/{transferReason}")
    public Transfer updateTransferReasonById(@PathVariable("id") String id, @PathVariable("transferReason") String transferReason){
        return transferService.updateTransferReasonById(id, transferReason);
    }

    @GetMapping("/{id}")
    public Transfer getById(@PathVariable("id") String id){
        return transferService.getById(id);
    }
}
