package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
//This class represents notes which a user can store in their hub.

public class Note {
    private HashMap<Integer, String> listOfNotes;

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: constructs a new note as an empty Hashmap
    public Note() {
        listOfNotes = new HashMap<Integer, String>();
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the hashmap of notes
    public HashMap<Integer, String> getListOfNotes() {
        return this.listOfNotes;
    }

    //REQUIRES: the noteID should be unique, otherwise an existing note gets updated
    //MODIFIES: this
    //EFFECTS: adds a new note with specified note ID to the hashmap
    public void addNote(Integer noteID, String text) {
        listOfNotes.put(noteID,text);
    }


    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the number of notes saved in the hashmap
    public Integer getNumberOfNotes() {
        return this.listOfNotes.size();
    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: removes the note with the specified note ID from the hashmap
    public void removeNote(Integer noteID) {
        this.listOfNotes.remove(noteID);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: retrieves a note with the specified note ID if found
    public String retrieveNote(Integer noteID) {
        if (listOfNotes.containsKey(noteID)) {
            return listOfNotes.get(noteID);
        } else {
            return "Note not found!";
        }
    }


    public JSONArray toJson() {
        JSONArray json = new JSONArray();
        for (Integer noteID : listOfNotes.keySet()) {
            JSONObject noteJson = new JSONObject();
            noteJson.put("noteID", noteID);
            noteJson.put("noteContent", listOfNotes.get(noteID));
            json.put(noteJson);

        }
        return json;
    }
}
