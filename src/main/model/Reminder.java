package model;

//This class represents reminders which a user can store in their hub.

import java.util.HashMap;
import java.time.*;

public class Reminder {
    private HashMap<LocalDate,String> reminders;

    public Reminder() {
        reminders = new HashMap<>();
    }

    public void addNewReminder(LocalDate date, String reminderText) {
        reminders.put(date,reminderText);
    }

    public void removeExistingReminder(LocalDate date) {
        reminders.remove(date);
    }

    public String retrieveReminder(LocalDate date) {
        return reminders.get(date);
    }

    public Boolean checkForExistingReminder(LocalDate date) {
        return reminders.containsKey(date);
    }

    public HashMap<LocalDate, String> getAllReminders() {
        return reminders;
    }

    public void clearAllReminders() {
        reminders.clear();
    }

    public int numberOfReminders() {
        return reminders.size();
    }


}
