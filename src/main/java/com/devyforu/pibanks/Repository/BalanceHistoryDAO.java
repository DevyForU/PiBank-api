package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.BalanceHistory;
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
public class BalanceHistoryDAO implements CrudRepository<BalanceHistory> {
    private Connection connection;

    @Override
    public List<BalanceHistory> findAll() {
        List<BalanceHistory> balanceHistoryList = new ArrayList<>();
        String sql = """
                SELECT * FROM "balanceHistory" inner join "account" on balanceHistory.idAccount=account.id;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("LastName"),
                        resultSet.getTimestamp("birthdayDate"),
                        resultSet.getDouble("netMonthSalary")
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
                BalanceHistory balanceHistory = new BalanceHistory(
                        resultSet.getString("id"),
                        resultSet.getDouble("mainBalance"),
                        resultSet.getDouble("loans"),
                        resultSet.getDouble("interestLoans"),
                        resultSet.getTimestamp("date"),
                        account);
                balanceHistoryList.add(balanceHistory);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return balanceHistoryList;
    }

    @Override
    public BalanceHistory save(BalanceHistory toSave) {
        String sql = """
                INSERT INTO "balanceHistory"(mainBalance,loans,interestLoans,idAccount)VALUES(?,?,?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(2, toSave.getMainBalance());
            statement.setDouble(3, toSave.getLoans());
            statement.setDouble(4, toSave.getInterestLoans());
            statement.setString(5, toSave.getAccount().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void deleteById(String id) {
        String sql = """
                DELETE from "balanceHistory" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                BalanceHistory balanceHistory = new BalanceHistory(id);
                System.out.println("Account deleted" + balanceHistory);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public BalanceHistory getById(String id) {
        String sql = """
                Select * from "balanceHistory" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(resultSet.getString("id"));
                return balanceHistory;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    //public BalanceHistory getAccountBalanceHistoryByAccountNumber(String accountNumber){
//
//        //NEED A SQL FUNCTION
//    String sql= """
////                SELECT
////                    "account".account_number,
////                    "balance_history".main_balance,
////                    "account".loans,
////                    "account".loans_interest,
////                    "balance_history".date
////                FROM
////                    "balance_history"
////                JOIN
////                    "account" ON balance_history.id_account = "account".id
////                JOIN
////                    "bank" ON account.id_bank ="bank".id
////                JOIN
////                    "user" ON "account".id_user = "user".id
////
////                WHERE "account".account_number='ACC001';
////                """;
//    }
}
