package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Bank;
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
public class BankDAO implements CrudRepository<Bank> {
    private Connection connection;


    @Override
    public List<Bank> findAll() {
        List<Bank> bankList = new ArrayList<>();
        String sql = """
                SELECT * from "bank";
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bankList.add(new Bank(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("ref")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bankList;

    }

    @Override
    public Bank save(Bank toSave) {
        String sql = """
                INSERT INTO "bank"(name,ref) VALUE(?,?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getName());
            statement.setString(2, toSave.getReference());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void deleteById(String id) {
        String sql = """
                DELETE from "bank" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                Bank deletedBank = new Bank(id);
                System.out.println("Account deleted" + deletedBank);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Bank getById(String id) {
        String sql = """
                Select id,name,ref from "bank" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Bank bank = new Bank();
                bank.setId(resultSet.getString("id"));
                bank.setName(resultSet.getString("name"));
                bank.setReference(resultSet.getString("ref"));
                return bank;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
