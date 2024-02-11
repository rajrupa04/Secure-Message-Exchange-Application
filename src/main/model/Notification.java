package model;

//This class represents a notification which will be sent to the recipient when a user sends them
//a message. The notification will be of different types based on how urgent the message is.

import java.util.HashMap;

public class Notification {
    private HashMap<UrgencyLevel,String> notifications;

    public Notification() {
        notifications = new HashMap<>();
    }

    //EFFECTS: adds a notification for a specific urgency level
    public void addNotification(UrgencyLevel urgencyLevel, String message) {
        notifications.put(urgencyLevel, message);
    }

    //EFFECTS: removes a notification for a specific urgency level
    public void removeNotification(UrgencyLevel urgencyLevel) {
        notifications.remove(urgencyLevel);
    }

    //EFFECTS: gets the notification message for a specific urgency level
    public String getNotification(UrgencyLevel urgencyLevel) {
        return notifications.get(urgencyLevel);
    }

    //EFFECTS: removes all notifications
    public void clearNotifications() {
        notifications.clear();
    }

    //EFFECTS: updates the notification message for a specific urgency level
    public void updateNotification(UrgencyLevel urgencyLevel, String newMessage) {
        if (notifications.containsKey(urgencyLevel)) {
            notifications.put(urgencyLevel, newMessage);
        }
    }


}
