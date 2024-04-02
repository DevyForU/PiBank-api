package com.devyforu.pibanks.Controller.Rest;

import com.devyforu.pibanks.Model.User;
import com.devyforu.pibanks.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> user = service.findAll();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User toSave) {
        User savedAccount = service.save(toSave);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        User user = service.getById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping()
    public ResponseEntity<String> updateNetMonthSalaryByName(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam double netMonthSalary) {

        try {
            service.updateNetMonthSalaryByName(firstName, lastName, netMonthSalary);
            return ResponseEntity.ok("Salary successfully updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update salary");
        }
    }
}
