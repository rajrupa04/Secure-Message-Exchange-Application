package persistence;

import model.*;
import org.json.JSONObject;
import java.io.*;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a writer that writes JSON representation of hub to file

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws IOException {
        writer = new PrintWriter(new FileWriter(destination, true));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of hub to file
    public void writeHub(String username, Hub hub) throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put(username, hub.toJson());
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of user to file
    public void writeUser(User u) throws FileNotFoundException {
        JSONObject userJson = new JSONObject();
        userJson.put(u.getUsername(),u.addUserToJson());
        try {
            open();
            appendToFile(userJson.toString(TAB));
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    // MODIFIES: this
    // EFFECTS: appends string to file
    private void appendToFile(String json) {
        writer.println(json);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
