package model;

//This class represents a message folder, which will be present in the hub of every user. Each user will
//be able to see their messages at a glance in the folder.

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import model.Encryption;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;


public class MessageFolder {
    private HashMap<Integer, Message> messageFolder;

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: constructs a new MessageFolder with an empty hashmap
    public MessageFolder() {
        messageFolder = new HashMap<>();
    }

    //REQUIRES: The message ID must be unique
    //MODIFIES: this
    //EFFECTS: adds a new message to the message folder
    public void addNewMessage(Integer messageID, Message message) {
        messageFolder.put(messageID,message);
    }

    //REQUIRES: The message should exist in the folder
    //MODIFIES: none
    //EFFECTS: fetches the message corresponding to the specified message ID
    public Message getMessageByID(Integer messageID) {
        return this.messageFolder.get(messageID);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns true if the message with the provided message ID exists in the message folder, otherwise false
    public boolean containsMessage(Integer messageID) {
        return messageFolder.containsKey(messageID);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the message folder hashmap
    public HashMap<Integer, Message> getMessageFolder() {
        return this.messageFolder;
    }


    public JSONArray toJson() {
        JSONArray json = new JSONArray();
        for (Integer messageID : messageFolder.keySet()) {
            JSONObject messageJson = new JSONObject();
            messageJson = getMessageByID(messageID).toJson();
            json.put(messageJson);
        }
        return json;
    }
}
