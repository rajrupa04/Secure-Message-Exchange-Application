package persistence;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import org.json.JSONObject;

//This interface represents an object that can be converted to a JSON format.

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}