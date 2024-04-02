package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class TransactionDAO implements CrudRepository<Transaction> {
    private Connection connection;
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;
    private CategoryDAO categoryDAO;

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = """
                SELECT * FROM "transaction" ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id_account = resultSet.getString("id");
                String id_transfer = resultSet.getString("id");
                String id_category = resultSet.getString("id");
                Category category = categoryDAO.getById(id_category);
                Account account = accountDAO.getById(id_account);
                Transfer transfer = transferDAO.getById(id_transfer);
                Transaction transaction = new Transaction(
                        resultSet.getString("id"),
                        transfer,
                        account,
                        category,
                        (TransactionType) resultSet.getObject("type"),
                        resultSet.getTimestamp("date").toInstant()
                );
                transactionList.add(transaction);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionList;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String sql = """
                INSERT INTO "transaction"(id_transfer,id_account,id_transfer, type) VALUES(?,?,?,?);
                               
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getTransfer().getId());
            statement.setString(2, toSave.getAccount().getId());
            statement.setString(3,toSave.getCategory().getId());
            statement.setObject(4, toSave.getType());
            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return toSave;
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }


    public void deleteById(String id) {
        String sql = """
                DELETE from "transaction" where id = ? ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Transaction " + id + " has been deleted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The transaction does not exist");
    }


    public Transaction getById(String id) {
        String sql = """
                Select * from "transaction" where id = ? ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id_category=resultSet.getString("id");
                String id_transfer=resultSet.getString("id");
                String id_account=resultSet.getString("id");
                Account account= accountDAO.getById(id_account);
                Transfer transfer=transferDAO.getById(id_transfer);
                Category category= categoryDAO.getById(id_category);
                Transaction transaction= new Transaction();
                transaction.setId(resultSet.getString("id"));
                transaction.setAccount(account);
                transaction.setTransfer(transfer);
                transaction.setCategory(category);
                transaction.setType((TransactionType) resultSet.getObject("type"));
                transaction.setDate(resultSet.getTimestamp("date").toInstant());

            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }
}
