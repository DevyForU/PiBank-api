package com.devyforu.pibanks.Repository;

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
public class UserDAO implements CrudRepository<User> {
    private Connection connection;

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = """
                SELECT * FROM "user"
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(
                        (String) resultSet.getObject("id"),
                        (String) resultSet.getObject("first_name"),
                        (String) resultSet.getObject("last_name"),
                        resultSet.getTimestamp("birthday"),
                        resultSet.getDouble("net_month_salary")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public User save(User toSave) {
        String sql = """
                INSERT INTO "user"
                (id, first_name, last_name, birthday, net_month_salary) VALUES(?,?,?,?,?) ;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, toSave.getId());
            statement.setString(2, toSave.getFirstName());
            statement.setString(3, toSave.getLastName());
            statement.setTimestamp(4, toSave.getBirthdayDate());
            statement.setDouble(5, toSave.getNetMonthSalary());

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
                DELETE from "user" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User "+ id +" has been deleted");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The user does not exist");
    }



    public User updateNetMonthSalaryById(String id, double netMontSalary) {
        String sql = """
                UPDATE "user"
                SET net_month_salary=?
                WHERE id=?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, netMontSalary);
            statement.setObject(2, id);

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return this.getById(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User getById(String id) {
        String sql= """
                Select * from "user" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        (String) resultSet.getObject("id"),
                        (String) resultSet.getObject("first_name"),
                        (String) resultSet.getObject("last_name"),
                        resultSet.getTimestamp("birthday"),
                        resultSet.getDouble("net_month_salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
