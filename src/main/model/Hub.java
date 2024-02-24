package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
//This class represents a hub. Every user will have their own individualised hub, with personalized
//information like notes, reminders, messages, contact list, and so on. Every user will be able to access their hub
//once they log in.

public class Hub implements Writable {
    private Note note;
    private Reminder reminder;
    private ArrayList<String> contactList;
    private MessageFolder messageFolder;
    private Notification notifications;

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: constructs a new hub with notes, reminders, contact lists, a message folder and notifications
    public Hub() {
        this.note = new Note();
        this.reminder = new Reminder();
        this.contactList = new ArrayList<String>();
        this.messageFolder = new MessageFolder();
        this.notifications = new Notification();
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the notes the user may have saved in their hub
    public Note getNote() {
        return this.note;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the reminders the user may have saved in their hub
    public Reminder getReminder() {
        return this.reminder;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the user's list of emergency contacts
    public ArrayList getContactList() {
        return this.contactList;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the user's notifications
    public Notification getNotifications() {
        return this.notifications;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the user's message folder
    public MessageFolder getMessageFolder() {
        return this.messageFolder;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: sends an encrypted message to the recipient
    public Integer sendMessage(User sender, User recipient, String messageContent, UrgencyLevel urgency)
            throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Message newMsg = new Message(sender, recipient, messageContent, urgency);
        Encryption e = new Encryption();
        String encryptedMessage = e.encryptMessage(messageContent, newMsg.getSharedKey());
        newMsg.setEncryptedMessageText(encryptedMessage);
        MessageFolder recipientMessageFolder = recipient.getHub().getMessageFolder();
        Notification recipientNotifications = recipient.getHub().getNotifications();
        recipientMessageFolder.addNewMessage(newMsg.getMessageID(),newMsg);
        recipientNotifications.addNotification(urgency,"You have 1 new message of type: " + urgency);
        return newMsg.getMessageID();

    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the decrypted message from a recipient's message folder
    public String receiveMessage(User recipient, Integer messageID) throws
            NoSuchPaddingException, NoSuchAlgorithmException {
        Message receivedMessage = recipient.getHub().getMessageFolder().getMessageByID(messageID);
        String messageToDecrypt = receivedMessage.getEncryptedMessageText();
        Encryption e = new Encryption();
        String decryptedMessage = null;
        decryptedMessage = e.decryptMessage(messageToDecrypt, receivedMessage.getSharedKey());
        return decryptedMessage;
    }



    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Notes", note.toJson());
        json.put("Reminders", reminder.toJson());
        json.put("ContactList", addContactListToJson(contactList));
        json.put("MessageFolder", messageFolder.toJson());
        return json;
    }

    private JSONArray addContactListToJson(ArrayList<String> c) {
        JSONArray json = new JSONArray();
        for (String element : c) {
            json.put(element);
        }
        return json;
    }
}
