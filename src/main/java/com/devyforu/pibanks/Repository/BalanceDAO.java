package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Balance;
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
public class BalanceDAO implements CrudRepository<Balance> {
    private Connection connection;

    @Override
    public List<Balance> findAll() {
        List<Balance> balanceList = new ArrayList<>();
        String sql = """
                SELECT * FROM "balance" inner join "account" on balance.idAccount=account.id;
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
                        user,
                        bank,
                        resultSet.getDouble("creditAllow"),
                        resultSet.getBoolean("overDraftLimit")
                );
                Balance balance = new Balance(
                        resultSet.getString("id"),
                        resultSet.getDouble("mainBalance"),
                        resultSet.getDouble("loans"),
                        resultSet.getDouble("interestLoans"),
                        account);
                balanceList.add(balance);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return balanceList;
    }

    @Override
    public Balance save(Balance toSave) {
        String sql = """
                INSERT INTO "balance"(id,mainBalance,loans,interestLoans,idAccount)VALUES(?,?,?,?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getId());
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

    @Override
    public void deleteById(String id) {
        String sql = """
                DELETE from "balance" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                Balance balance = new Balance(id);
                System.out.println("Account deleted" + balance);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Balance getById(String id) {
        String sql = """
                Select * from "balance" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Balance balance= new Balance();
                balance.setId(resultSet.getString("id"));
                return balance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
