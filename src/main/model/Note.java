package model;

import java.util.HashMap;
//This class represents notes which a user can store in their hub.

public class Note {
    private HashMap<Integer, String> listOfNotes;

    public Note() {
        listOfNotes = new HashMap<Integer, String>();
    }

    public HashMap<Integer, String> getListOfNotes() {
        return this.listOfNotes;
    }

    public void addNote(Integer noteID, String text) {
        listOfNotes.put(noteID,text);
    }


    public Integer getNumberOfNotes() {
        return this.listOfNotes.size();
    }

    public String getMostRecentNote() {
        return this.listOfNotes.get(this.getNumberOfNotes() - 1);
    }

    public void removeMostRecentNotes(Integer noteID) {
        this.listOfNotes.remove(noteID);
    }

    public String retrieveNote(Integer noteID) {
        if (listOfNotes.containsKey(noteID)) {
            return listOfNotes.get(noteID);
        } else {
            return "Not Found";
        }
    }


}
