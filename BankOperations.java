package Interface;

import java.io.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import Interface.AccountOperations;
import Interface.TransactionOperations;

public class BankOperations implements AccountOperations, TransactionOperations {
    private Connection con;
    private Statement st;
    private Scanner sc;

    public BankOperations(Connection con, Scanner sc) throws SQLException {
        this.con = con;
        this.st = con.createStatement();
        this.sc = sc;
    }

    public static void showAllAccounts(Statement st2) throws SQLException {
        String query = "SELECT * FROM bank_details";
        ResultSet rs = st2.executeQuery(query);

        System.out.println("All accounts in the database:");
        while (rs.next()) {
            System.out.println("Account Number:- " + rs.getInt("account_number") +
                    "  __  Name:- " + rs.getString("f_name") + " " + rs.getString("l_name") +
                    "  __  Age:- " + rs.getInt("age") +
                    "  __  Type:- " + rs.getString("account_type") +
                    "  __  Balance:- " + rs.getDouble("amount") +
                    "  __  Phone Number:- " + rs.getDouble("P_number") +
                    "  __  PIN:- " + rs.getInt("pin"));

        }
    }

    @Override
    public void openAccount() {
        try {
            System.out.println("----------------------");
            System.out.println("Enter your first name:");
            System.out.println("----------------------");
            String fname = validateName(sc);
            System.out.println("----------------------");
            System.out.println("Enter your last name:");
            System.out.println("----------------------");
            String lname = validateName(sc);
            System.out.println("----------------------");
            System.out.println("Enter your age:");
            System.out.println("----------------------");
            int age = readAndValidateAge(sc);
            System.out.println("----------------------");
            System.out.println("Enter your account type:");
            System.out.println("----------------------");
            String type = getValidAccountType(sc);
            System.out.println("----------------------");
            System.out.println("Enter amount:");
            System.out.println("----------------------");
            double amount = getValidatedAmount(sc);
            System.out.println("--------------------------------------------");
            System.out.println("Enter your phone number (must be 10 digits):");
            System.out.println("--------------------------------------------");
            String pnumber = getValidatedPhoneNumber(sc);

            int accountNumber = (int) (Math.random() * 1000000);
            System.out.println("-------------------------------------");
            System.out.println("Your account number is " + accountNumber);
            System.out.println("-------------------------------------");
            System.out.println("----------------------------------------");
            System.out.println("Set your PIN (must be exactly 4 digits):");
            System.out.println("----------------------------------------");
            int pin = getValidatedPin(sc);
            System.out.println("-------------------------------------");
            System.out.println("You set a valid 4-digit PIN: " + pin);
            System.out.println("-------------------------------------");

            String sql = "INSERT INTO bank_details(f_name, l_name, age, account_type, amount, P_number, pin, account_number) VALUES('"
                    + fname + "','" + lname + "'," + age + ",'" + type + "'," + amount + "," + pnumber + ","
                    + pin + "," + accountNumber + ")";
            st.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static String validateName(Scanner sc) {
        String name = "";
        boolean valid = false;

        while (!valid) {
            // Take input from the user
            try {
                name = sc.next();
                if (!name.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException("Name must only contain alphabetic characters.");
                } else {
                    valid = true; // input is valid, exit loop
                }
            } catch (IllegalArgumentException e) {
                System.out.println("__________________________");
                System.out.println(e.getMessage());
                System.out.println("Please try again.");
                System.out.println("__________________________");
            }
        }

        return name;
    }

    private static int readAndValidateAge(Scanner sc) {
        int age = -1;
        boolean valid = false;

        while (!valid) {
            try {
                age = sc.nextInt();
                if (age <= 0) {

                    throw new IllegalArgumentException("Age must be a positive number.");
                } else if (age < 18) {
                    throw new IllegalArgumentException("Age must be at least 18.");
                }
                valid = true; // input is valid, exit loop
            } catch (InputMismatchException e) {
                sc.next(); // clear invalid input
                System.out.println("___________________________________________________");
                System.out.println("Invalid input. Please enter a valid number for age.");
                System.out.println("___________________________________________________");
            } catch (IllegalArgumentException e) {
                System.out.println("___________________________________");
                System.out.println(e.getMessage());
                System.out.println("___________________________________");
            }
        }

        return age;
    }

    public static String getValidAccountType(Scanner sc) {
        int typeChoice;
        String accountType = null;
        boolean isValid = false;

        do {
            System.out.println("___________________________________");
            System.out.println("Select your account type:");
            System.out.println("1. Savings");
            System.out.println("2. Current");
            System.out.println("___________________________________");
            try {
                typeChoice = sc.nextInt();
                switch (typeChoice) {
                    case 1:
                        accountType = "Savings";
                        isValid = true;
                        break;
                    case 2:
                        accountType = "Current";
                        isValid = true;
                        break;
                    default:
                        System.out.println("-------------------------------------------------------------------------");
                        System.out.println("Invalid account type choice. Please enter 1 for Savings or 2 for Current.");
                        System.out.println("-------------------------------------------------------------------------");
                        break;
                }
            } catch (InputMismatchException e) {
                sc.next(); // Clear invalid input
                System.out.println("_____________________________________");
                System.out.println("Invalid input. Please enter a number.");
                System.out.println("_____________________________________");

            }
        } while (!isValid);

        return accountType;
    }

    private static double getValidatedAmount(Scanner sc) {
        double amount = 0;
        boolean valid = false;

        while (!valid) {
            try {
                amount = sc.nextDouble();
                validateAmount(amount); // Call the validation method
                valid = true; // If no exception, input is valid, exit loop
            } catch (IllegalArgumentException e) {
                System.out.println("___________________________________");
                System.out.println(e.getMessage()); // Print the error message
                System.out.println("Please try again."); // Ask user to enter the amount again
                System.out.println("___________________________________");
            } catch (InputMismatchException e) {
                System.out.println("___________________________________");
                System.out.println("Invalid input. Please enter a numeric value.");
                System.out.println("___________________________________");
                sc.next(); // Clear the invalid input
            }
        }

        return amount;
    }

    private static void validateAmount(double amount) throws IllegalArgumentException {
        if (amount <= 5000 || amount >= 500000) {
            throw new IllegalArgumentException("Amount must be between 5000 and 500000.");
        }
    }

    public static String getValidPhoneNumber(Scanner sc) {
        String phoneNumber;
        boolean isValid = false;

        do {
            phoneNumber = sc.next();
            try {
                validatePhoneNumber(phoneNumber);
                isValid = true; // If no exception is thrown, the phone number is valid
            } catch (InvalidPhoneNumberException e) {
                System.out.println("___________________________________");
                System.out.println(e.getMessage());
                System.out.println("___________________________________");
            }
        } while (!isValid);

        return phoneNumber;
    }

    private static String getValidatedPhoneNumber(Scanner sc) {
        String phoneNumber = "";
        boolean valid = false;

        while (!valid) {
            try {
                phoneNumber = sc.next(); // Take input from the user
                validatePhoneNumber(phoneNumber); // Call the validation method
                valid = true; // If no exception, input is valid, exit loop
            } catch (InvalidPhoneNumberException e) {
                System.out.println("___________________________________");
                System.out.println(e.getMessage()); // Print the error message
                System.out.println("Please try again.");// Ask the user to enter the phone number again
                System.out.println("___________________________________");
            }
        }

        return phoneNumber;
    }

    private static void validatePhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if (!phoneNumber.matches("\\d{10}")) {
            throw new InvalidPhoneNumberException("Phone number must be exactly 10 digits.");
        }
    }

    private static int getValidatedPin(Scanner sc) {
        int pin = 0;
        boolean valid = false;

        while (!valid) {
            try {

                pin = sc.nextInt();

                // Check if the PIN is exactly 4 digits
                if (String.valueOf(pin).length() == 4) {
                    valid = true; // PIN is valid, exit loop
                } else {
                    System.out.println("-----------------------------------------------");

                    System.out.println("PIN must be exactly 4 digits. Please try again.");
                    System.out.println("-----------------------------------------------");

                }
            } catch (InputMismatchException e) {
                System.out.println("_____________________________________________");
                System.out.println("Invalid input. Please enter a 4-digit number.");
                System.out.println("_____________________________________________");
                sc.next(); // Clear invalid input from scanner
            }
        }

        return pin;
    }

    @Override
    public void login() {
        try {
            System.out.println();
            System.out.println("---------------------------------");
            System.out.println("Please enter your account number:");
            System.out.println("---------------------------------");
            int anumber = sc.nextInt();
            System.out.println("---------------------------------");

            System.out.println("Enter your pin:");
            System.out.println("---------------------------------");

            int pin1 = sc.nextInt();

            ResultSet rs = st.executeQuery("SELECT account_number, pin FROM bank_details");
            boolean loggedIn = false;
            while (rs.next()) {
                int a = rs.getInt(1);
                int pin2 = rs.getInt(2);
                if (a == anumber && pin1 == pin2) {
                    System.out.println("You logged in");
                    loggedIn = true;
                    break;
                }
            }

            if (loggedIn) {
                handleLoggedInUser(anumber);
            } else {
                System.out.println("Failed to log in");
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    private void handleLoggedInUser(int anumber) {
        try {
            int ch1 = 0;
            while (ch1 != 7) {
                System.out.println("______________________________________________");
                System.out.println("Please enter 1 for withdraw");
                System.out.println("Please enter 2 for deposit");
                System.out.println("Please enter 3 for transfer money");
                System.out.println("Please enter 4 to update any details");
                System.out.println("Please enter 5 to change pin");
                System.out.println("Please enter 6 to print all account activity");
                System.out.println("Enter 7 to exit");
                System.out.println("Please enter 1 for withdraw");
                ch1 = sc.nextInt();

                switch (ch1) {
                    case 1:
                        withdraw();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        transfer();
                        break;
                    case 4:
                        updateDetails();
                        break;
                    case 5:
                        changePin();
                        break;
                    case 6:
                        printAccountActivity();
                        break;
                    case 7:
                        System.out.println("_______________");
                        System.out.println("Exiting...");
                        System.out.println("_______________");
                        break;
                    default:
                        System.out.println("____________________");
                        System.out.println("Enter a valid choice");
                        System.out.println("____________________");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("___________________________________");
            System.out.println("Exception occurred: " + e.getMessage());
            System.out.println("___________________________________");
        }
    }

    @Override
    public void withdraw() {
        try {
            System.out.println("----------------------------------");
            System.out.println("Enter amount you want to withdraw:");
            System.out.println("----------------------------------");
            float am1 = sc.nextFloat();
            System.out.println("----------------------------------");

            System.out.println("Enter your account number:");
            System.out.println("----------------------------------");

            int anumber = sc.nextInt();
            String sq1 = "SELECT amount FROM bank_details WHERE account_number=" + anumber + ";";
            ResultSet rs1 = st.executeQuery(sq1);
            float am2 = 0;

            while (rs1.next()) {
                am2 = rs1.getInt(1);
            }

            if (am1 > am2) {
                System.out.println("------------------------------------------------------");
                System.out.println("Account " + anumber + " does not have that much money");
                System.out.println("------------------------------------------------------");

            } else {
                st.executeUpdate("UPDATE bank_details SET amount=" + (am2 - am1)
                        + " WHERE account_number=" + anumber + ";");
                System.out.println("Your current amount in account: " + (am2 - am1));
                writeTransactionToFile(anumber, "Withdraw: " + am1);
            }
        } catch (SQLException e) {
            System.out.println("_________________________________________");
            System.out.println("Error withdrawing money: " + e.getMessage());
            System.out.println("_________________________________________");
        }
    }

    @Override
    public void deposit() {
        try {
            System.out.println("----------------------------------");

            System.out.println("Enter amount you want to deposit:");
            System.out.println("----------------------------------");

            float am3 = sc.nextFloat();
            System.out.println("----------------------------------");

            System.out.println("Enter your account number:");
            System.out.println("----------------------------------");

            int anumber = sc.nextInt();
            String sql1 = "SELECT amount FROM bank_details WHERE account_number=" + anumber + ";";
            ResultSet rs2 = st.executeQuery(sql1);
            float amo = 0;

            while (rs2.next()) {
                amo = rs2.getInt(1);
            }

            st.executeUpdate("UPDATE bank_details SET amount=" + (am3 + amo)
                    + " WHERE account_number=" + anumber + ";");
            System.out.println("----------------------------------");
            System.out.println("Your current amount in account: " + (am3 + amo));
            System.out.println("----------------------------------");

            writeTransactionToFile(anumber, "Deposit: " + am3);
        } catch (SQLException e) {
            System.out.println("_________________________________________");
            System.out.println("Error depositing money: " + e.getMessage());
            System.out.println("_________________________________________");
        }
    }

    @Override
    public void transfer() {
        try {
            System.out.println("--------------------------------------------------------");
            System.out.println("Enter account number that you want to transfer money to:");
            System.out.println("--------------------------------------------------------");
            int transfer_account = sc.nextInt();
            int d1 = 0;
            int a1 = 0;

            ResultSet rs21 = st.executeQuery("SELECT account_number FROM bank_details");
            while (rs21.next()) {
                a1 = rs21.getInt(1);
                if (transfer_account == a1) {
                    d1 = 1;
                }
            }

            if (d1 == 1) {
                System.out.println("--------------------------------------------------------");

                System.out.println("Enter amount to transfer:");
                System.out.println("--------------------------------------------------------");

                float transfer_money = sc.nextFloat();
                System.out.println("--------------------------------------------------------");

                System.out.println("Enter your account number:");
                System.out.println("--------------------------------------------------------");

                int anumber = sc.nextInt();
                String sql = "SELECT amount FROM bank_details WHERE account_number=" + anumber + ";";
                ResultSet rs11 = st.executeQuery(sql);
                float exist_money = 0;
                while (rs11.next()) {
                    exist_money = rs11.getFloat(1);
                }

                if (exist_money >= transfer_money) {
                    st.executeUpdate(
                            "UPDATE bank_details SET amount=" + (exist_money - transfer_money)
                                    + " WHERE account_number=" + anumber + ";");
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Your current amount in account " + anumber + " is "
                            + (exist_money - transfer_money));
                    System.out.println("--------------------------------------------------------");

                    writeTransactionToFile(anumber,
                            "Transferred to account " + transfer_account + ": " + transfer_money);

                    String sql1 = "SELECT amount FROM bank_details WHERE account_number="
                            + transfer_account + ";";
                    ResultSet rs12 = st.executeQuery(sql1);
                    float exist_money1 = 0;
                    while (rs12.next()) {
                        exist_money1 = rs12.getFloat(1);
                    }

                    st.executeUpdate(
                            "UPDATE bank_details SET amount=" + (exist_money1 + transfer_money)
                                    + " WHERE account_number=" + transfer_account + ";");
                } else {
                    System.out.println("--------------------------------------------------------");

                    System.out.println("Account " + anumber + " does not have that much money");
                    System.out.println("--------------------------------------------------------");
                }
            } else {
                System.out.println("-----------------");
                System.out.println("Account not found");
                System.out.println("-----------------");

            }
        } catch (SQLException e) {
            System.out.println("___________________________________________");
            System.out.println("Error transferring money: " + e.getMessage());
            System.out.println("___________________________________________");
        }
    }

    @Override
    public void updateDetails() {
        try {
            System.out.println("_______________________________________________");

            System.out.println("You can only update name, age, and account type");
            System.out.println("_______________________________________________");
            System.out.println("----------------------------------------");

            System.out.println("Enter account number you want to change:");
            System.out.println("----------------------------------------");

            int id = sc.nextInt();
            System.out.println("---------------------");
            System.out.println("Enter new first name:");
            System.out.println("---------------------");

            String fn = sc.next();
            System.out.println("---------------------");
            System.out.println("Enter new last name:");
            System.out.println("---------------------");
            String ln = sc.next();
            System.out.println("---------------------");
            System.out.println("Enter new age:");
            System.out.println("---------------------");
            int age = sc.nextInt();
            System.out.println("---------------------");
            System.out.println("Enter new account type:");
            System.out.println("---------------------");
            String at = sc.next();

            String sql = "UPDATE bank_details SET f_name=?, l_name=?, age=?, account_type=? WHERE account_number=?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fn);
            pst.setString(2, ln);
            pst.setInt(3, age);
            pst.setString(4, at);
            pst.setInt(5, id);
            int r = pst.executeUpdate();
            if (r != 0) {
                System.out.println("---------------------");
                System.out.println("Update successful");
                System.out.println("---------------------");
            } else {
                System.out.println("---------------------");

                System.out.println("Update failed");
                System.out.println("---------------------");

            }
        } catch (SQLException e) {
            System.out.println("---------------------------------------");
            System.out.println("Error updating details: " + e.getMessage());
            System.out.println("---------------------------------------");
        }
    }

    @Override
    public void changePin() {
        try {
            System.out.println("---------------------");
            System.out.println("Enter existing pin:");
            System.out.println("---------------------");
            int epin = getValidatedPin(sc);
            System.out.println("---------------------");
            System.out.println("Enter new pin:");
            System.out.println("---------------------");
            int npin = getValidatedPin(sc);
            System.out.println("----------------------------");
            System.out.println("Re-enter new pin to confirm:");
            System.out.println("----------------------------");
            int cpin = getValidatedPin(sc);

            if (npin == cpin) {
                String sql = "UPDATE bank_details SET pin=? WHERE pin=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, npin);
                pst.setInt(2, epin);
                int r = pst.executeUpdate();
                if (r != 0) {
                    System.out.println("----------------------------");
                    System.out.println("Pin changed successfully");
                    System.out.println("----------------------------");

                } else {
                    System.out.println("----------------------------");
                    System.out.println("Something went wrong");
                    System.out.println("----------------------------");

                }
            } else {
                System.out.println("------------------------------------");
                System.out.println("New pin and confirm pin do not match");
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("_____________________________________");
            System.out.println("Error changing pin: " + e.getMessage());
            System.out.println("_____________________________________");
        }
    }

    @Override
    public void printAccountActivity() {
        System.out.println("_____________________________________");
        System.out.println("Account activity:");
        System.out.println("_____________________________________");
        try {
            System.out.println("------------------------------------");
            System.out.println("Enter your account number:");
            System.out.println("------------------------------------");
            int anumber = sc.nextInt();
            readTransactionsFromFile(anumber);
        } catch (Exception e) {
            System.out.println("_______________________________________________");
            System.out.println("Error reading account activity: " + e.getMessage());
            System.out.println("_______________________________________________");
        }

    }

    private static void writeTransactionToFile(int accountNumber, String transaction) {
        try (FileWriter writer = new FileWriter(accountNumber + "_transactions.txt", true)) {
            writer.write(transaction + "\n");
        } catch (IOException e) {
            System.out.println("_______________________________________________");
            System.out.println("An error occurred while writing to the file.");
            System.out.println("_______________________________________________");
            e.printStackTrace();
        }
    }

    private static void readTransactionsFromFile(int accountNumber) {
        try (FileReader reader = new FileReader(accountNumber + "_transactions.txt");
                BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("-------------------------");
                System.out.println(line);
                System.out.println("-------------------------");
            }
        } catch (IOException e) {
            System.out.println("_______________________________________________");
            System.out.println("An error occurred while reading the file.");
            System.out.println("_______________________________________________");
            e.printStackTrace();
        }
    }

}
