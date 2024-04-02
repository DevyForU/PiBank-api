package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.AccountStatement;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@AllArgsConstructor
public class AccountStatementDAO {
    private Connection connection;

    public AccountStatement getStatementAccountByAccountNumber(String accountNumber) {
        String query = """
                select * from account_statements(?);
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                AccountStatement statement = new AccountStatement();
                statement.setDate(resultSet.getDate("date").toInstant());
                statement.setReference(resultSet.getString("reference"));
                statement.setReason(resultSet.getString("reason"));
                statement.setCredit(resultSet.getDouble("credit"));
                statement.setDebit(resultSet.getDouble("debit"));
                statement.setBalance(resultSet.getDouble("balance"));

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
