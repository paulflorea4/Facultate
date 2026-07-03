package org.example.template.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/mappractic";
    private static final String user = "postgres"; // Your pgAdmin 4 username
    private static final String password = "flo"; // Your pgAdmin 4 password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
