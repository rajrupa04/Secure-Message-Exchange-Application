package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {
    private Note n;

    @BeforeEach
    public void setUp() {
        n = new Note();
    }

    @Test
    public void constructorTest() {
        HashMap<Integer, String> listOfNotes = n.getListOfNotes();
        assertTrue(listOfNotes.isEmpty());
    }

    @Test
    public void addNoteTest() {
        assertEquals(n.retrieveNote(1),"Note not found!");
        assertEquals(n.retrieveNote(2),"Note not found!");
        n.addNote(1,"note 1");
        assertEquals(n.retrieveNote(1),"note 1");
        assertEquals(n.retrieveNote(2),"Note not found!");
        n.addNote(2,"note 2");
        assertEquals(n.getNumberOfNotes(),2);
        assertEquals(n.retrieveNote(2),"note 2");
    }

    @Test
    public void getNumberOfNotesTest() {
        assertEquals(n.getNumberOfNotes(),0);
        n.addNote(1,"note 1");
        assertEquals(n.getNumberOfNotes(),1);
        n.addNote(2,"note 2");
        assertEquals(n.getNumberOfNotes(),2);
        n.removeNote(2);
        assertEquals(n.getNumberOfNotes(),1);
        n.removeNote(1);
        assertEquals(n.getNumberOfNotes(),0);

    }
}
