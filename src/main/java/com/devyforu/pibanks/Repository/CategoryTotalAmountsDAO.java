package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.CategoryTotalAmounts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@AllArgsConstructor
public class CategoryTotalAmountsDAO {
    private Connection connection;

    public CategoryTotalAmounts getAmountCategoryBetweenDates(Date startDate, Date endDate){
        String query= """
                Select * from aggregate_amount_by_category(?,?);
                """;
        try(PreparedStatement statement=connection.prepareStatement(query)) {
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            ResultSet resultSet= statement.executeQuery();
            while(resultSet.next()){
                CategoryTotalAmounts totalAmounts=new CategoryTotalAmounts();
                totalAmounts.setCategory_name(resultSet.getString("category_name"));
                totalAmounts.setTotal_amount(resultSet.getDouble("total_amount"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
