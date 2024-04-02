package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.FinancialSummary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;

@Repository
@AllArgsConstructor
public class FinancialSummaryDAO {
    private Connection connection;

    public FinancialSummary getFinancialSummaryBetweenDates(Date startDate, Date endDate) {
        String query = """
                Select * from aggregate_expenses_and_income(?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FinancialSummary summary = new FinancialSummary();
                summary.setYear(resultSet.getInt("year"));
                summary.setMonth(resultSet.getInt("month"));
                summary.setTotal_expenses(resultSet.getDouble("total_expenses"));
                summary.setTotal_income(resultSet.getDouble("total_income"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
