package ui;

import model.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;



public class SecureMsgApp {
    private UserMap userMap;
    private User user;
    private Scanner input;

    public SecureMsgApp() {
        userMap = new UserMap();
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
                Hub userHub = user.getHub();
                displayHub(userHub);
                displayHubMenu();

            }


        }


    }

    private void displayHubMenu() {
        System.out.println("You can carry out the following tasks:");
        System.out.println("1. Add a new note\n2. Add a new reminder\n3.Add an existing user to your contact list");
        System.out.println("4. Send a message to an existing user\n5.quit");
        System.out.println("Choose an action (1-5):");
        Integer choice = input.nextInt();
        interpretChoice(choice);
    }

    private void interpretChoice(Integer choice) {
        switch (choice) {
            case 1:
                interpretChoiceOne();
                break;

            case 2:
                interpretChoiceTwo();
                break;

            case 5:
                System.exit(0);


        }
    }

    private void interpretChoiceOne() {
        System.out.println("You have chosen to add a new note.");
        Note userNotes = user.getHub().getNote();
        System.out.println("Enter an integer note ID: ");
        Integer noteID = input.nextInt();
        System.out.println("Enter the contents of your note: ");
        String noteText = input.next();
        userNotes.addNote(noteID,noteText);
        System.out.println("Note added!");

    }

    private void interpretChoiceTwo() {
        System.out.println("You have chosen to add a new reminder.");
        Reminder r = user.getHub().getReminder();
        System.out.println("Enter the date for your reminder in yyyy-mm-dd format: ");
        String date = input.next();
        System.out.println("Enter the contents of your reminder: ");
        String rtext = input.next();
        r.addNewReminder(LocalDate.parse(date),rtext);
        System.out.println("Reminder added!");
    }

    private void displayHub(Hub userHub) {
        displayHubNotes(userHub);
        displayHubReminders(userHub);
        displayHubContacts(userHub);
        displayHubMessageFolder(userHub);
        displayHubNotifications(userHub);
    }

    private void displayHubNotifications(Hub userHub) {
        System.out.println("Checking notifications...");
        Notification userNotif = userHub.getNotifications();
        if (userNotif.getNotifications().isEmpty()) {
            System.out.println("You have no new notifications");
        } else {
            System.out.println("ok");
        }
    }

    private void displayHubMessageFolder(Hub userHub) {
        System.out.println("Fetching your message folder...");
        MessageFolder userMsgFolder = userHub.getMessageFolder();
        if (userMsgFolder.getMessageFolder().isEmpty()) {
            System.out.println("You do not have any messages in your folder yet.");
        } else {
            System.out.println("wait");
        }
    }

    private void displayHubContacts(Hub userHub) {
        System.out.println("Fetching your contact list...");
        ArrayList<User> userContacts = userHub.getContactList();
        if (userContacts.isEmpty()) {
            System.out.println("You do not have any saved contacts.");
        } else {
            System.out.println(userContacts);
        }
    }

    private void displayHubReminders(Hub userHub) {
        System.out.println("Fetching reminders...");
        Reminder userReminders = userHub.getReminder();
        if (userReminders.getAllReminders().isEmpty()) {
            System.out.println("You do not have any saved reminders.");
        } else {
            System.out.println(userReminders.getAllReminders());
        }
    }


    private void displayHubNotes(Hub userHub) {
        System.out.println("Welcome to your personalized Hub!");
        System.out.println("Fetching notes...");
        Note userNotes = userHub.getNote();
        if (userNotes.getListOfNotes().isEmpty()) {
            System.out.println("You do not have any saved notes.");
        } else {
            System.out.println(userNotes.getListOfNotes());

        }

    }

    private void displayLoginPageForExistingUser() {
        System.out.println("Welcome back. Please enter your username.");
        String username = input.next();
        System.out.println("Now please enter your password.");
        String password = input.next();
        System.out.println("Please enter your unique user ID.");
        Integer id = input.nextInt();
        User userFromUserMap = userMap.getUser(id);
        Boolean successfulLogin = userFromUserMap.userLogIn(id,username,password);
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
        userMap.addUser(userID,user);


    }

}
