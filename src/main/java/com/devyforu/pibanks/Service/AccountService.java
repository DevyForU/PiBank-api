package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Repository.AccountDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class AccountService {
    private AccountDAO accountDAO;

    public List<Account> findAll(){
        return accountDAO.findAll();
    }
    public Account save(Account toSave){
        return accountDAO.save(toSave);
    }
    public void deleteById(String id){
        accountDAO.deleteById(id);
    }
    public Account getById(String id){
        return accountDAO.getById(id);
    }
}



