package HugoVanDerWel.services;


import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
//    private List<DatabaseModel> databases;
//    private DatabaseModel currentDatabase;
    private final String connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube;user=Spotitube;password=AppelTaart;encrypt=true;trustServerCertificate=true";


    public Database() {
//        databases = new ArrayList<>();
//        addNewDatabase("", "jdbc:sqlserver://localhost\\\\sqlexpress", "Spotitube", "AppelTaart");
//        currentDatabase = databases.get(0);
    }

//    public void addNewDatabase(String databaseType, String url, String username, String password) {
//        DatabaseModel databaseModel = new DatabaseModel();
//
//        this.databases.add(databaseModel);
//    }

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(connectionURL);
        } catch (Exception e) {
            throw new RuntimeException("Failed establishing database connection: " + e.getMessage(), e.getCause());
        }
    }

//    public void switchDatabase(int database) {
//        try {
//            currentDatabase = databases.get(database);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Database switch error");
//        }
//    }

//    public ResultSet executeQuery(String query, String[] attributes) {
//        try (Connection con = DriverManager
//                .getConnection(connectionURL)) {
//            PreparedStatement preparedStatement = con.prepareStatement(query);
//            if (attributes.length > 0) {
//                for (int attributeNumber = 1; attributeNumber <= attributes.length; attributeNumber++) {
//                    preparedStatement.setString(attributeNumber, attributes[attributeNumber]);
//                }
//            }
//            return preparedStatement.executeQuery();
//        } catch (SQLException e) {
//            throw new RuntimeException("A database error has occurred" + e.getErrorCode());
//        }
//    }
}
