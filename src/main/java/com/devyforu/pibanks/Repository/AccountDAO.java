package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class AccountDAO implements CrudRepository<Account> {

    private Connection connection;

    @Override
    public List<Account> findAll() {
        List<Account> accountList = new ArrayList<>();
        String sql = """
                 SELECT * FROM account inner join "user"
                                        on account.id_user="user".id
                                        inner join bank
                                        on account.id_bank=bank.id;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getTimestamp("birthday_date"),
                        resultSet.getDouble("net_month_salary")
                );
                Bank bank = new Bank(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("reference")
                );
                Account account = new Account(
                        resultSet.getString("id"),
                        resultSet.getString("account_number"),
                        resultSet.getDouble("main_balance"),
                        resultSet.getDouble("loans"),
                        resultSet.getDouble("interest_loans"),
                        resultSet.getDouble("credit_allow"),
                        resultSet.getBoolean("over_draft_limit"),
                        resultSet.getDouble("interest_rate_before_7_days"),
                        resultSet.getDouble("interest_rate_after_7_days"),
                        user,
                        bank
                );
                accountList.add(account);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountList;
    }

    @Override
    public Account save(Account toSave) {
        String sql = """
                INSERT INTO "account" (account_number,idUser,idBank) VALUES (?,?,?)
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getAccountNumber());
            statement.setString(4, toSave.getUser().getId());
            statement.setString(5, toSave.getBank().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public void deleteByAccountNumber(String accountNumber) {
        String sql = """
                DELETE from "account" where account_number = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, accountNumber);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                Account deletedAccount = new Account(accountNumber);
                System.out.println("Account deleted" + deletedAccount);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Account getByAccountNumber(String accountNumber) {
        String sql = """
                Select * from "account" where account_number = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getString("account_number"));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
