package Bank;

import java.sql.*;
import java.util.Scanner;

public class Withdraw {
    Connection connection;
    String name = "";
    String userId = "";

    public void withdraw() {
        System.out.println("================================>> WITHDRAW <<================================");
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter user name:");
            name = scanner.nextLine();
            System.out.println("Enter your id:");
            userId = scanner.nextLine();
            boolean checkDetails = checkAccount();
            if (checkDetails) {
                String depositAmount = getDepositAmount();
                System.out.println("Enter withdraw amount:");
                String withdrawAmount = scanner.nextLine();
                getWithdrawAmount(withdrawAmount, depositAmount);
            } else {
                System.out.println("Please check your id...");
            }
        } catch (SQLException e) {
            System.out.println("Please check your name and account id...");
        }
    }

    private boolean checkAccount() throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.database();
        String getWithdrawDetails = "select * from amount where user_id='" + userId + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getWithdrawDetails);
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    private String getDepositAmount() throws SQLException {
        String deposit = "";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.database();
        String getWithdrawDetails = "select * from amount where user_id='" + userId + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getWithdrawDetails);
        if (resultSet.next()) {
            System.out.println("YOUR DEPOSIT AMOUNT   :  " + resultSet.getString("deposit"));
            deposit = resultSet.getString("deposit");
        }
        return deposit;
    }

    private void getWithdrawAmount(String withdrawAmount, String depositAmount) throws SQLException {
        if (Integer.parseInt(withdrawAmount) <= Integer.parseInt(depositAmount)) {
            int amount = Integer.parseInt(depositAmount) - Integer.parseInt(withdrawAmount);
            String updateDeposit = "update amount set deposit='" + amount + "' where user_id='" + userId + "'";
            PreparedStatement updateDepositStatement = connection.prepareStatement(updateDeposit);
            updateDepositStatement.executeUpdate();
            String insertAmount = "insert into withdraw (user_id,user_name,withdraw) values (?,?,?);";
            PreparedStatement insertAmountStatement = connection.prepareStatement(insertAmount);
            insertAmountStatement.setString(1, userId);
            insertAmountStatement.setString(2, name);
            insertAmountStatement.setString(3, withdrawAmount);
            insertAmountStatement.executeUpdate();
            System.out.println("Withdraw successfully...");
        }else{
            System.out.println("Check your deposit amount...");
            withdraw();
        }
    }
}
