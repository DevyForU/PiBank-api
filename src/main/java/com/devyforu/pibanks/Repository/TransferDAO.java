package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Model.Transfer;
import com.devyforu.pibanks.Model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class TransferDAO implements CrudRepository<Transfer> {
    private Connection connection;

    @Override
    public List<Transfer> findAll() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = """
                SELECT * FROM "transfer"
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
                Account accountSender = new Account(
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
                Account accountReceiver = new Account(
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
                Transfer transfer = new Transfer(
                        resultSet.getString("id"),
                        resultSet.getString("reference"),
                        resultSet.getString("transfer_reason"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("effective_date").toInstant(),
                        resultSet.getTimestamp("registration_date").toInstant(),
                        resultSet.getBoolean("is_canceled"),
                        accountSender,
                        accountReceiver
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
                 INSERT INTO "transfer"(id, transfer_reason, amount, effective_date, registration_date, is_canceled) VALUES(?,?,?,?,?,?) 
                 ON CONFLICT (id) DO UPDATE SET transfer_reason=EXCLUDED.transfer_reason, amount=EXCLUDED.amount,
                effective_date=EXCLUDED.effective_date, registration_date=EXCLUDED.registration_date, is_canceled=EXCLUDED.is_canceled;
                 """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, toSave.getId());
            statement.setString(2, toSave.getTransferReason());
            statement.setDouble(3, toSave.getAmount());
            statement.setTimestamp(4, Timestamp.from(toSave.getEffectiveDate()));
            statement.setTimestamp(5, Timestamp.from(toSave.getRegistrationDate()));
            statement.setBoolean(6, toSave.isCanceled());

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
        System.out.println("The user does not exist");
    }


    public Transfer updateTransferReasonById(String id, String transferReason) {
        String sql = """
                UPDATE "transfer"
                SET transfer_reason=?
                WHERE id=?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setObject(2, transferReason);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return this.getById(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transfer getById(String id) {
        String sql = """
                Select "transfer".reference,"transfer".reason,
                "transfer".effective_date,"transfer".registration_date,
                "transfer".is_canceled from "transfer"
                inner join 
                where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {User user = new User(
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
                Account accountSender = new Account(
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
                Account accountReceiver = new Account(
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

                return new Transfer(
                        resultSet.getString("id"),
                        resultSet.getString("reference"),
                        resultSet.getString("transfer_reason"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("effective_date").toInstant(),
                        resultSet.getTimestamp("registration_date").toInstant(),
                        resultSet.getBoolean("is_canceled"),
                        accountSender,
                        accountReceiver

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
