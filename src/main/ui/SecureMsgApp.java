package ui;

import model.*;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

            hubOrQuit();

        }


    }

    private void hubOrQuit() {
        System.out.println("Would you like to continue to your hub or quit? (enter any key to continue, q to quit)");
        String ans = input.next();
        if (ans.equals("q")) {
            System.exit(0);
        } else {
            System.out.println("Loading your hub...");
            Hub userHub = user.getHub();
            displayHub(userHub);
            displayHubMenu();
        }


    }


    private void displayHubMenu() {
        System.out.println("You can carry out the following tasks:");
        System.out.println("1. Add a new note\n2. Add a new reminder\n3. Add an existing user to your emergency "
                +
                "contact list");
        System.out.println("4. Send a message to an existing user\n5. quit");
        System.out.println("Choose an action (1-5):");
        Integer choice = input.nextInt();
        interpretChoice(choice);
    }



    private void interpretChoice(Integer choice) {
        switch (choice) {
            case 1:
                interpretChoiceOne();
                hubOrQuit();


            case 2:
                interpretChoiceTwo();
                hubOrQuit();


            case 3:
                interpretChoiceThree();
                hubOrQuit();

            case 4:
                try {
                    interpretChoiceFour();
                } catch (NoSuchPaddingException e) {
                    System.err.println("Unexpected NoSuchPaddingException!");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("Unexpected NoSuchAlgorithmException!");
                }
                hubOrQuit();
            case 5:
                System.exit(0);


        }
    }

    private void interpretChoiceFour() throws NoSuchPaddingException, NoSuchAlgorithmException {
        System.out.println("You have chosen to send a message to an existing user.");
        User sender = this.user;
        System.out.println("Enter the user ID of the recipient.");
        Integer rid = input.nextInt();
        User recipient = userMap.getUser(rid);
        System.out.println("State the urgency level of this message (1. REGULAR, 2. URGENT, 3. EMERGENCY)");
        Integer ul = input.nextInt();
        UrgencyLevel msgUrgency = chooseUrgency(ul);
        System.out.println("Enter the contents of the message you'd like to send.");
        String msgContents = input.next();
        Integer messageID = 0;
        try {
            messageID = sender.getHub().sendMessage(sender,recipient,msgContents,msgUrgency);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unexpected UnsupportedEncodingException!");
        } catch (IllegalBlockSizeException e) {
            System.err.println("Unexpected IllegalBlockSizeException!");
        } catch (BadPaddingException e) {
            System.err.println("Unexpected BadPaddingException!");
        } catch (InvalidKeyException e) {
            System.err.println("Unexpected InvalidKeyException!");
        }

        System.out.println("Message sent! The message ID is: " + messageID);
    }

    private UrgencyLevel chooseUrgency(Integer ul) {
        UrgencyLevel u = null;
        switch (ul) {
            case 1: u = UrgencyLevel.REGULAR;
                    System.out.println("You chose the urgency level REGULAR.");
                    break;

            case 2: u = UrgencyLevel.URGENT;
                    System.out.println("You chose the urgency level URGENT.");
                    break;

            case 3: u = UrgencyLevel.EMERGENCY;
                    System.out.println("You chose the urgency level EMERGENCY.");
                    break;

            default:
                System.out.println("Sorry, that's an invalid entry.");
                break;

        }
        return u;
    }

    private void interpretChoiceThree() {
        System.out.println("You have chosen to add an existing user to your emergency contact list.");
        ArrayList<User> contactList = user.getHub().getContactList();
        System.out.println("Enter the user ID of the user you would like to add to your emergency contact list:");
        Integer id = input.nextInt();
        User userToAdd = userMap.getUser(id);
        contactList.add(userToAdd);
        System.out.println("Added!");
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
        HashMap userNotif = userHub.getNotifications().getHashMapOfNotifications();
        if (userNotif.isEmpty()) {
            System.out.println("You have no new notifications");
        } else {
            displayNotifications(userNotif);
        }
    }

    private void displayNotifications(HashMap h) {
        System.out.println("Notifications:");
        System.out.println(h);
    }

    private void displayHubMessageFolder(Hub userHub) {
        System.out.println("Fetching your message folder...");
        MessageFolder userMsgFolder = userHub.getMessageFolder();
        if (userMsgFolder.getMessageFolder().isEmpty()) {
            System.out.println("You do not have any messages in your folder yet.");
        } else {
            System.out.println("You have " + userMsgFolder.getMessageFolder().size() + " new messages!");
        }
    }

    private void displayMessage(Hub userHub, Integer i) {
        try {
            String decryptedMsg = userHub.receiveMessage(user,i);
            System.out.println(decryptedMsg);
        } catch (NoSuchPaddingException e) {
            System.err.println("Unexpected NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Unexpected NoSuchAlgorithmException");
        }


    }

    private void displayHubContacts(Hub userHub) {
        System.out.println("Fetching your emergency contact list...");
        ArrayList<User> userContacts = userHub.getContactList();
        if (userContacts.isEmpty()) {
            System.out.println("You do not have any saved contacts.");
        } else {
            for (User u : userContacts) {
                System.out.println("UserID: " + u.getUserID() + " UserName: " + u.getUsername());
            }
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
