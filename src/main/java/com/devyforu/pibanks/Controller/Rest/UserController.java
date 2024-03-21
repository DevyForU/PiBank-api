package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
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

    @DeleteMapping
    public User delete(@RequestBody User toDelete){
        return userService.delete(toDelete);
    }

    @PutMapping
    public User update(@RequestBody User toUpdate){
        return userService.update(toUpdate);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable String id){
        return userService.getById(id);
    }
}
