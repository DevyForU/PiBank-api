package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatement {
    private Instant date;
    private String reference;
    private String reason;
    private double credit;
    private double debit;
    private double balance;
}
