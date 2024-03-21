package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Service.UserService;
import jakarta.websocket.server.PathParam;
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
    public User toDelete(@PathParam("id") String id){
        return userService.toDelete(id);
    }
    @PutMapping
    public User update(@RequestBody User toUpdate){
        return userService.update(toUpdate);
    }

    @GetMapping("/{id}")
    public User getById(@PathParam("id") String id){
        return userService.getById(id);
    }
}
