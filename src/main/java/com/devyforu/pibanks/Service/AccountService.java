package com.devyforu.pibanks.Service;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Transaction;
import com.devyforu.pibanks.Model.TransactionType;
import com.devyforu.pibanks.Model.Transfer;
import com.devyforu.pibanks.Repository.AccountDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountDAO accountDAO;

    public void makeWithdrawal(String accountId, double amount) {
        Account account = accountDAO.getById(accountId);
        if (account != null) {
            double availableBalance = account.getMainBalance();
            if (availableBalance >= amount) {
                double newMainBalance = account.getMainBalance() - amount;
                account.setMainBalance(newMainBalance);

                accountDAO.update(account);

                Transaction transaction = new Transaction();
                transaction.setType(TransactionType.DEBIT);
                transaction.setLabel("Withdrawal");
                transaction.setTransfer(new Transfer(amount));

                account.getTransactionList().add(transaction);

                accountDAO.update(account);

                System.out.println("Withdrawal successful.");
            }
            else if (account.getOverDraftLimit()) {

            } else {
                System.out.println("Error: Insufficient funds.");
            }
        } else {
            System.out.println("Error: Account not found.");
        }
    }

}
