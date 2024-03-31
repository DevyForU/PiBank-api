package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Transaction;
import com.devyforu.pibanks.Model.TransactionType;
import com.devyforu.pibanks.Model.Transfer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
@AllArgsConstructor
public class TransactionDAO implements CrudRepository<Transaction> {
    private Connection connection;

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = """
                SELECT * FROM "transaction"
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactionList.add(new Transaction (
                        resultSet.getString("id"),
                        null,
                        resultSet.getString("label"),
                        (TransactionType) resultSet.getObject("type")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionList;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String sql = """
                INSERT INTO "transaction"(id, id_transfer, label, type) VALUES(?,?,?,?) 
                ON CONFLICT (id) DO UPDATE SET id_transfer=EXCLUDED.id_transfer, label=EXCLUDED.label,
               type=EXCLUDED.type;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, toSave.getId());
            statement.setObject(2, toSave.getTransfer());
            statement.setString(3, toSave.getLabel());
            statement.setObject(4, toSave.getType());
            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return toSave;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteById(String id) {
        String sql = """
                DELETE from "transaction" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Transfer "+ id +" has been deleted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The user does not exist");
    }


    public Transaction updateLabelById(String id, String label) {
        String sql = """
                UPDATE "transaction"
                SET label=?
                WHERE id=?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setString(2, label);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return this.getById(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Transaction getById(String id) {
        String sql= """
                Select * from "transaction" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Transaction(
                         resultSet.getString("id"),
                         null,
                        resultSet.getString("label"),
                        (TransactionType) resultSet.getObject("type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
