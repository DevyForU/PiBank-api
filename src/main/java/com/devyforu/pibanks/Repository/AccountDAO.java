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
                 SELECT * FROM "account inner join "user"
                                        on account.idUser="user".id
                                        inner join bank
                                        on account.idBank=bank.id";
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
                INSERT INTO "account" (id,credit_allow, over_draft_limit,idUser,idBank) VALUES (?,?,?,?,?)
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getId());
            statement.setDouble(2, toSave.getCreditAllow());
            statement.setBoolean(3, toSave.getOverDraftLimit());
            statement.setString(4, toSave.getUser().getId());
            statement.setString(5, toSave.getBank().getId());
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
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                Account deletedAccount = new Account(id);
                System.out.println("Account deleted" + deletedAccount);
                ;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Account getById(String id) {
        String sql = """
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
