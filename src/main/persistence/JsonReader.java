package persistence;

import model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.stream.Stream;
import org.json.*;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads workroom from JSON data stored in file

public class JsonReader {
    private String hubSource;
    private String userSource;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String hs, String us) {
        this.hubSource = hs;
        this.userSource = us;

    }

    // EFFECTS: reads hub from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Hub read(Integer userID) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        String hubJsonData = readFile(hubSource);
        String userJsonData = readFile(userSource);

        JSONObject hubJsonObject = new JSONObject(hubJsonData);
        JSONObject userJsonObject = new JSONObject(userJsonData); // Parse user data JSON

        return parseHub(hubJsonObject, userJsonObject, userID);
    }

    // EFFECTS: returns the JSONObject corresponding to the string filepath
    public static JSONObject returnJsonObject(String s) throws IOException {
        String data = readFile(s);
        JSONObject j = new JSONObject(data);
        return j;
    }

    // EFFECTS: parses hub from JSON object and returns it
    private Hub parseHub(JSONObject jsonObject, JSONObject userJsonObject, Integer id) throws NoSuchPaddingException,
            NoSuchAlgorithmException {
        JSONObject hubObject = jsonObject.getJSONObject("Hub");
        Hub h = new Hub();
        addNote(h, hubObject);
        addReminder(h, hubObject);
        addContactList(h,hubObject);
        addMessageFolder(h, hubObject, userJsonObject);
        addNotifications(h,hubObject);
        return h;
    }

    // MODIFIES: h
    // EFFECTS: parses notifications from JSON object and adds them to hub
    private void addNotifications(Hub h, JSONObject hubObject) {
        JSONArray jsonArray = hubObject.getJSONArray("Notifications");
        for (Object json : jsonArray) {
            JSONObject nextNotif = (JSONObject) json;
            addSingleNotification(h, nextNotif);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses a single notification from JSON object and adds it to hub
    private void addSingleNotification(Hub h, JSONObject nextNotif) {
        String u = nextNotif.getString("UrgencyLevel");
        UrgencyLevel ul = UrgencyLevel.valueOf(u);
        String message = nextNotif.getString("Message");
        h.getNotifications().addNotification(ul,message);
    }

    // MODIFIES: h
    // EFFECTS: parses messages from JSON object and adds them to hub
    public void addMessageFolder(Hub h, JSONObject jsonObject, JSONObject userJsonObject)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException {
        JSONArray jsonArray = jsonObject.getJSONArray("MessageFolder");
        for (Object json : jsonArray) {
            JSONObject nextMessage = (JSONObject) json;
            addSingleMessage(h, nextMessage, userJsonObject);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses a single message from JSON object and adds them to hub
    public void addSingleMessage(Hub h, JSONObject jsonObject, JSONObject userJsonObject)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException {
        Integer senderUserID = jsonObject.getInt("SenderUserID");
        Integer recipientUserID = jsonObject.getInt("RecipientUserID");
        Integer messageID = jsonObject.getInt("MessageID");
        String decryptedMessageText = jsonObject.getString("DecryptedMessageText");
        String urgency = jsonObject.getString("Urgency Level");
        String secretKey = jsonObject.getString("SharedKey");
        User sender = getUserByID(senderUserID, userJsonObject);
        User recipient = getUserByID(recipientUserID, userJsonObject);

        Message m = new Message(sender,recipient,decryptedMessageText,UrgencyLevel.valueOf(urgency));
        SecretKey sk = stringToSecretKey(secretKey);
        m.setSharedKey(sk);
        h.getMessageFolder().addNewMessage(messageID,m);

    }

    //EFFECTS: converts the string secret key from the JSON object to a SecretKey type
    public SecretKey stringToSecretKey(String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodedKey, "DES");
    }

    //EFFECTS: fetches the user information matching the user ID from the JSON object
    public User getUserByID(Integer senderUserID, JSONObject userJsonObject) {
        User u = new User();
        JSONArray usersArray = userJsonObject.getJSONArray("Users");
        for (Object userObj : usersArray) {
            JSONObject userJson = (JSONObject) userObj;
            Integer userID = userJson.getInt("UserID");
            if (userID.equals(senderUserID)) {
                String username = userJson.getString("Username");
                String password = userJson.getString("Password");
                u.setUsername(username);
                u.setPassword(password);
                u.setUserID(userID);

            }
        }
        return u;

    }

    // MODIFIES: h
    // EFFECTS: parses contact list from JSON object and adds them to hub
    private void addContactList(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("ContactList");
        for (int i = 0; i < jsonArray.length(); i++) {
            String username = jsonArray.getString(i);
            h.getContactList().add(username);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses reminders from JSON object and adds them to hub
    private void addReminder(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Reminders");
        for (Object json : jsonArray) {
            JSONObject nextReminder = (JSONObject) json;
            addSingleReminder(h, nextReminder);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses a single reminder from JSON object and adds it to hub
    private void addSingleReminder(Hub h, JSONObject jsonObject) {
        String reminderDate = jsonObject.getString("Date");
        String reminderContent = jsonObject.getString("reminderContent");
        LocalDate date = LocalDate.parse(reminderDate);
        h.getReminder().addNewReminder(date,reminderContent);
    }

    // MODIFIES: h
    // EFFECTS: parses notes from JSON object and adds them to hub
    private void addNote(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Notes");
        for (Object json : jsonArray) {
            JSONObject nextNote = (JSONObject) json;
            addSingleNote(h, nextNote);
        }
    }

    // MODIFIES: hub
    // EFFECTS: parses a single note from JSON object and adds it to hub
    private void addSingleNote(Hub hub, JSONObject jsonObject) {
        Integer noteID = jsonObject.getInt("noteID");
        String noteContent = jsonObject.getString("noteContent");
        hub.getNote().addNote(noteID,noteContent);
    }

    // EFFECTS: reads source file as string and returns it
    public static String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
}
