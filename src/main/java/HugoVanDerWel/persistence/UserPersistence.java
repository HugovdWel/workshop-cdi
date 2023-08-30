package HugoVanDerWel.persistence;

import HugoVanDerWel.exceptions.UserNotFoundException;
import HugoVanDerWel.models.UserModel;
import HugoVanDerWel.services.Database;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPersistence {

    private Database db;

    @Inject
    public UserPersistence(Database db) {
        this.db = db;
    }

    public UserModel getPasswordForUser(UserModel user) throws UserNotFoundException {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "SELECT userhash FROM spotitubeUser u WHERE u.username = ?");
            query.setString(1, user.username);
            ResultSet resultset = query.executeQuery();
            if (resultset.next()) {
                user.password = resultset.getString("userhash");
                return user;
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
        throw new UserNotFoundException();
    }

    public UserModel getTokenForUser(UserModel user) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "SELECT token FROM spotitubeUser u WHERE u.username = ?");
            query.setString(1, user.username);
            ResultSet resultset = query.executeQuery();
            user.token = resultset.getString("token");
            return user;
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public UserModel setTokenForUser(UserModel user, String token) {
        user.token = token;
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "UPDATE spotitubeUser SET token = ? WHERE username = ?");
            query.setString(1, user.token);
            query.setString(2, user.username);
            query.executeUpdate();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
        return user;
    }

    public UserModel getUsernameForToken(UserModel user) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "SELECT username FROM spotitubeUser u WHERE u.token = ?");
            query.setString(1, user.token);
            ResultSet resultset = query.executeQuery();
            user.username = resultset.getString("username");
            return user;
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }
}
