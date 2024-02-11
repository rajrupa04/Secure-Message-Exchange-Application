package model;

//This class represents a notification which will be sent to the recipient when a user sends them
//a message. The notification will be of different types based on how urgent the message is.

import java.util.HashMap;

public class Notification {
    private HashMap<UrgencyLevel,String> notifications;

    public Notification() {
        notifications = new HashMap<>();
    }

    // Method to add a notification for a specific urgency level
    public void addNotification(UrgencyLevel urgencyLevel, String message) {
        notifications.put(urgencyLevel, message);
    }

    // Method to remove a notification for a specific urgency level
    public void removeNotification(UrgencyLevel urgencyLevel) {
        notifications.remove(urgencyLevel);
    }

    // Method to get the notification message for a specific urgency level
    public String getNotification(UrgencyLevel urgencyLevel) {
        return notifications.get(urgencyLevel);
    }

    // Method to remove all notifications
    public void clearNotifications() {
        notifications.clear();
    }

    // Method to update the notification message for a specific urgency level
    public void updateNotification(UrgencyLevel urgencyLevel, String newMessage) {
        if (notifications.containsKey(urgencyLevel)) {
            notifications.put(urgencyLevel, newMessage);
        }
    }


}
