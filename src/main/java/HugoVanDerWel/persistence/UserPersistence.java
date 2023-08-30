package HugoVanDerWel.persistence;

import HugoVanDerWel.Models.UserModel;
import HugoVanDerWel.services.Database;
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

    public UserModel getPasswordForUser(UserModel user) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "SELECT userhash FROM spotitubeUser u WHERE u.username = ?");
            query.setString(1, user.username);
            ResultSet resultset = query.executeQuery();
            user.password = resultset.getString("userhash");
            return user;
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
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
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public UserModel setTokenForUser(UserModel user, String token) {
        user.token = token;
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "UPDATE User SET token = ? WHERE username = ?");
            query.setString(1, user.token);
            query.setString(2, user.username);
            query.executeQuery();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
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
            throw new RuntimeException(e.getMessage(), e.getCause());
        }    }
}
