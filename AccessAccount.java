import java.io.*;
import java.util.*;

public class AccessAccount extends Account {
    private String password;//password is most important for accessing an account. 

    public AccessAccount(String name, int accountNumber, double balance, String password) {
        super(name, accountNumber, balance);
        this.password = password;
    }

   
    public void deposit(double amount) {
        if (amount <= 0) { //Checks for negative case. 
            System.out.println("Deposit amount must be positive.");
        } else {
            super.deposit(amount);//update the balance with the given value.
            System.out.println("Deposited: $" + amount);
        }
    }

    
    public void withdraw(double amount) {
        if (amount > balance) { //checks if the user tries to withdraw more than they have
            System.out.println("Insufficient funds.");
        } else {
            super.withdraw(amount);//update the balance. 
            System.out.println("Withdrew: $" + amount);
        }
    }

    public String getPassword() {
        return password;
    }

    public void performTransaction(Scanner kb, File balanceFile) {
    	//Transactions will only be accesible after the user enters their account with name and pass. 
        System.out.println("Do you want to deposit or withdraw? (D/W/None)");
        String choice = kb.nextLine();

        if (choice.equals("D") || choice.equals("d")) {
            System.out.println("Enter deposit amount:");
            double amount = kb.nextDouble();
            kb.nextLine();
            deposit(amount);
            recordTransaction("Deposit", amount, balanceFile);
        } else if (choice.equals("W") || choice.equals("w")) {
            System.out.println("Enter withdrawal amount:");
            double amount = kb.nextDouble();
            kb.nextLine();
            withdraw(amount);
            recordTransaction("Withdraw", amount, balanceFile);
        } else {
            System.out.println("Returning to menu...");
        }
    }

    private void recordTransaction(String type, double amount, File balanceFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(balanceFile, true))) {
            writer.println(accountNumber + "," + name + "," + type + "," + amount + "," + balance);
        } catch (Exception e) {
            System.out.println("Failed to record transaction.");
        } //This methods records all the transactions performed within the accessed account. 
          //The info is written into a separate file for transaction records only. (balanceSheet)
    }
}