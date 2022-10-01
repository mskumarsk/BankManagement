package Bank;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.IllegalFormatCodePointException;
import java.util.Scanner;

public class AdminMainPage extends AdminLogin {
    Scanner scanner = new Scanner(System.in);

    public void adminPage() {
        while (true) {
            System.out.println("================================>> ADMIN PAGE  <<================================");
            System.out.println("================================>> LOGIN PAGE  <<================================");
            System.out.println("Enter the admin name:");
            String adminName = scanner.nextLine();
            boolean nameCheck = checkAdminName(adminName);
            if (nameCheck) {
                System.out.println("Enter the password:");
                String adminPassword = scanner.nextLine();
                boolean passwordCheck = checkAdminPassword(adminPassword);
                if (passwordCheck) {
                    new AdminPage().adminPage();
                }
            }
        }
    }

    private boolean checkAdminName(String adminName) {
        if (adminName.equals(getAdminName())) {
            return true;
        } else {
            System.out.println("Admin name is wrong...");
            return false;
        }
    }

    private boolean checkAdminPassword(String adminPassword) {
        if (adminPassword.equals(getAdminPassword())) {
            return true;
        } else {
            System.out.println("Incorrect Password");
            return false;
        }
    }
}
