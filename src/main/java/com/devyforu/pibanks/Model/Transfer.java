package com.devyforu.pibanks.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@Data
public class Transfer {
    private String id;
    private String transferReason;
    private double amount;
    private Instant effectiveDate;
    private Instant registrationDate;
    private boolean isCanceled;

    public Transfer(double amount) {
        this.amount = amount;
    }
}
