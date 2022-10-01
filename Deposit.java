package Bank;

import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Deposit {
    Connection connection;
    String name = "";
    String userId = "";
    String depositAmount = "";
    String accountBalance = "";
    int amount = 0;

    public void deposit() {
        System.out.println("================================>> DEPOSIT <<================================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user name:");
        name = scanner.nextLine();
        System.out.println("Enter your id:");
        userId = scanner.nextLine();
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connection = databaseConnection.database();
            Statement statement = connection.createStatement();
            boolean accountCheck = accountCheck(statement);
            if (accountCheck) {
                try {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("Enter deposit amount:");
                    int amount = scan.nextInt();
                    depositAmount = String.valueOf(amount);
                    System.out.println("0  -> OK");
                    System.out.println("1  -> CANCEL");
                    System.out.println("Enter the number:");
                    int number = scanner.nextInt();
                    if (number == 0) {
                        insertDepositAmount(statement);
                    } else if (number == 1) {
                        System.out.println("Cancel...");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Please enter number...");
                }
            }
        } catch (SQLException e) {
            System.out.println("Please check your user name and password...");
        }

    }

    private boolean accountCheck(Statement statement) throws SQLException {
        String getDepositAmount = "select * from new_account where user_name='" + name + "' and id='" + userId + "'";
        ResultSet resultSet = statement.executeQuery(getDepositAmount);
        if (resultSet.next()) {
            return true;
        } else {
            System.out.println("Please check your details...");
            return false;
        }
    }

    private void insertDepositAmount(Statement statement) throws SQLException {
        String insertDepositAmount = "insert into deposit (user_id,user_name,deposit) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDepositAmount);
        preparedStatement.setString(1, userId);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, depositAmount);
        preparedStatement.executeUpdate();
        System.out.println("Deposit successfully...");
        getDepositAmount(statement);
    }

    private void getDepositAmount(Statement statement) throws SQLException {
        String getUserAmount = "select * from amount where user_id='" + userId + "'";
        ResultSet userAmountResultSet = statement.executeQuery(getUserAmount);
        if (userAmountResultSet.next()) {
            accountBalance = userAmountResultSet.getString("deposit");
            amount = Integer.parseInt(accountBalance);
            updateDepositAmount();
        } else {
            insertInitialAmount();
        }
    }

    private void updateDepositAmount() throws SQLException {
        int totalAmount = amount + Integer.parseInt(depositAmount);
        String updateDeposit = "update amount set deposit='" + totalAmount + "' where user_id='" + userId + "'";
        PreparedStatement updateDepositStatement = connection.prepareStatement(updateDeposit);
        updateDepositStatement.executeUpdate();
    }

    private void insertInitialAmount() throws SQLException {
        String insertAmount = "insert into amount (user_id,user_name,deposit) values (?,?,?);";
        PreparedStatement insertAmountStatement = connection.prepareStatement(insertAmount);
        insertAmountStatement.setString(1, userId);
        insertAmountStatement.setString(2, name);
        insertAmountStatement.setString(3, depositAmount);
        insertAmountStatement.executeUpdate();
    }
}
