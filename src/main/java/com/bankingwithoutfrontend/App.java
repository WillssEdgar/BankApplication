package com.bankingwithoutfrontend;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    public static String menu() {
        String menuWelcome = "What Would you like to do today?";
        String deposit = " 1. Deposit";
        String withdrawal = " 2. Withdrawal";
        String checkBalance = " 3. Check Balance";
        String addAccount = " 4. Add Account";

        return String.format("%n%s%n%s%n%s%n%s%n%s%n", menuWelcome, deposit, withdrawal, checkBalance, addAccount);
    }

    public static void main(String[] args) {

        String line = "-".repeat(40);
        System.out.println(line);
        System.out.println("Welcome to WSE Banking!");
        Scanner scanner = new Scanner(System.in);

        User loggedInUser = null;
        Boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("0. Exit");
            System.out.print("Choose one of the above: ");
            int action = scanner.nextInt();

            if (1 == action || 2 == action) {
                loggedInUser = Bank.create_or_login(scanner, action);
                if (!loggedInUser.getFirstName().equals("Login Failed")) {
                    loggedIn = true;
                } else {
                    System.out.println("Error. Please try again");
                }
            } else if (0 == action) {
                System.out.println("Thank you for using WSE Banking. Goodbye!");
                break;
            } else {
                System.out.println("Not a valid input. Please try again!");
                System.out.println(line);
            }
        }

        while (loggedIn) {
            System.out.println(menu());
            System.out.print("Please enter a number (1-4, or 0 to exit): ");

            if (scanner.hasNext()) {
                String num = scanner.next();

                switch (num) {
                    case "1":
                        if (loggedInUser.accountNumbers.length > 1) {
                            System.out.println("Accounts to choose from: ");
                            for (int i = 0; i < loggedInUser.accountNumbers.length; i++) {
                                System.out.println("Account: " + loggedInUser.accountNumbers[i]);
                            }
                            System.out.print("Which Account would you like to deposit into?: ");
                            int accountNum = scanner.nextInt();
                            Account account = new Account(accountNum, loggedInUser.userID, 0.00);
                            System.out.print("How much would like to Deposit: $");
                            int depositAmount = scanner.nextInt();
                            Bank.Deposit(account, depositAmount);

                            break;
                        }
                    case "2":
                        if (loggedInUser.accountNumbers.length > 1) {
                            System.out.println("Accounts to choose from: ");
                            for (int i = 0; i < loggedInUser.accountNumbers.length; i++) {
                                System.out.println("Account: " + loggedInUser.accountNumbers[i]);
                            }
                            System.out.print("Which Account would you like to withdrawal from?: ");
                            int accountNum = scanner.nextInt();
                            Account account = new Account(accountNum, loggedInUser.userID, 0.00);
                            System.out.print("How much would like to Withdrawal: $");
                            int withdrawalAmount = scanner.nextInt();
                            Bank.Withdrawal(account, withdrawalAmount);

                            break;
                        }
                    case "3":
                        if (loggedInUser.accountNumbers.length > 1) {
                            System.out.println("Accounts to choose from: ");
                            for (int i = 0; i < loggedInUser.accountNumbers.length; i++) {
                                System.out.println("Account: " + loggedInUser.accountNumbers[i]);
                            }
                            System.out.print("Which Account balance would you like to check?: ");
                            int accountNum = scanner.nextInt();
                            Account account = new Account(accountNum, loggedInUser.userID, 0.00);

                            System.out.println(Bank.CheckBalance(account));

                            break;
                        }
                    case "4":
                        System.out.println(Bank.AddAccount(scanner, loggedInUser));
                        break;
                    case "0":
                        System.out.println("Thank you for using WSE Banking. Goodbye!");
                        return; // Exit the loop and end the program
                    default:
                        System.out.println("Invalid option. Please enter a valid number.");
                }

                if ("0".equals(num)) {
                    System.out.println("Thank you for using WSE Banking. Goodbye!");
                    break; // Exit the loop and end the program
                }

            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                // Consume the invalid input to avoid an infinite loop
                scanner.nextLine();
            }
        }

        // Close the scanner after use
        scanner.close();

    }

}
