package Bank;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NewAccount {
    Connection connection;
    String userName = "";
    String userDateOfBirth = "";
    String userPhoneNumber = "";
    String userGender = "";
    String userCountry = "";
    String userState = "";
    String userAadharNumber = "";

    public void newAccount() {
        System.out.println("================================>> NEW ACCOUNT <<================================");
        Scanner scanner = new Scanner(System.in);
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your name:");
        userName = scanner.nextLine();
        System.out.println("Enter your date of birth:");
        userDateOfBirth = scanner.nextLine();
        System.out.println("Enter the phone number:");
        long phoneNumber = scan.nextLong();
        userPhoneNumber = String.valueOf(phoneNumber);
        System.out.println("Gender:");
        userGender = scanner.nextLine();
        System.out.println("Enter country:");
        userCountry = scanner.nextLine();
        System.out.println("Enter state:");
        userState = scanner.nextLine();
        System.out.println("Enter your aadhar number:");
        long aadharNumber = scan.nextLong();
        userAadharNumber = String.valueOf(aadharNumber);
        try {
            System.out.println("0  -> OK");
            System.out.println("1  -> CANCEL");
            System.out.println("Enter the number:");
            int number = scan.nextInt();
            if (number == 0) {
                try {
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    connection = databaseConnection.database();
                    createNewAccount();
                    System.out.println("Enter your name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter your phone number:");
                    long phoneNo = scan.nextLong();
                    String getPhoneNumber = String.valueOf(phoneNo);
                    System.out.println("0  -> OK");
                    System.out.println("1  -> CANCEL");
                    System.out.println("Enter the number:");
                    int option = scan.nextInt();
                    if (option == 0) {
                        getAccountNumber(name, getPhoneNumber);
                    } else if (option == 1) {
                        System.out.println("Cancel...");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter number...");
                }
            } else if (number == 1) {
                System.out.println("Cancel...");
            }
        } catch (SQLException e) {
            System.out.println("Please check your details...");
        }

    }

    private void createNewAccount() throws SQLException {
        String insertUserDetails = "insert into new_account(user_name,date_of_birth,phone_no,gender,country,state," +
                "aadhar) values(?,?,?,?,?,?,?);";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.database();
        PreparedStatement preparedStatement = connection.prepareStatement(insertUserDetails);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, userDateOfBirth);
        preparedStatement.setString(3, userPhoneNumber);
        preparedStatement.setString(4, userGender);
        preparedStatement.setString(5, userCountry);
        preparedStatement.setString(6, userState);
        preparedStatement.setString(7, userAadharNumber);
        preparedStatement.executeUpdate();
        System.out.println("New account create successfully...");
    }

    private void getAccountNumber(String name, String getPhoneNumber) throws SQLException {
        String getUserDetails = "select id,user_name,phone_no from new_account where " +
                "user_name='" + name + "' and phone_no='" + getPhoneNumber + "'";
        PreparedStatement getUserDetailsStatement = connection.prepareStatement(getUserDetails);
        ResultSet resultSet = getUserDetailsStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("YOUR NAME           :  " + resultSet.getString("user_name"));
            System.out.println("YOUR ACCOUNT ID     :  " + resultSet.getString("id"));
            new UserAccountPage().userAccount();
        } else {
            System.out.println("Please check your name and phone number...");
        }
    }
}
