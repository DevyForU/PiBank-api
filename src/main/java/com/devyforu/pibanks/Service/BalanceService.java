package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Balance;
import com.devyforu.pibanks.Repository.BalanceDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BalanceService {
    private BalanceDAO balance;

    public List<Balance> findAll(){
        return balance.findAll();
    }
    public Balance save (Balance toSave){
        return balance.save(toSave);
    }
    public void deleteById(String id){
        balance.deleteById(id);
    }
    public Balance getById(String id){
        return balance.getById(id);
    }

}
