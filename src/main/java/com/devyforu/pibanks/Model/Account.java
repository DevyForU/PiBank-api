package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String id;
    private User user;
    private double creditAllow;
    private double overDraftLimit;
}
