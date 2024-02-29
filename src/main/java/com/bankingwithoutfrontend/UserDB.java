package com.bankingwithoutfrontend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDB {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Users ("
            + "id SERIAL PRIMARY KEY,"
            + "firstname VARCHAR(255),"
            + "lastname VARCHAR(255),"
            + "isadmin BOOLEAN,"
            + "email VARCHAR(255)"
            + ")";

    private static final String INSERT_USER_SQL = "INSERT INTO Users (firstname, lastname, isadmin, email)"
            + "VALUES(?,?,?,?)";

    public static int add(User user) {

        try (var conn = DB.connect();
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.executeUpdate(CREATE_TABLE_SQL);

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setBoolean(3, user.getIsAdmin());
            pstmt.setString(4, user.getEmail());

            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static final String SELECT_USER_ID_SQL = "SELECT id FROM Users WHERE firstname = ? AND lastname = ?";

    public static int getUserId(String firstname, String lastname) {
        try (var conn = DB.connect();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_ID_SQL)) {

            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no matching user is found
    }
}
