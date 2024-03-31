package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHistory {
    private String id;
    private double mainBalance;
    private double loans;
    private double loansInterest;
    private Instant date;
    private Account account;

    public BalanceHistory(String id) {
        this.id = id;
    }


}
