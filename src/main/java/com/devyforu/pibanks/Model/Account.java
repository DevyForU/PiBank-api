package com.devyforu.pibanks.Model;

import lombok.*;

@NoArgsConstructor
@Data
public class Account {
    private String id;
    private String accountNumber;
    private double mainBalance;
    private double loans;
    private double interestLoans;
    private double creditAllow;
    private boolean overDraftLimit;
    private double interestRateBefore7Days;
    private double interestRateAfter7Days;
    private User user;
    private Bank bank;


    public Account(String id, User user, double creditAllow, boolean overDraftLimit) {
        this.id = id;
        this.user = user;
        this.creditAllow = creditAllow;
        this.overDraftLimit = overDraftLimit;
    }

    public Account(String id, String accountNumber, double mainBalance, double loans, double interestLoans, double creditAllow, boolean overDraftLimit, double interestRateBefore7Days, double interestRateAfter7Days, User user, Bank bank) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.mainBalance = 0;
        this.loans = 0;
        this.interestLoans = 0;
        this.creditAllow = user.getNetMonthSalary()/3;
        this.overDraftLimit = false;
        this.interestRateBefore7Days = 0.01;
        this.interestRateAfter7Days = 0.02;
        this.user = user;
        this.bank = bank;
    }


    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }




}
