package com.devyforu.pibanks.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Bank {
    private String id;
    private String name;
    private String reference;

    public Bank(String id) {
        this.id = id;
    }

    public Bank(String id, String name, String reference) {
        this.id = id;
        this.name = name;
        this.reference = reference;
    }
}
