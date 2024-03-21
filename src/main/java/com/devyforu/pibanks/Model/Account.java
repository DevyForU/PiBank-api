package com.devyforu.pibanks.Model;

import lombok.*;

import java.util.List;



@NoArgsConstructor
@Data
public class Account {
    private String id;
    private User user;
    private double mainBalance;
    private double loans;
    private double interestLoans;
    private double creditAllow;
    private boolean overDraftLimit;
    private List<Transaction> transactionList;

    public Account(String id, double mainBalance, double loans, double interestLoans, double creditAllow, boolean overDraftLimit) {
        this.id = id;
        this.mainBalance = mainBalance;
        this.loans = loans;
        this.interestLoans = interestLoans;
        this.creditAllow = creditAllow;
        this.overDraftLimit = overDraftLimit;
    }

    public double getCreditAllow() {
        return user.getNetMonthSalary()/ 3;
    }
    public boolean getOverDraftLimit(){
        return false;
    }

}
