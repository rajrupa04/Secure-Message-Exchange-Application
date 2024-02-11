package model;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import model.Encryption;
import model.Message;

import javax.crypto.NoSuchPaddingException;
//This class represents a hub. Every user will have their own individualised hub, with personalized
//information like notes, reminders, messages, contact list, and so on. Every user will be able to access their hub
//once they log in.

public class Hub {
    private Note note;
    private Reminder reminder;
    private ArrayList<User> contactList;
    private MessageFolder messageFolder;
    private Notification notifications;

    public Hub() {
        this.note = new Note();
        this.reminder = new Reminder();
        this.contactList = new ArrayList<User>();
        this.messageFolder = new MessageFolder();
        this.notifications = new Notification();
    }

    public Note getNote() {
        return this.note;
    }

    public Reminder getReminder() {
        return this.reminder;
    }

    public ArrayList getContactList() {
        return this.contactList;
    }

    public Notification getNotifications() {
        return this.notifications;
    }

    public MessageFolder getMessageFolder() {
        return this.messageFolder;
    }

    public Integer sendMessage(User sender, User recipient, String messageContent, UrgencyLevel urgency)
            throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
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

    public String receiveMessage(User recipient, Integer messageID) throws
            NoSuchPaddingException, NoSuchAlgorithmException {
        Message receivedMessage = recipient.getHub().getMessageFolder().getMessageByID(messageID);
        String messageToDecrypt = receivedMessage.getEncryptedMessageText();
        Encryption e = new Encryption();
        String decryptedMessage = e.decryptMessage(messageToDecrypt, receivedMessage.getSharedKey());
        return decryptedMessage;
    }



}
