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
                        (String) resultSet.getObject("firsName"),
                        (String) resultSet.getObject("lastName"),
                        resultSet.getTimestamp("birthdayDate"),
                        resultSet.getDouble("netMonthSalary")

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
                INSERT INTO "user"(id, firstName, lastName, birthdayDate, netMonthSalary) VALUES(?,?,?,?,?) 
                ON CONFLICT (id) DO UPDATE SET firstName=EXCLUDED.firstName, netMonthSalary=EXCLUDED.netMonthSalary,
                lastName=EXCLUDED.lastName, birthdayDate=EXCLUDED.birthdayDate;
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

    @Override
    public User delete(User toDelete) {
        String sql = """
                DELETE from "user" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, toDelete.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return toDelete;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public User update(User toUpdate) {
        String sql = """
                UPDATE "user"
                SET netMonthSalary=?
                WHERE id=?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, toUpdate.getNetMonthSalary());
            statement.setObject(2, toUpdate.getId());

            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                return toUpdate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
