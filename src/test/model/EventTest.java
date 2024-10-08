package model;

import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Sensor open at door");   // (1)
        d = Calendar.getInstance().getTime();   // (2)

    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e.getDescription());
        assertEquals(d.getDay(), e.getDate().getDay());
        assertEquals(d.getMonth(), e.getDate().getMonth());
        assertEquals(d.getYear(), e.getDate().getYear());
        assertEquals(d.getHours(), e.getDate().getHours());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
    }

    @Test
    void testEqualsNull() {
        assertFalse(e.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        Object obj = new Object();
        assertFalse(e.equals(obj));
    }

    @Test
    void testEqualsSameObject() {
        Event event2 = new Event("Sensor open at door");
        assertTrue(e.equals(event2));
    }

    @Test
    void testEqualsDifferentAttributes() {
        Event event2 = new Event("Different Description");
        assertFalse(e.equals(event2));
    }

    @Test
    void testHashCodeEquality() {
        int hashCode1 = e.hashCode();
        int hashCode2 = e.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeInequality() {
        Event event2 = new Event("Different Description");
        assertNotEquals(e.hashCode(), event2.hashCode());
    }
}
