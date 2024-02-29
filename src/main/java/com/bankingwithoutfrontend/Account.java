package com.bankingwithoutfrontend;

public class Account {
    public int accountNumber;
    public int userID;
    public double balance;

    public Account(int accountNumber, int userID, double balance) {
        this.accountNumber = accountNumber;
        this.userID = userID;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        String line = "-".repeat(20);

        return String.format("%s%nAccount Number: %7d %n%10d %nBalance: %16.2f %n%s", line, this.accountNumber,
                this.userID,
                this.balance);
    }
}
