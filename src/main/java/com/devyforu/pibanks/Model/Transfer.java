package com.devyforu.pibanks.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {
    private String id;
    private String reference;
    private String transferReason;
    private double amount;
    private String label;
    private Instant effectiveDate;
    private Instant registrationDate;
    private boolean isCanceled;
    private Account accountSender;
    private Account accountReceiver;

    public Transfer(String id, String reference, String transferReason, double amount, Instant effectiveDate, Instant registrationDate, boolean isCanceled, Account senderAccount, Account receiverAccount) {
    }
}
