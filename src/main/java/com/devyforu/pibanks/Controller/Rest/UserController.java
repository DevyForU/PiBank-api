package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User save(@RequestBody User toSave){
        return userService.save(toSave);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id){
        userService.deleteById(id);
    }
    @PutMapping("/{id}/{netMonthSalary}")
    public User updateNetMonthSalaryById(@PathVariable("id") String id, @PathVariable("netMonthSalary") double netMonthSalary){
        return userService.updateNetMonthSalaryById(id, netMonthSalary);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") String id){
        return userService.getById(id);
    }
}
