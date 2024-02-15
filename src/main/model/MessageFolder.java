package model;

//This class represents a message folder, which will be present in the hub of every user. Each user will
//be able to see their messages at a glance in the folder.

import java.util.HashMap;
import model.Encryption;

public class MessageFolder {
    private HashMap<Integer, Message> messageFolder;

    public MessageFolder() {
        messageFolder = new HashMap<>();
    }

    public void addNewMessage(Integer messageID, Message message) {
        messageFolder.put(messageID,message);
    }

    public Message getMessageByID(Integer messageID) {
        return this.messageFolder.get(messageID);
    }

    public boolean containsMessage(Integer messageID) {
        return messageFolder.containsKey(messageID);
    }

    public HashMap<Integer, Message> getMessageFolder() {
        return this.messageFolder;
    }
}
