package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Repository.UserDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private UserDAO userDAO;

    public List<User> findAll(){
        return userDAO.findAll();
    }

    public User save(User toSave){
        return userDAO.save(toSave);
    }

    public void deleteById(String id){
        userDAO.deleteById(id);
    }

    public void updateNetMonthSalaryByName(String firstName,String lastName, double netMonthSalary){
        userDAO.updateNetMonthSalaryByName(firstName,lastName, netMonthSalary);
    }

    public User getById(String id){
        return userDAO.getById(id);
    }


}
