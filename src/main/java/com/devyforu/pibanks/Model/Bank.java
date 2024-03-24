package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private String id;
    private String name;
    private String reference;

    public Bank(String id) {
        this.id = id;
    }
}
