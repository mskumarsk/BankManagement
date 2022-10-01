package Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AccountBlock {
    Connection connection;

    public void accountBlock() {
        System.out.println("================================>> ACCOUNT BLOCK  <<================================");
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter customer account number:");
            int accountNumber = scanner.nextInt();
            accountDelete(accountNumber);
        } catch (InputMismatchException e) {
            System.out.println("Please check your account number...");
        }
    }

    private void accountDelete(int accountNumber) {
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            connection = databaseConnection.database();
            String accountDelete = "delete from new_account where id='" + accountNumber + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(accountDelete);
            preparedStatement.executeUpdate();
            System.out.println("Account delete successfully...");
        } catch (SQLException e) {
            System.out.println("Please check your account number...");
        }
    }
}
