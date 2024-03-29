package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.BalanceHistory;
import com.devyforu.pibanks.Repository.AccountDAO;
import com.devyforu.pibanks.Repository.BalanceHistoryDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class AccountService {
    private AccountDAO accountDAO;
    private BalanceHistoryDAO balanceHistoryDAO;

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

        if (!account.getOverDraftLimit()) {
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

    private void saveBalanceHistory(Account account, double newMainBalance) {
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setAccount(account);
        balanceHistory.setMainBalance(newMainBalance);
        balanceHistory.setLoans(account.getLoans());
        balanceHistory.setInterestLoans(account.getInterestLoans());
        balanceHistory.setDate(Timestamp.valueOf(LocalDateTime.now()));
        balanceHistoryDAO.save(balanceHistory);
    }
}



