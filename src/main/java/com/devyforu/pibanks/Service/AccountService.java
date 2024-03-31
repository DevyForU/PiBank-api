package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.BalanceHistory;
import com.devyforu.pibanks.Model.Transfer;
import com.devyforu.pibanks.Repository.AccountDAO;
import com.devyforu.pibanks.Repository.BalanceHistoryDAO;
import com.devyforu.pibanks.Repository.TransferDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class AccountService {
    private AccountDAO accountDAO;
    private BalanceHistoryDAO balanceHistoryDAO;
    private TransferDAO transferDAO;

    public List<Account> findAll() {
        return accountDAO.findAll();
    }

    public Account save(Account toSave) {
        return accountDAO.save(toSave);
    }

    public void deleteById(String accountNumber) {
        accountDAO.deleteByAccountNumber(accountNumber);
    }

    public Account getById(String accountNumber) {
        return accountDAO.getByAccountNumber(accountNumber);
    }

    public void makeWithdrawal(String accountNumber, double amount) {
        Account account = accountDAO.getByAccountNumber(accountNumber);
        double balance = account.getMainBalance();

        if (!account.isOverDraftLimit()) {
            if (balance < amount && account.getCreditAllow() > 0) {
                int paymentDelayDays = (int) Math.ceil((amount - balance) / account.getInterestLoans());
                double interestRate;
                if (paymentDelayDays <= 7) {
                    interestRate = account.getInterestRateBefore7Days();
                    double interestAmount = balance * interestRate * paymentDelayDays;
                    balance += interestAmount;

                    double updatedLoans = account.getLoans() + interestAmount;
                    account.setLoans(updatedLoans);
                } else {
                    interestRate = account.getInterestRateAfter7Days();
                    double interestAmount = balance * interestRate * paymentDelayDays;
                    double updatedLoans = account.getLoans() + interestAmount;
                    balance += interestAmount;
                    account.setLoans(updatedLoans);
                }
                saveBalanceHistory(account, balance);
            }
        }

        if (balance >= amount) {
            double newMainBalance = balance - amount;
            account.setMainBalance(newMainBalance);
            saveBalanceHistory(account, newMainBalance);
        } else {
            System.out.println("Insufficient funds to make the withdrawal.");
        }
    }

    public void performTransfer(String accountNumberSender, String accountNumberReceiver, double amount, String transferReason, Instant effectiveDate, Instant registrationDate) {
        Account accountSender = accountDAO.getByAccountNumber(accountNumberSender);
        Account accountReceiver = accountDAO.getByAccountNumber(accountNumberReceiver);
        if (Objects.equals(accountSender.getBank().getName(), accountReceiver.getBank().getName())) {
            performSameBankTransfer(accountSender, accountReceiver, amount, transferReason, effectiveDate, registrationDate);
        }
        if (!Objects.equals(accountSender.getBank().getName(), accountReceiver.getBank().getName())) {
            performInterBankTransfer(accountSender, accountReceiver, amount, transferReason, effectiveDate, registrationDate);
        }


    }

    private void saveBalanceHistory(Account account, double newMainBalance) {
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setAccount(account);
        balanceHistory.setMainBalance(newMainBalance);
        balanceHistory.setLoans(account.getLoans());
        balanceHistory.setInterestLoans(account.getInterestLoans());
        balanceHistory.setDate(Timestamp.valueOf(LocalDateTime.now()));
        balanceHistoryDAO.save(balanceHistory);
    }

    private void saveTransferHistory(String sender, String receiver, Double amount, String reason, Instant effectiveDate, Instant registerDate) {
        Transfer transfer = new Transfer();
        transfer.setAccountSender(accountDAO.getByAccountNumber(sender));
        transfer.setAccountReceiver(accountDAO.getByAccountNumber(receiver));
        transfer.setAmount(amount);
        transfer.setTransferReason(reason);
        transfer.setEffectiveDate(effectiveDate);
        transfer.setRegistrationDate(registerDate);

        transferDAO.save(transfer);

    }

    private void performSameBankTransfer(Account accountSender, Account accountReceiver, double amount, String transferReason, Instant effectiveDate, Instant registrationDate) {
        if (accountSender.getMainBalance() >= amount) {
            accountSender.setMainBalance(accountSender.getMainBalance() - amount);
            accountReceiver.setMainBalance(accountReceiver.getMainBalance() + amount);
            accountDAO.updateBalance(accountSender);
            accountDAO.updateBalance(accountReceiver);
            saveBalanceHistory(accountSender, accountSender.getMainBalance());
            saveBalanceHistory(accountReceiver, accountReceiver.getMainBalance());
        }
        saveTransferHistory(accountSender.getAccountNumber(), accountReceiver.getAccountNumber(), amount, transferReason, effectiveDate, registrationDate);
    }

    private void performInterBankTransfer(Account accountSender, Account accountReceiver, double amount, String transferReason, Instant effectiveDate, Instant registrationDate) {
        Instant debitScheduledDate = effectiveDate.plus(Duration.ofHours(48));
        if (accountSender.getMainBalance() >= amount) {
            accountSender.setMainBalance(accountSender.getMainBalance() - amount);
            accountReceiver.setMainBalance(accountReceiver.getMainBalance() + amount);
            accountDAO.updateBalance(accountSender);
            accountDAO.updateBalance(accountReceiver);
            saveBalanceHistory(accountSender, accountSender.getMainBalance());
            saveBalanceHistory(accountReceiver, accountReceiver.getMainBalance());
        }
        saveTransferHistory(accountSender.getAccountNumber(), accountReceiver.getAccountNumber(), amount, transferReason, debitScheduledDate, registrationDate);
    }
}



