package com.bankingwithoutfrontend;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    public static User create_or_login(Scanner scanner, int action) {
        /*
         * This method is called to initialize the User.
         * case 1 to if the user wants to login
         * case 2 is when the user wants to create an account
         */
        switch (action) {
            case 1:
                System.out.print("Enter your username: ");
                String username = scanner.next();
                if (!username.matches("^[a-zA-Z0-9_]+$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }
                System.out.print("Enter your password: ");
                String password = scanner.next();
                if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }
                User loggedInUser = Bank.login(username, password);

                return loggedInUser;

            case 2:
                System.out.print("Enter your First Name: ");
                String firstName = scanner.next();
                if (!firstName.matches("^[a-zA-Z]+$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }
                System.out.print("Enter your Last Name: ");
                String lastName = scanner.next();
                if (!lastName.matches("^[a-zA-Z]+$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }
                System.out.print("Enter your Email: ");
                String email = scanner.next();
                if (!email.matches("^[a-zA-Z0-9@]+$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }
                System.out.print("Enter your Username: ");
                username = scanner.next();
                if (!username.matches("^[a-zA-Z]+$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }
                System.out.print("Enter your Password: ");
                password = scanner.next();
                if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                    System.out.println("Error Invalid Input");
                    return new User("Login Failed");
                }

                User createdUser = new User(firstName, lastName, false, email, username, password);
                Bank.create(createdUser);
                return createdUser;
            default:
                return new User("Login Failed");
        }
    }

    public static User create(User user) {

        final String CREATE_USER = "INSERT INTO Users (firstname, lastname, isadmin, email, username, password) VALUES(?,?,?,?,?,?);";
        try (var conn = DB.connect();
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.firstName);
            pstmt.setString(2, user.lastName);
            pstmt.setBoolean(3, false);
            pstmt.setString(4, user.email);
            pstmt.setString(5, user.username);
            pstmt.setString(6, user.password);

            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    user.userID = rs.getInt(1);
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static User login(String username, String password) {

        final String LOGIN_CHECK = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (var conn = DB.connect();
                PreparedStatement pstmt = conn.prepareStatement(LOGIN_CHECK)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    Boolean isAdmin = rs.getBoolean("isadmin");
                    String email = rs.getString("email");
                    String username2 = rs.getString("username");
                    String password2 = rs.getString("password");
                    Array accountNumbersArray = rs.getArray("accountnumbers");
                    Integer[] accountNumbers = (Integer[]) accountNumbersArray.getArray();
                    User user = new User(id, firstname, lastname, isAdmin, accountNumbers, email, username2, password2);
                    return user;
                } else {
                    return new User("Login Failed");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new User("Login Failed");
        }

    }

    public static void Deposit(Account account, float depositAmount) {
        final String UPDATE_BALANCE = "UPDATE account SET balance = balance + ? WHERE accountnumber = ?";

        try (var conn = DB.connect();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_BALANCE)) {

            pstmt.setDouble(1, depositAmount);
            pstmt.setInt(2, account.getAccountNumber());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Deposit Successful!");
                account.setBalance(account.getBalance() + depositAmount);
            } else {
                System.out.println("Deposit failed. Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String Withdrawal(Account account, float withdrawalAmount) {
        final String UPDATE_BALANCE = "UPDATE account SET balance = balance - ? WHERE accountnumber = ?";

        try (var conn = DB.connect();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_BALANCE)) {

            pstmt.setDouble(1, withdrawalAmount);
            pstmt.setInt(2, account.getAccountNumber());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Withdrawal Successful!");
                account.setBalance(account.getBalance() - withdrawalAmount);
            } else {
                System.out.println("Withdrawal failed. Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String CheckBalance(Account account) {
        final String SELECT_BALANCE = "SELECT balance FROM Account WHERE accountnumber = ?";
        try (var conn = DB.connect();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BALANCE)) {

            pstmt.setInt(1, account.accountNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "This is your current Balance: $" + rs.getInt("balance");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No Account Found!";
    }

    public static int CreateAccountNumber() {
        Random random = new Random();

        int min = 10000000;
        int max = 99999999;
        int accountNumber = random.nextInt(max - min + 1) + min;
        while (true) {
            Boolean checkAccountNumber = AccountNumberInDatabase(accountNumber);
            if (!checkAccountNumber) {
                return accountNumber;
            }
        }
    }

    public static Boolean AccountNumberInDatabase(int accountNumber) {
        final String GET_ACCOUNT_NUMBERS = "SELECT COUNT(accountnumber) FROM Account "
                + "WHERE accountnumber = ?;";

        try (var conn = DB.connect();
                PreparedStatement pstmt = conn.prepareStatement(GET_ACCOUNT_NUMBERS)) {
            pstmt.setInt(1, accountNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void AddAccountToUser(User user, int accountNumber) {
        final String GET_ACCOUNT_NUMBERS = "SELECT accountNumbers FROM Users WHERE id = ?";
        final String UPDATE_ACCOUNT_NUMBERS = "UPDATE Users SET accountNumbers = ? WHERE id = ?";

        try (var conn = DB.connect();
                PreparedStatement pstmtSelect = conn.prepareStatement(GET_ACCOUNT_NUMBERS);
                PreparedStatement pstmtUpdate = conn.prepareStatement(UPDATE_ACCOUNT_NUMBERS)) {

            // Step 1: Fetch the existing account numbers array
            pstmtSelect.setInt(1, user.getUserID());
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                Integer[] existingAccountNumbers = (Integer[]) rs.getArray("accountNumbers").getArray();

                // Step 2: Append the new account number
                Integer[] updatedAccountNumbers = Arrays.copyOf(existingAccountNumbers,
                        existingAccountNumbers.length + 1);
                updatedAccountNumbers[existingAccountNumbers.length] = accountNumber;

                // Step 3: Update the array in the database
                pstmtUpdate.setArray(1, conn.createArrayOf("INTEGER", updatedAccountNumbers));
                pstmtUpdate.setInt(2, user.getUserID());
                pstmtUpdate.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String AddAccount(Scanner scanner, User user) {
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Would you like to deposit money into the account?: ");
        String option = scanner.next();

        if ("1".equals(option)) {
            System.out.print("How much would you like to deposit?:  ");
            double amount = scanner.nextInt();

            final String ADD_ACCOUNT = "INSERT INTO Account(accountNumber, id, balance)"
                    + "VALUES(?,?,?);";
            try (var conn = DB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(ADD_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {

                Account account = new Account(CreateAccountNumber(), user.getUserID(), amount);
                pstmt.setInt(1, account.getAccountNumber());
                pstmt.setInt(2, account.getUserID());
                pstmt.setDouble(3, account.getBalance());

                int insertedRow = pstmt.executeUpdate();
                if (insertedRow > 0) {
                    var rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        return "Account created!";
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if ("2".equals(option)) {
            final String ADD_ACCOUNT = "INSERT INTO Account(accountNumber, id, balance)"
                    + "VALUES(?,?,?);";
            try (var conn = DB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(ADD_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {

                Account account = new Account(CreateAccountNumber(), user.getUserID(), 0.00);
                pstmt.setInt(1, account.getAccountNumber());
                pstmt.setInt(2, account.getUserID());
                pstmt.setDouble(3, account.getBalance());

                int insertedRow = pstmt.executeUpdate();
                if (insertedRow > 0) {
                    return "Account created with account number: " + account.getAccountNumber();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
