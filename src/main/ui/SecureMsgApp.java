package ui;

import model.*;
import org.json.JSONArray;
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
    private String pathForSpecificUser;
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
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
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

    //EFFECTS: runs the methods corresponding to displaying the information in the Hub, and the menu
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
            pathForSpecificUser = "./data/" + username + ".json";
        } else {
            System.out.println("Invalid Credentials. Please try again!");
            runSecureMsgApp();
        }
    }

    //EFFECTS: returns the user corresponding to the given UserID from the JSON_USERINFO file
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
        pathForSpecificUser = "./data/" + username + ".json";
        userMap.addUser(userID, user);
        addNewUserToFile(user);
    }

    //EFFECTS: Adds a new user to the JSON_USERINFO file
    private void addNewUserToFile(User u) {
        try {
            jsonWriterUserInfo.openInAppendMode();
            jsonWriterUserInfo.writeUser(user);
            jsonWriterUserInfo.close();

        } catch (IOException e) {
            System.out.println("Unexpected FileNotFoundException!");
        }

    }






    //EFFECTS: asks the user if they would like to view their hub or quit the application
    private void hubOrQuitForExistingUser() {
        System.out.println("Would you like to load your saved hub from file, create a new hub, or quit?");
        System.out.println("Enter 'l' to load\nEnter 'n' to create a new hub\nEnter 'q' to quit");

        String ans = input.next();
        if (ans.equals("q")) {
            System.exit(0);
        } else if (ans.equals("l")) {
            System.out.println("Loading your hub...");
            try {
                loadHubFromFile(user.getUsername());
            } catch (NoSuchPaddingException e) {
                System.out.println("Unexpected NoSuchPaddingException!");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Unexpected NoSuchPaddingException!");
            }
            Hub userHub = user.getHub();
            displayHub(userHub);
            displayHubMenu();
        } else if (ans.equals("n")) {
            Hub userHub = new Hub();
            user.setHub(userHub);
            displayHub(userHub);
            displayHubMenu();
        }


    }

    //EFFECTS: loads the Hub corresponding to the specific user's username from their specific hub file, throws
    //IOException if error occurs in loading hub
    private void loadHubFromFile(String username) throws NoSuchPaddingException, NoSuchAlgorithmException {
        try {
            pathForSpecificUser = "./data/" + username + ".json";
            jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
            user.setHub(jsonReader.read(user.getUserID()));
            System.out.println("Loaded " + user.getUsername() + "'s Hub from" + pathForSpecificUser);
        } catch (IOException e) {
            System.out.println("Error! Unable to read from file: " + pathForSpecificUser);
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
                System.exit(0);
            case 6:
                interpretChoiceSix();
                System.exit(0);

        }
    }

    //EFFECTS: Gives the option to the user to either quit without saving, or to save their progress before quitting
    private void interpretChoiceSix() {
        System.out.println("Are you sure you want to quit? Your hub is not saved!");
        System.out.println("Enter 'q' if you want to quit without saving");
        System.out.println("Enter 's' if you want to save your hub before quitting");
        String ans = input.next();
        if (ans.equals("q")) {
            System.exit(0);
        } else if (ans.equals("s")) {
            interpretChoiceFive();
        } else {
            System.out.println("Invalid choice! Quitting...");
        }
    }

    //EFFECTS: Saves the user's hub to the file, saving any changes made. Throws FileNotFoundException if file not
    //found, otherwise IOException thrown if error occurs in writing
    private void interpretChoiceFive() {
        try {
            if (user != null && user.getUsername() != null) {
                jsonWriter = new JsonWriter(pathForSpecificUser);
                jsonWriter.open();
                jsonWriter.writeHub(user.getUsername(), user.getUserID().toString(), user.getHub());
                jsonWriter.close();
                System.out.println("Saved " + user.getUsername() + "'s Hub to" + pathForSpecificUser);
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
        User recipient = readUserFromFile(rid);
        String recipientFilePath = "./data/" + recipient.getUsername() + ".json";
        loadRecipientHub(recipient,recipientFilePath);
        System.out.println("You have chosen to send a message to: " + recipient.getUsername() + ".");
        System.out.println("State the urgency level of this message (1. REGULAR, 2. URGENT, 3. EMERGENCY)");
        Integer ul = input.nextInt();
        UrgencyLevel msgUrgency = chooseUrgency(ul);
        System.out.println("Enter the contents of the message you'd like to send.");
        String msgContents = input.next();
        Integer messageID = 0;
        sendMessageToRecipient(messageID, sender, recipient, msgContents, msgUrgency);
        addMessageToRecipientJson(recipient, messageID, sender, msgContents, msgUrgency, recipientFilePath);
        displayHubMenu();

    }

    //EFFECTS: adds the received message to the recipient's JSON file, throws IOException if error occurs in
    //reading the file
    private void addMessageToRecipientJson(User recipient,Integer messageID,
                                           User sender, String msgContents, UrgencyLevel msgUrgency, String fp) {
        jsonReader = new JsonReader(fp,JSON_USERINFO);
        try {
            JSONObject hubJson = jsonReader.returnJsonObject(pathForSpecificUser);
            JSONArray messageFolderArray = hubJson.getJSONObject("Hub").getJSONArray("MessageFolder");
            JSONObject msg = new JSONObject();
            msg.put("SenderUserID",sender.getUserID());
            msg.put("RecipientUserID",recipient.getUserID());
            msg.put("MessageID",messageID);
            msg.put("DecryptedMessageText",msgContents);
            msg.put("Urgency Level",msgUrgency.toString());
            messageFolderArray.put(msg);
            hubJson.getJSONObject("Hub").put("MessageFolder", messageFolderArray);

            jsonWriter = new JsonWriter(fp);
            jsonWriter.open();
            jsonWriter.writeHub(recipient.getUsername(), recipient.getUserID().toString(), recipient.getHub());
            jsonWriter.close();

        } catch (IOException e) {
            System.out.println("Error! Unable to write to recipient file");
        }


    }

    //EFFECTS: loads the recipient's hub from their specific hub file, throws IOException if error occurs in
    //reading the file
    private void loadRecipientHub(User recipient, String fp) throws NoSuchPaddingException, NoSuchAlgorithmException {
        try {
            jsonReader = new JsonReader(fp,JSON_USERINFO);
            Hub h = new Hub();
            h = jsonReader.read(user.getUserID());
            recipient.setHub(h);
            System.out.println("Loaded " + recipient.getUsername() + "'s Hub from" + pathForSpecificUser);
        } catch (IOException e) {
            System.out.println("Error! Unable to read from file: " + pathForSpecificUser);
        }

    }

    //EFFECTS: sends the message from the sender's hub to the recipient, printing appropriate messages if either hubs
    //are null. Prints out the messageID once sent.
    private void sendMessageToRecipient(Integer messageID, User sender, User recipient, String m, UrgencyLevel u) {
        if (sender.getHub() == null) {
            System.err.println("Sender or sender's hub is not properly initialized.");
        }

        if (recipient.getHub() == null) {
            System.err.println("Recipient user is null.");
        }

        try {
            messageID = sender.getHub().sendMessage(sender, recipient, m, u);
            System.out.println("Message sent! The message ID is: " + messageID);
            this.user = sender;
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unexpected UnsupportedEncodingException!");
        } catch (IllegalBlockSizeException e) {
            System.err.println("Unexpected IllegalBlockSizeException!");
        } catch (BadPaddingException e) {
            System.err.println("Unexpected BadPaddingException!");
        } catch (InvalidKeyException e) {
            System.err.println("Unexpected InvalidKeyException!");
        } catch (NoSuchPaddingException e) {
            System.err.println("Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Unexpected NoSuchAlgorithmException!");
        }
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


    //EFFECTS: adds a user to the user's emergency contact list, throws IOException if error occurs in
    //reading the file
    private void interpretChoiceThree() {
        System.out.println("You have chosen to add an existing user to your emergency contact list.");
        ArrayList<String> contactList = user.getHub().getContactList();
        System.out.println("Enter the user ID of the user you would like to add to your emergency contact list:");
        Integer id = input.nextInt();
        try {
            JSONObject uj = jsonReader.returnJsonObject(JSON_USERINFO);
            User userToAdd = jsonReader.getUserByID(id,uj);
            contactList.add(userToAdd.getUsername());
            System.out.println("Added!");
            displayHubMenu();
        } catch (IOException e) {
            System.out.println("Error in finding the user!");
        }

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
        displayHubNotifications(userHub);
        displayHubMessageFolder(userHub);

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
            System.out.println("Enter 'v' to view your messages now, or 'c' to continue to your actions.");
            String ans = input.next();
            if (ans.equals("v")) {
                displayMessages("./data/" + user.getUsername() + ".json");
            } else if (ans.equals("c")) {
                //pass
            }
        }
    }

    //EFFECTS: displays the user's individual messages from their message folder, throws IOException if error occurs in
    //reading the file
    private void displayMessages(String filepath) {
        JsonReader reader = new JsonReader("./data/" + user.getUsername() + ".json",JSON_USERINFO);
        try {
            JSONObject j = reader.returnJsonObject(filepath);
            JSONObject userJsonObject = reader.returnJsonObject(JSON_USERINFO);
            JSONArray messageFolder = j.getJSONObject("Hub").getJSONArray("MessageFolder");
            for (int i = 0; i < messageFolder.length(); i++) {
                JSONObject messageObj = messageFolder.getJSONObject(i);
                int messageID = messageObj.getInt("MessageID");
                String decryptedMessageText = messageObj.getString("DecryptedMessageText");
                UrgencyLevel urgencyLevel = UrgencyLevel.valueOf(messageObj.getString("Urgency Level"));
                User sender = reader.getUserByID(messageObj.getInt("SenderUserID"),userJsonObject);
                User recipient = reader.getUserByID(messageObj.getInt("RecipientUserID"),userJsonObject);
                System.out.println("Message ID: " + messageID);
                System.out.println("Sender: " + sender.getUsername());
                System.out.println("Recipient: " + recipient.getUsername());
                System.out.println("Urgency Level: " + urgencyLevel.toString());
                System.out.println("Message content: " + decryptedMessageText);
                System.out.println();

            }
        } catch (IOException e) {
            System.out.println("Error fetching messages!");
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







