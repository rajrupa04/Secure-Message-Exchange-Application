package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReminderTest {
    private Reminder r;

    @BeforeEach
    public void setUp() {
        r = new Reminder();
    }

    @Test
    public void constructorTest() {
        assertEquals(r.numberOfReminders(),0);
    }

    @Test
    public void addNewReminderTest() {
        assertEquals(r.numberOfReminders(),0);
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        assertEquals(r.numberOfReminders(),1);

    }

    @Test
    public void removeExistingReminderTest() {
        assertEquals(r.numberOfReminders(),0);
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        assertEquals(r.numberOfReminders(),1);
        r.removeExistingReminder(LocalDate.of(2024, 10, 15));
        assertEquals(r.numberOfReminders(),0);

    }

    @Test
    public void checkForExistingReminderTest() {
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        assertTrue(r.checkForExistingReminder(LocalDate.of(2024, 10, 15)));
        assertFalse(r.checkForExistingReminder(LocalDate.of(2024, 10, 16)));


    }

    @Test
    public void numberOfRemindersTest() {
        assertEquals(r.numberOfReminders(),0);
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        assertEquals(r.numberOfReminders(),1);
        r.addNewReminder(LocalDate.of(2024, 11, 16),"reminder2");
        assertEquals(r.numberOfReminders(),2);
        r.clearAllReminders();
        assertEquals(r.numberOfReminders(),0);
    }

    @Test
    public void retrieveReminderTest() {
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        r.addNewReminder(LocalDate.of(2024, 11, 16),"reminder2");
        assertEquals("reminder1",r.retrieveReminder(LocalDate.of(2024, 10, 15)));
        assertEquals("reminder2",r.retrieveReminder(LocalDate.of(2024, 11, 16)));

    }

    @Test
    public void getAllRemindersTest() {
        assertEquals(r.getAllReminders().size(),0);
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        assertEquals(r.getAllReminders().size(),1);
        r.addNewReminder(LocalDate.of(2024, 11, 16),"reminder2");
        assertEquals(r.getAllReminders().size(),2);
    }

    @Test
    public void clearAllRemindersTest() {
        r.addNewReminder(LocalDate.of(2024, 10, 15),"reminder1");
        r.addNewReminder(LocalDate.of(2024, 11, 16),"reminder2");
        r.clearAllReminders();
        assertEquals(0,r.numberOfReminders());
    }


}
