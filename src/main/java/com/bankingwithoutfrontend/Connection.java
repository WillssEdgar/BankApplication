package com.bankingwithoutfrontend;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    public static java.sql.Connection connectToDatabase() throws SQLException {
        String jdbcUrl = "localhost/5431";
        String username = "willsedgar";
        String password = "2123";

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Connected to Database");

        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
