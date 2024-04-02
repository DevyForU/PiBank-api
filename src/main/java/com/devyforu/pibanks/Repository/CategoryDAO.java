package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Category;
import com.devyforu.pibanks.Model.CategoryType;
import com.devyforu.pibanks.Model.TransactionType;
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
public class CategoryDAO implements CrudRepository<Category> {
    private Connection connection;


    @Override
    public List<Category> findAll() {
        List<Category> categoryList = new ArrayList<>();
        String sql = """
                Select * from "category"
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        (CategoryType) resultSet.getObject("type")
                );
                categoryList.add(category);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }

    @Override
    public Category save(Category toSave) {
        String sql = """
                Insert into "category" (name,type) values (?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getId());
            statement.setObject(2, toSave.getType());
            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return toSave;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteById(String id) {
        String sql = """
                DELETE from "category" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Category" + id + " has been deleted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The category does not exist");
    }


    public Category getById(String id) {
        String sql = """
                Select * from "category" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getString("id"));
                category.setName(resultSet.getString("name"));
                category.setType((CategoryType) resultSet.getObject("type"));
                return category;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }
}
