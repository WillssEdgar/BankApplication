package com.bankingwithoutfrontend;

public class User {
    public int userID;
    public String firstName;
    public String lastName;
    public boolean isAdmin;
    public Integer[] accountNumbers;
    public String email;
    public String username;
    public String password;

    public User(int userID, String firstName, String lastName, boolean isAdmin, Integer[] accountNumbers,
            String email, String username, String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.accountNumbers = accountNumbers;
        this.email = email;
        this.username = username;
        this.password = password;

    }

    public User(String firstName, String lastName, boolean isAdmin, Integer[] accountNumbers, String email,
            String username,
            String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.accountNumbers = accountNumbers;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, boolean isAdmin, Integer[] accountNumbers, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.accountNumbers = accountNumbers;
        this.email = email;

    }

    public User(String firstName, String lastName, boolean isAdmin, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.email = email;

    }

    public User(String firstName, String lastName, boolean isAdmin, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String firstName) {
        this.firstName = firstName;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }

    public Integer[] getAccountNumbers() {
        return this.accountNumbers;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String toString() {
        if (this.isAdmin == false) {
            return String.format("First Name: %10s %n Last Name: %10s", this.firstName, this.lastName);
        } else {
            return String.format("First Name: %13s %nLast Name: %14s %nIs Admin: %14b", this.firstName, this.lastName,
                    this.isAdmin);
        }
    }
}
