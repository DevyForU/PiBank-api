package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
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
        List<Account> accountList=new ArrayList<>();
        String sql = """
                 SELECT * FROM "account";
                """;
        try(PreparedStatement statement=connection.prepareStatement(sql)){
            ResultSet resultSet= statement.executeQuery();
            while (resultSet.next()){
                accountList.add(new Account(
                        (String) resultSet.getObject("id"),
                        resultSet.getDouble("mainBalance"),
                        resultSet.getDouble("loans"),
                        resultSet.getDouble("interestLoans"),
                        resultSet.getDouble("creditAllow"),
                        resultSet.getBoolean("allowDraftLimit")
                ));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Account save(Account toSave) {
        String sql = """
                INSERT INTO "account" (id, id_user, main_balance, loans, loans_interest, credit_allow, over_draft_limit) VALUES (?, ?, ?, ?, ?, ?, ?)
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getId());
            statement.setString(2, toSave.getUser().getId()); // Save the user ID
            statement.setDouble(3, toSave.getMainBalance());
            statement.setDouble(4, toSave.getLoans());
            statement.setDouble(5, toSave.getInterestLoans());
            statement.setDouble(6, toSave.getCreditAllow());
            statement.setBoolean(7, toSave.getOverDraftLimit());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        String sql = """
                DELETE from "account" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1,id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                Account deletedAccount= new Account(id);
                System.out.println("Account deleted"+ deletedAccount);;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Account updateBalance(Account toUpdate) {
        String sql= """
                UPDATE "account"
                SET main_balance = ?
                where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, toUpdate.getMainBalance());
            statement.setObject(2, toUpdate.getId());

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return toUpdate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Account updateTransactionList(Account toUpdate) {
        String sql= """
                UPDATE "account"
                SET  = ?
                where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, toUpdate.getTransactionList());
            statement.setObject(2, toUpdate.getId());

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return toUpdate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Account getById(String id) {
        String sql= """
                Select * from "account" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getString("id"));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
