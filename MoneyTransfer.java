package Bank;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MoneyTransfer {
    Connection connection;
    String userName = "";
    String userId = "";
    String receiverId = "";
    int transferAmount = 0;
    int amount = 0;
    int senderDeposit = 0;
    int receiverDeposit = 0;

    public void moneyTransfer() {
        System.out.println("================================>> MONEY TRANSFER <<================================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user name:");
        userName = scanner.nextLine();
        System.out.println("Enter your id:");
        userId = scanner.nextLine();
        System.out.println("Receiver account id:");
        receiverId = scanner.nextLine();
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connection = databaseConnection.database();
            Statement statement = connection.createStatement();
            getSenderDepositAmount(statement);
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter amount:");
            transferAmount = scan.nextInt();
            boolean amountCheck=amountCheck();
            if (amountCheck) {
                getReceiverDepositAmount(statement);
                System.out.println("0  -> OK");
                System.out.println("1  -> CANCEL");
                System.out.println("Enter the number:");
                int number = scanner.nextInt();
                if (number == 0) {
                    updateReceiverAmount();
                } else if (number == 1) {
                    System.out.println("Cancel...");
                }
            }else {
                System.out.println("Please check your deposit amount...");
                moneyTransfer();
            }
        } catch (InputMismatchException | SQLException e) {
            System.out.println("Please enter number...");
        }
    }

    private void getSenderDepositAmount(Statement statement) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.database();
        String getUserAmount = "select * from amount where user_id='" + userId + "'";
        ResultSet resultSet = statement.executeQuery(getUserAmount);
        if (resultSet.next()) {
            System.out.println("YOUR DEPOSIT AMOUNT   :  " + resultSet.getString("deposit"));
            String senderDepositAmount = resultSet.getString("deposit");
            senderDeposit = Integer.parseInt(senderDepositAmount);
        }
    }

    private void getReceiverDepositAmount(Statement statement) throws SQLException {
        String getReceiverAmount = "select * from amount where user_id='" + receiverId + "'";
        ResultSet receiverAmountResultSet = statement.executeQuery(getReceiverAmount);
        if (receiverAmountResultSet.next()) {
            receiverDeposit = Integer.parseInt(receiverAmountResultSet.getString("deposit"));
            amount = receiverDeposit + transferAmount;
        }
    }

    private void updateReceiverAmount() throws SQLException {
        String updateAmount = "update amount set deposit='" + amount + "' where user_id='" + receiverId + "'";
        PreparedStatement updateAmountStatement = connection.prepareStatement(updateAmount);
        updateAmountStatement.executeUpdate();
        updateSenderAmount();
    }

    private void updateSenderAmount() throws SQLException {
        int senderAmount = senderDeposit - transferAmount;
        String updateSenderAmount = "update amount set deposit='" + senderAmount + "' where user_id='" + userId + "'";
        PreparedStatement updateSenderAmountStatement = connection.prepareStatement(updateSenderAmount);
        updateSenderAmountStatement.executeUpdate();
        insertWithdrawDetails();
    }

    private void insertWithdrawDetails() throws SQLException {
        String insertAmount = "insert into withdraw (user_id,user_name,withdraw) values (?,?,?);";
        PreparedStatement insertAmountStatement = connection.prepareStatement(insertAmount);
        insertAmountStatement.setString(1, userId);
        insertAmountStatement.setString(2, userName);
        insertAmountStatement.setString(3, String.valueOf(transferAmount));
        insertAmountStatement.executeUpdate();
        System.out.println("Transfer successfully...");
    }

    private boolean amountCheck(){
        if (senderDeposit>=transferAmount){
            return true;
        }else {
            return false;
        }
    }
}
