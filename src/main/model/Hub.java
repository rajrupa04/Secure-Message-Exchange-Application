package model;

import java.util.ArrayList;
//This class represents a hub. Every user will have their own individualised hub, with personalized
//information like notes, reminders, messages, contact list, and so on. Every user will be able to access their hub
//once they log in.

public class Hub {
    private Note note;
    private Reminder reminder;
    private ArrayList<User> contactList;
    private MessageFolder messageFolder;

    public Hub() {
        this.note = new Note();
        this.reminder = new Reminder();
        this.contactList = new ArrayList<User>();
        this.messageFolder = new MessageFolder();
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

    public MessageFolder getMessageFolder() {
        return this.messageFolder;
    }
}
