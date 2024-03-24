package com.devyforu.pibanks.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private Timestamp birthdayDate;
    private double netMonthSalary;

    public User(String id, String firstName, String lastName, Timestamp birthdayDate, double netMonthSalary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdayDate = birthdayDate;
        this.netMonthSalary = netMonthSalary;
    }


    public User(String id) {
        this.id = id;
    }
}
