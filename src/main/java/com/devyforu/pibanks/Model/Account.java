package com.devyforu.pibanks.Model;

import lombok.*;

@NoArgsConstructor
@Data
public class Account {
    private String id;
    private User user;
    private Bank bank;
    private double creditAllow;
    private boolean overDraftLimit;

    public Account(String id, User user, double creditAllow, boolean overDraftLimit) {
        this.id = id;
        this.user = user;
        this.creditAllow = creditAllow;
        this.overDraftLimit = overDraftLimit;
    }

    public Account(String id, User user, Bank bank, double creditAllow, boolean overDraftLimit) {
        this.id = id;
        this.user = user;
        this.bank = bank;
        this.creditAllow = creditAllow;
        this.overDraftLimit = overDraftLimit;
    }

    public Account(String id) {
        this.id = id;
    }

    public double getCreditAllow() {
        return user.getNetMonthSalary()/ 3;
    }
    public boolean getOverDraftLimit(){
        return false;
    }
}
