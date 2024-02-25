package ui;

import model.*;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//This class contains all the methods handling user interactions and input.

public class SecureMsgApp {
    private static final String JSON_HUB = "./data/hub.json";
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private UserMap userMap;
    private User user;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterUserInfo;



    //EFFECTS: constructs a new SecureMsgApp object
    public SecureMsgApp() {
        userMap = new UserMap();
        user = new User();
        jsonWriter = new JsonWriter(JSON_HUB);
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(JSON_HUB,JSON_USERINFO);
        runSecureMsgApp();
    }

    //EFFECTS: runs the application, handles the user log in
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
                hubOrQuit();
            } else {
                displayLoginPageForExistingUser();
                hubOrQuitForExistingUser();
            }




        }


    }

    private void hubOrQuit() {
        Hub userHub = user.getHub();
        displayHub(userHub);
        displayHubMenu();
    }

    //EFFECTS: displays the login page for a user who already has an existing account
    private void displayLoginPageForExistingUser() {
        System.out.println("Welcome back. Please enter your username.");
        String username = input.next();
        System.out.println("Now please enter your password.");
        String password = input.next();
        System.out.println("Please enter your unique user ID.");
        Integer id = input.nextInt();
        User userFromFile = readUserFromFile(id);
        this.user = userFromFile;
        Boolean successfulLogin = userFromFile.userLogIn(id, username, password);
        if (successfulLogin) {
            System.out.println("Login successful! Welcome.");
        } else {
            System.out.println("Invalid Credentials. Please try again!");
        }
    }

    private User readUserFromFile(Integer id) {
        try {
            JSONObject userJsonObject = jsonReader.returnJsonObject(JSON_USERINFO);
            User u = jsonReader.getUserByID(id, userJsonObject);
            return u;
        } catch (IOException e) {
            System.out.println("Error retrieving user from file: " + JSON_USERINFO);
        }
        return null;
    }



    //EFFECTS: displays the login page for a new user without an existing account
    private void displayLoginPageForNewUser() {
        System.out.println("Hello, new User!");
        System.out.println("What should we call you?");
        String nameOfUser = input.next();
        System.out.println("Hello, " + nameOfUser + ". Go ahead and set your username.");
        String username = input.next();
        System.out.println("Go ahead and set your password.");
        String password = input.next();
        user.createNewUser(username, password);
        Integer userID = user.getUserID();
        System.out.println("Account successfully created! Welcome, "
                + nameOfUser + ". Your unique user ID is: " + userID);
        userMap.addUser(userID, user);
        try {
            jsonWriterUserInfo.writeUser(user);
        } catch (FileNotFoundException e) {
            System.out.println("Unexpected FileNotFoundException!");
        }
    }





    //EFFECTS: asks the user if they would like to view their hub or quit the application
    private void hubOrQuitForExistingUser() {
        System.out.println("Would you like to load your saved hub from file or quit? "
                +
                "(enter l to continue, q to quit)");
        String ans = input.next();
        if (ans.equals("q")) {
            System.exit(0);
        } else if (ans.equals("l")) {
            System.out.println("Loading your hub...");
            try {
                loadHubFromFile();
            } catch (NoSuchPaddingException e) {
                System.out.println("Unexpected NoSuchPaddingException!");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Unexpected NoSuchPaddingException!");
            }
            Hub userHub = user.getHub();
            displayHub(userHub);
            displayHubMenu();
        }


    }

    private void loadHubFromFile() throws NoSuchPaddingException, NoSuchAlgorithmException {
        try {
            user.setHub(jsonReader.read());
            System.out.println("Loaded " + user.getUsername() + "'s Hub from" + JSON_HUB);
        } catch (IOException e) {
            System.out.println("Error! Unable to read from file: " + JSON_HUB);
        }
    }


    //EFFECTS: displays all the tasks the user can carry out in the hub
    private void displayHubMenu() {
        System.out.println("You can carry out the following tasks:");
        System.out.println("1. Add a new note\n2. Add a new reminder\n3. Add an existing user to your emergency "
                +
                "contact list");
        System.out.println("4. Send a message to an existing user\n5. Save hub to file\n6. Quit");
        System.out.println("Choose an action (1-6):");
        Integer choice = input.nextInt();
        interpretChoice(choice);
    }


    //EFFECTS: interprets the user's choice based on which action he would like to carry out
    private void interpretChoice(Integer choice) {
        switch (choice) {
            case 1:
                interpretChoiceOne();


            case 2:
                interpretChoiceTwo();


            case 3:
                interpretChoiceThree();

            case 4:
                try {
                    interpretChoiceFour();
                } catch (NoSuchPaddingException e) {
                    System.err.println("Unexpected NoSuchPaddingException!");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("Unexpected NoSuchAlgorithmException!");
                }
                hubOrQuitForExistingUser();

            case 5:
                interpretChoiceFive();
            case 6:
                System.exit(0);


        }
    }

    private void interpretChoiceFive() {
        try {
            if (user != null && user.getUsername() != null) {
                jsonWriter.open();
                jsonWriter.writeHub(user.getUsername(), user.getHub());
                jsonWriter.close();
                System.out.println("Saved " + user.getUsername() + "'s Hub to" + JSON_HUB);
            } else {
                System.out.println("User is not properly initialized or username is null.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unexpected FileNotFoundException!");
        } catch (IOException e) {
            System.out.println("Unexpected IOException!");
        }
    }


    //EFFECTS: sends an encrypted message of a certain urgency level and content to the specified recipient

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
            messageID = sender.getHub().sendMessage(sender, recipient, msgContents, msgUrgency);
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


    //EFFECTS: returns the urgency level corresponding to the user inputted integer
    private UrgencyLevel chooseUrgency(Integer ul) {
        UrgencyLevel u = null;
        switch (ul) {
            case 1:
                u = UrgencyLevel.REGULAR;
                System.out.println("You chose the urgency level REGULAR.");
                break;

            case 2:
                u = UrgencyLevel.URGENT;
                System.out.println("You chose the urgency level URGENT.");
                break;

            case 3:
                u = UrgencyLevel.EMERGENCY;
                System.out.println("You chose the urgency level EMERGENCY.");
                break;

            default:
                System.out.println("Sorry, that's an invalid entry.");
                break;

        }
        return u;
    }


    //EFFECTS: adds a user to the user's emergency contact list
    private void interpretChoiceThree() {
        System.out.println("You have chosen to add an existing user to your emergency contact list.");
        ArrayList<String> contactList = user.getHub().getContactList();
        System.out.println("Enter the user ID of the user you would like to add to your emergency contact list:");
        Integer id = input.nextInt();
        User userToAdd = userMap.getUser(id);
        contactList.add(userToAdd.getUsername());
        System.out.println("Added!");
        displayHubMenu();
    }


    //EFFECTS: adds a new note with a note ID to the user's notes
    private void interpretChoiceOne() {
        System.out.println("You have chosen to add a new note.");
        Note userNotes = user.getHub().getNote();
        System.out.println("Enter an integer note ID: ");
        Integer noteID = input.nextInt();
        System.out.println("Enter the contents of your note: ");
        String noteText = input.next();
        userNotes.addNote(noteID, noteText);
        System.out.println("Note added!");
        displayHubMenu();

    }


    //EFFECTS: adds a new reminder with a date to the user's reminders
    private void interpretChoiceTwo() {
        System.out.println("You have chosen to add a new reminder.");
        Reminder r = user.getHub().getReminder();
        System.out.println("Enter the date for your reminder in yyyy-mm-dd format: ");
        String date = input.next();
        System.out.println("Enter the contents of your reminder: ");
        String rtext = input.next();
        r.addNewReminder(LocalDate.parse(date), rtext);
        System.out.println("Reminder added!");
        displayHubMenu();
    }


    //EFFECTS: displays all the parts of the user's hub - notes, reminders, contacts, message folder and notifications
    private void displayHub(Hub userHub) {
        displayHubNotes(userHub);
        displayHubReminders(userHub);
        displayHubContacts(userHub);
        displayHubMessageFolder(userHub);
        displayHubNotifications(userHub);
    }


    //EFFECTS: displays the user's notifications, printing a message if there are none
    private void displayHubNotifications(Hub userHub) {
        System.out.println("Checking notifications...");
        HashMap userNotif = userHub.getNotifications().getHashMapOfNotifications();
        if (userNotif.isEmpty()) {
            System.out.println("You have no new notifications");
        } else {
            displayNotifications(userNotif);
        }
    }

    //EFFECTS: displays the hashmap containing the notifications
    private void displayNotifications(HashMap h) {
        System.out.println("Notifications:");
        System.out.println(h);
    }


    //EFFECTS: displays the user's message folder, printing a message if there are no messages
    private void displayHubMessageFolder(Hub userHub) {
        System.out.println("Fetching your message folder...");
        MessageFolder userMsgFolder = userHub.getMessageFolder();
        if (userMsgFolder.getMessageFolder().isEmpty()) {
            System.out.println("You do not have any messages in your folder yet.");
        } else {
            System.out.println("You have " + userMsgFolder.getMessageFolder().size() + " new messages!");
        }
    }


    //EFFECTS: displays the message corresponding to the message ID.
    private void displayMessage(Hub userHub, Integer i) {
        try {
            String decryptedMsg = userHub.receiveMessage(user, i);
            System.out.println(decryptedMsg);
        } catch (NoSuchPaddingException e) {
            System.err.println("Unexpected NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Unexpected NoSuchAlgorithmException");
        }


    }


    //EFFECTS: displays the user's emergency contact list, printing a special message if they don't have any
    private void displayHubContacts(Hub userHub) {
        System.out.println("Fetching your emergency contact list...");
        ArrayList<String> userContacts = userHub.getContactList();
        if (userContacts.isEmpty()) {
            System.out.println("You do not have any saved contacts.");
        } else {
            for (String u : userContacts) {
                System.out.println("UserName: " + u);
            }
        }
    }

    //EFFECTS: displays the user's reminders, printing a special message if they don't have any
    private void displayHubReminders(Hub userHub) {
        System.out.println("Fetching reminders...");
        Reminder userReminders = userHub.getReminder();
        if (userReminders.getAllReminders().isEmpty()) {
            System.out.println("You do not have any saved reminders.");
        } else {
            for (HashMap.Entry<LocalDate, String> e : userReminders.getAllReminders().entrySet()) {
                LocalDate k = e.getKey();
                String v = e.getValue();
                System.out.println("Date: " + k + " Reminder message: " + v);

            }

        }
    }


    //EFFECTS: displays the user's notes, printing a special message if they don't have any
    private void displayHubNotes(Hub userHub) {
        System.out.println("Welcome to your personalized Hub!");
        System.out.println("Fetching notes...");
        Note userNotes = userHub.getNote();
        if (userNotes.getListOfNotes().isEmpty()) {
            System.out.println("You do not have any saved notes.");
        } else {
            for (HashMap.Entry<Integer, String> e : userNotes.getListOfNotes().entrySet()) {
                Integer k = e.getKey();
                String v = e.getValue();
                System.out.println("Note ID: " + k + " Note contents: " + v);

            }

        }
    }
}







