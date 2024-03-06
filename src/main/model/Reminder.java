package model;

//This class represents reminders which a user can store in their hub.

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.time.*;

public class Reminder {
    private HashMap<LocalDate,String> reminders;

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: constructs a new set of reminders as an empty hashmap
    public Reminder() {
        reminders = new HashMap<>();
    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: adds a new reminder with a specific date to the hashmap
    public void addNewReminder(LocalDate date, String reminderText) {
        reminders.put(date,reminderText);
    }

    //REQUIRES: there should be an existing reminder with the same date
    //MODIFIES: this
    //EFFECTS: removes the specified reminder from the hashmap
    public void removeExistingReminder(LocalDate date) {
        reminders.remove(date);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: retrieves the reminder which has the specified date.
    public String retrieveReminder(LocalDate date) {
        return reminders.get(date);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: checks if a reminder with the specified date exists in the hashmap.
    public Boolean checkForExistingReminder(LocalDate date) {
        return reminders.containsKey(date);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the entire hashmap of reminders
    public HashMap<LocalDate, String> getAllReminders() {
        return reminders;
    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: clears the hashmap of reminders.
    public void clearAllReminders() {
        reminders.clear();
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the number of reminders
    public int numberOfReminders() {
        return reminders.size();
    }



    //EFFECTS: returns the reminders as a Json Array
    public JSONArray toJson() {
        JSONArray json = new JSONArray();
        for (LocalDate date : reminders.keySet()) {
            JSONObject reminderJson = new JSONObject();
            reminderJson.put("Date", date.format(DateTimeFormatter.ISO_DATE));
            reminderJson.put("reminderContent", reminders.get(date));
            json.put(reminderJson);

        }
        return json;
    }
}
