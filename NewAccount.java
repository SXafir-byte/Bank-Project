import java.io.*;
import java.util.*;

/* 
 * Class for creating users' accounts and storing the 
 * information in the text files. 
 */


public class NewAccount {
    Scanner kb = new Scanner(System.in);
    private File accountFile = new File("AccountInfoFile.txt");//File names can be changed right here. 
    														   
    private File balanceSheet = new File("balanceSheet.txt");
    private static int nextAccountNumber = 100000;

    private String generatePassword() {
    	//Password randomizer for when the account is created
    	//"characters" stores most keyboard characters. 
    	//More ascii characters can be added for further security. 
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        String password = ""; 
        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * characters.length());
            password += characters.charAt(index); //adds a random character from the string, into the password. 
        }

        return password;
    }

    private boolean isDuplicate(String name, String dob) {
        try (Scanner reader = new Scanner(accountFile)) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(","); 
                if (data.length >= 3 && data[1].equals(name) && data[2].equals(dob)) {
                    return true; //return true if the method loaded values match the ones within the file. 
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file");
        }
        return false;
    }

    private void createAccount() {
        System.out.println("What is your full name:");
        String name = kb.nextLine();
        System.out.println("Date of birth (mm/dd/yyyy):");
        String dob = kb.nextLine();

        if (isDuplicate(name, dob)) { //check for duplicates before proceeding. 
            System.out.println("An account with that name and date of birth already exists.");
            return;
        }

        double balance;
        System.out.println("Would you like to make an initial deposit? (Y/N)");
        char choice = kb.nextLine().charAt(0);
        if (choice == 'Y' || choice == 'y') {
            System.out.println("What would be your initial balance:");
            balance = kb.nextDouble();
            kb.nextLine();
        } else {
            balance = 0.0;
            System.out.println("No initial balance will be deposited\n");
        }

        int accountNumber = nextAccountNumber++;//acc num incremented
        String password = generatePassword();
        AccessAccount newAcc = new AccessAccount(name, accountNumber, balance, password);

        try (PrintWriter writer = new PrintWriter(new FileWriter(accountFile, true))) {
        	//Writes all the information into the file with a specific format
        	//so that it can be later accessed more easily. Splitting with a comma
        	//helps to use the line split in other methods. 
            writer.println(accountNumber + "," + name + "," + dob + "," + balance + "," + password);
            System.out.println("Account created successfully!");
            System.out.println("Your account number is: " + accountNumber);
            System.out.println("Your password is: " + password + " \nKeep it safe!");
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }

    private void viewAccount() {
        System.out.print("Enter your full name: ");
        String inputName = kb.nextLine();
        System.out.print("Enter your password: ");
        String inputPassword = kb.nextLine();
        //Method for checking the account information 
        //and then performing withdrawal or deposit. 
        try (Scanner reader = new Scanner(accountFile)) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 5 && data[1].equals(inputName) && data[4].equals(inputPassword)) {
                    AccessAccount acc = new AccessAccount(data[1], Integer.parseInt(data[0]), Double.parseDouble(data[3]), data[4]);
                    System.out.println("\n=== Account Information ===");
                    acc.displayInfo();
                    System.out.println("===========================");
                    acc.performTransaction(kb, balanceSheet);
                    updateAccountFile(acc);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file.");
        }

        System.out.println("No account found with the given name and password.");
    }

    private void updateAccountFile(AccessAccount updatedAcc) {
        try { //File writing has to be done using the temporary file to safely 
        	  //record all the changes without a mix up. 
        	  //It took me a while, but I realized that a specific line cannot be
        	  //altered, and this is the best alternative. 
            File tempFile = new File("temp.txt");
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            Scanner reader = new Scanner(accountFile);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length >= 5 && data[0].equals(String.valueOf(updatedAcc.getAccountNumber()))) {
                    writer.println(updatedAcc.getAccountNumber() + "," + updatedAcc.getName() + "," +
                                   data[2] + "," + updatedAcc.getBalance() + "," + updatedAcc.getPassword());
               } else {
                    writer.println(line);
                }
            }

            reader.close();
            writer.close();

            if (accountFile.delete()) {
                tempFile.renameTo(accountFile); //tempFile will now be the accountFile. 
            }
        } catch (Exception e) {
            System.out.println("Failed to update accountInfo.txt.");
        }
    }

    private void initAccountNumber() {
        try (Scanner reader = new Scanner(accountFile)) {
            int max = 100000;
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 1) {
                    int num = Integer.parseInt(data[0]);
                    if (num > max) 
                    	max = num;
                    //Sets the max acc number to the current last number found in the file
                    //so that when an account is created, it uses the latest maximum. 
                }
            }
            nextAccountNumber = max + 1;
        } catch (Exception e) {
            System.out.println("There was a problem finding the account numbers!");
            //This message appears when first writing to an empty file. 
        }
    }

    public void openMenu() {
    	//Main method for accessing the information by the user about their account
    	//or creating an account. The aforecreated methods can only be accessed 
    	//by the user right here. 
        initAccountNumber();
        boolean runMenu = true;

        while (runMenu) {
            System.out.println("\n==== MAIN MENU ====");
            System.out.println("1. Create Account");
            System.out.println("2. View Account");
            System.out.println("3. Quit");
            System.out.print("Choose an option (1-3): ");

            int choice = kb.nextInt();
            kb.nextLine();

            switch (choice) {//switch case for the menu choices.
                case 1:
                    createAccount();
                    break;
                case 2:
                    viewAccount();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    runMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 3.");
            }
        }
    }
}

