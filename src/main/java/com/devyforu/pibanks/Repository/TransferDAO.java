package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Transfer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class TransferDAO implements CrudRepository<Transfer> {
    private Connection connection;
    private AccountDAO accountDAO;

    @Override
    public List<Transfer> findAll() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = """
                SELECT * FROM "transfer"
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String senderId = resultSet.getString("id_sender");
                String receiverId = resultSet.getString("id_receiver");
                Account senderAccount = accountDAO.getById(senderId);
                Account receiverAccount = accountDAO.getById(receiverId);

                Transfer transfer = new Transfer(
                        resultSet.getString("id"),
                        resultSet.getString("reference"),
                        resultSet.getString("transfer_reason"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("label"),
                        resultSet.getTimestamp("effective_date").toInstant(),
                        resultSet.getTimestamp("registration_date").toInstant(),
                        resultSet.getBoolean("is_canceled"),
                        senderAccount,
                        receiverAccount
                );
                transferList.add(transfer);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transferList;
    }

    @Override
    public Transfer save(Transfer toSave) {
        String sql = """
                INSERT INTO "transfer"(id_sender, id_receiver, amount, registration_date, effective_date, reason, label, is_canceled) VALUES(?,?,?,?,?,?,?,?);
                 """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getAccountSender().getId());
            statement.setString(2, toSave.getAccountReceiver().getId());
            statement.setDouble(3, toSave.getAmount());
            statement.setTimestamp(4, Timestamp.from(toSave.getRegistrationDate()));
            statement.setTimestamp(5, Timestamp.from(toSave.getEffectiveDate()));
            statement.setString(6, toSave.getTransferReason());
            statement.setString(7, toSave.getLabel());
            statement.setBoolean(8, toSave.isCanceled());

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
                DELETE from "transfer" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Transfer " + id + " has been deleted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The transfer does not exist");
    }

    public Transfer getById(String id) {
        String sql = """
                Select * from "transfer"where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String senderId = resultSet.getString("id_sender");
                String receiverId = resultSet.getString("id_receiver");

                Account senderAccount = accountDAO.getById(senderId);
                Account receiverAccount = accountDAO.getById(receiverId);
                Transfer transfer=new Transfer();
                transfer.setId(resultSet.getString("id"));
                transfer.setReference(resultSet.getString("ref"));
                transfer.setAmount(resultSet.getDouble("amount"));
                transfer.setRegistrationDate(resultSet.getTimestamp("registration").toInstant());
                transfer.setEffectiveDate(resultSet.getTimestamp("effective_date").toInstant());
                transfer.setTransferReason(resultSet.getString("reason"));
                transfer.setLabel(resultSet.getString("label"));
                transfer.setCanceled(resultSet.getBoolean("is_canceled"));
                transfer.setAccountSender(senderAccount);
                transfer.setAccountReceiver(receiverAccount);

            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }
}
