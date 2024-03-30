package model;

//This class represents a notification which will be sent to the recipient when a user sends them
//a message. The notification will be of different types based on how urgent the message is.

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;

public class Notification {
    private HashMap<UrgencyLevel,String> notifications;

    //MODIFIES: this
    //EFFECTS: constructs a new notification object as an empty hashmap
    public Notification() {
        notifications = new HashMap<>();
    }

    //REQUIRES: the urgency level should be valid
    //MODIFIES: this
    //EFFECTS: adds a notification for a specific urgency level
    public void addNotification(UrgencyLevel urgencyLevel, String message) {
        EventLog.getInstance().logEvent(new Event("Added a new notification with urgency level: "
                +
                urgencyLevel + " and content: " + message + "."));
        notifications.put(urgencyLevel, message);
    }

    //REQUIRES: the urgency level should be valid
    //MODIFIES: this
    //EFFECTS: removes a notification for a specific urgency level
    public void removeNotification(UrgencyLevel urgencyLevel) {
        notifications.remove(urgencyLevel);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the notification message for a specific urgency level
    public String getNotification(UrgencyLevel urgencyLevel) {
        if (notifications.containsKey(urgencyLevel)) {
            return notifications.get(urgencyLevel);
        } else {
            return null;
        }

    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: removes all notifications
    public void clearNotifications() {
        notifications.clear();
    }

    //REQUIRES: there should be an existing notification with the same urgency level
    //MODIFIES: this
    //EFFECTS: updates the notification message for a specific urgency level
    public void updateNotification(UrgencyLevel urgencyLevel, String newMessage) {
        if (notifications.containsKey(urgencyLevel)) {
            notifications.put(urgencyLevel, newMessage);
        }
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the hashmap containing the notifications along with their urgency levels
    public HashMap getHashMapOfNotifications() {
        return this.notifications;
    }


    //EFFECTS: returns the notifications as a Json Array
    public JSONArray toJson() {
        JSONArray json = new JSONArray();
        for (UrgencyLevel u : notifications.keySet()) {
            JSONObject notifToAdd = new JSONObject();
            notifToAdd.put("UrgencyLevel",u.toString());
            notifToAdd.put("Message",getNotification(u));
            json.put(notifToAdd);
        }
        return json;
    }
}
