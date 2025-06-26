/*
 * Parent class for storing protected object
 * values which can be accessed through 
 * child classes using designated methods.  
 */
public class Account {
    protected String name;
    protected int accountNumber;
    protected double balance;

    public Account(String name, int accountNumber, double balance) {//constructor with the necessary information
    																//balance is not initially needed, so it is always
    																//initialized with at least 0. 
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void displayInfo() {//display user's information 
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Balance: $" + balance);
    }

    public void deposit(double amount) { //simple method to deposit double amount
        balance += amount;
    }

    public void withdraw(double amount) {//withdraw a double amount.
        balance -= amount;
    }

    public double getBalance() {//return the current balance
        return balance;
    }

    public int getAccountNumber() {//return account's number
        return accountNumber;
    }

    public String getName() {//return user's full name
        return name;
    }
}