package persistence;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.json.*;

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads hub from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Hub read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseHub(jsonObject);
    }

    // EFFECTS: parses hub from JSON object and returns it
    private Hub parseHub(JSONObject jsonObject) {
        String username = jsonObject.getString("Username");
        Hub h = new Hub();
        addNote(h, jsonObject);
        addReminder(h, jsonObject);
        addContactList(h,jsonObject);
        addMessageFolder(h,jsonObject);
        return h;
    }

    // MODIFIES: h
    // EFFECTS: parses messages from JSON object and adds them to hub
    private void addMessageFolder(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("MessageFolder");
        for (Object json : jsonArray) {
            JSONObject nextMessage = (JSONObject) json;
            addSingleMessage(h, nextMessage);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses a single message from JSON object and adds them to hub
    private void addSingleMessage(Hub h, JSONObject jsonObject) {

    }

    // MODIFIES: h
    // EFFECTS: parses contact list from JSON object and adds them to hub
    private void addContactList(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("ContactList");
        for (Object json : jsonArray) {
            JSONObject nextContact = (JSONObject) json;
            String username = nextContact.getString("Username");
            h.getContactList().add(username);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses reminders from JSON object and adds them to hub
    private void addReminder(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Reminders");
        for (Object json : jsonArray) {
            JSONObject nextReminder = (JSONObject) json;
            addSingleReminder(h, nextReminder);
        }
    }

    // MODIFIES: h
    // EFFECTS: parses a single reminder from JSON object and adds it to hub
    private void addSingleReminder(Hub h, JSONObject jsonObject) {
        String reminderDate = jsonObject.getString("Date");
        String reminderContent = jsonObject.getString("reminderContent");
        LocalDate date = LocalDate.parse(reminderDate);
        h.getReminder().addNewReminder(date,reminderContent);
    }

    // MODIFIES: h
    // EFFECTS: parses notes from JSON object and adds them to hub
    private void addNote(Hub h, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Notes");
        for (Object json : jsonArray) {
            JSONObject nextNote = (JSONObject) json;
            addSingleNote(h, nextNote);
        }
    }

    // MODIFIES: hub
    // EFFECTS: parses a single note from JSON object and adds it to hub
    private void addSingleNote(Hub hub, JSONObject jsonObject) {
        Integer noteID = jsonObject.getInt("noteID");
        String noteContent = jsonObject.getString("noteContent");
        hub.getNote().addNote(noteID,noteContent);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
}
