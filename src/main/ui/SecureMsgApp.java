package ui;

import model.*;


import java.util.Scanner;



public class SecureMsgApp {
    private User user;
    private Scanner input;

    public SecureMsgApp() {
        user = new User();
        runSecureMsgApp();
    }

    private void runSecureMsgApp() {
        boolean keepGoing = true;
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        while (keepGoing) {
            System.out.println("Welcome to your secure hub! Are you a new user? (yes/no)");
            String answer = input.next();
            answer = answer.toLowerCase();

            if (answer.equals("yes")) {
                displayLoginPageForNewUser();
            } else {
                displayLoginPageForExistingUser();
            }

            System.out.println("Would you like to continue to your hub or quit? (enter q to quit)");
            String ans = input.next();
            if (ans.equals("q")) {
                break;
            } else {
                System.out.println("Loading your hub...");
            }


        }


    }

    private void displayLoginPageForExistingUser() {
        System.out.println("Welcome back. Please enter your username.");
        String username = input.next();
        System.out.println("Now please enter your password.");
        String password = input.next();
        System.out.println("Please enter your unique user ID.");
        Integer id = input.nextInt();
        Boolean successfulLogin = user.userLogIn(id,username,password);
        if (successfulLogin) {
            System.out.println("Login successful! Welcome.");
        } else {
            System.out.println("Invalid Credentials. Please try again!");
        }
    }

    private void displayLoginPageForNewUser() {
        System.out.println("Hello, new User!");
        System.out.println("What should we call you?");
        String nameOfUser = input.next();
        System.out.println("Hello, " + nameOfUser + ". Go ahead and set your username.");
        String username = input.next();
        System.out.println("Go ahead and set your password.");
        String password = input.next();
        user.createNewUser(username,password);
        Integer userID = user.getUserID();
        System.out.println("Account successfully created! Welcome, "
                + nameOfUser + ". Your unique user ID is: " + userID);


    }

}
