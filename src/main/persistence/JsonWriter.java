package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a writer that writes JSON representation of hub to file

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private PrintWriter writerAppend;
    private String destination;
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private String pathForSpecificUser;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws IOException {
        writer = new PrintWriter(new FileWriter(destination));
    }

    // MODIFIES: this
    // EFFECTS: opens writer in append mode; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void openInAppendMode() throws IOException {
        writerAppend = new PrintWriter(new FileWriter(destination,true));
    }

    // EFFECTS: returns the writerAppend object
    public PrintWriter getWriterAppend() {
        return this.writerAppend;
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of hub to file
    public void writeHub(String username, String userID, Hub hub) throws IOException {
        pathForSpecificUser = "./data/" + username + ".json";
        JSONObject json = new JSONObject();
        json.put("Hub", hub.toJson());
        json.put("Username", username);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of user to file
    public void writeUser(User u) throws IOException {
        JSONArray usersArray;
        JSONObject userJson = new JSONObject();
        userJson = u.addUserToJson();

        JsonReader jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
        JSONObject existingData = jsonReader.returnJsonObject(JSON_USERINFO);
        usersArray = existingData.getJSONArray("Users");
        usersArray.put(userJson);
        JSONObject newData = new JSONObject();
        newData.put("Users", usersArray);
        open();
        appendToFile(newData.toString(TAB));


    }

    // MODIFIES: this
    // EFFECTS: appends string to file
    public void appendToFile(String json) {
        writerAppend.println(json);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        if (writerAppend != null) {
            writerAppend.close();
        } else if (writer != null) {
            writer.close();
        }
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    // MODIFIES: this
    // EFFECTS: sets the writerAppend field to the provided PrintWriter object
    public void setWriterAppend(PrintWriter p) {
        this.writerAppend = p;
    }
}
