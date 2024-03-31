package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.BalanceHistory;
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
    private AccountDAO accountDAO;

    @Override
    public List<BalanceHistory> findAll() {
        List<BalanceHistory> balanceHistoryList = new ArrayList<>();
        String sql = """
                SELECT * FROM "balance_history";
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id_account = resultSet.getString("id_account");
                Account account= accountDAO.getById(id_account);
                BalanceHistory balanceHistory = new BalanceHistory(
                        resultSet.getString("id"),
                        resultSet.getDouble("main_balance"),
                        resultSet.getDouble("loans"),
                        resultSet.getDouble("loans_interest"),
                        resultSet.getTimestamp("date").toInstant(),
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
                INSERT INTO "balance_history"(main_balance,loans,loans_interest,id_account)VALUES(?,?,?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, toSave.getMainBalance());
            statement.setDouble(2, toSave.getLoans());
            statement.setDouble(3, toSave.getLoansInterest());
            statement.setString(4, toSave.getAccount().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void deleteById(String id) {
        String sql = """
                DELETE from "balance_history" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                BalanceHistory balanceHistory = new BalanceHistory(id);
                System.out.println("balance deleted" + balanceHistory);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public BalanceHistory getById(String id) {
        String sql = """
                Select * from "balance_history" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id_account = resultSet.getString("id_account");
                Account account= accountDAO.getById(id_account);
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(resultSet.getString("id"));
                balanceHistory.setMainBalance(resultSet.getDouble("main_balance"));
                balanceHistory.setLoans(resultSet.getDouble("loans"));
                balanceHistory.setLoansInterest(resultSet.getDouble("loans_interest"));
                balanceHistory.setDate(resultSet.getTimestamp("date").toInstant());
                balanceHistory.setAccount(account);

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
