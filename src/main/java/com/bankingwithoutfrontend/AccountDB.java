package com.bankingwithoutfrontend;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDB {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Account ("
            + "accountNumber INT PRIMARY KEY,"
            + "id INT,"
            + "balance DOUBLE PRECISION,"
            + "FOREIGN KEY (id) REFERENCES Users(id)"
            + ");";

    private static final String INSERT_USER_SQL = "INSERT INTO Account(accountNumber, id, balance)"
            + "VALUES(?,?,?);";

    public static int add(Account account) {

        try (var conn = DB.connect();
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.executeUpdate(CREATE_TABLE_SQL);

            pstmt.setInt(1, account.getAccountNumber());
            pstmt.setInt(2, account.getUserID());
            pstmt.setDouble(3, account.getBalance());

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
}
