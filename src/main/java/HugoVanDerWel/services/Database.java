package HugoVanDerWel.services;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {

    private final String connectionURL;


    public Database() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
            Class.forName(properties.getProperty("driver"));

            connectionURL = properties.getProperty("connectionString");
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("A database connection error has occurred.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(connectionURL);
        } catch (Exception e) {
            throw new RuntimeException("Failed establishing database connection.");
        }
    }
}
