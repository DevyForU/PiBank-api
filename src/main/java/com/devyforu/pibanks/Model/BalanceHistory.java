package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHistory {
    private String id;
    private double mainBalance;
    private double loans;
    private double loansInterest;
    private Timestamp date;
    private Account account;

    public BalanceHistory(String id) {
        this.id = id;
    }


}
