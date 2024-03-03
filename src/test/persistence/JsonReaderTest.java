package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    private User u;

    @BeforeEach
    void setUp() {
        u = new User();
        u.setUsername("testuser1");
        u.setPassword("p@ssw0rd");
        u.setUserID(12345678);
        u.setHub(new Hub());
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile1.json","./data/noSuchFile2.json");
        try {
            Hub h = reader.read(u.getUserID());
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException!");
        }
    }

    @Test
    void testReaderEmptyHub() {
        JsonReader reader = new JsonReader
                ("./data/testReaderEmptyHub.json","./data/testReaderEmptyUserInfo.json");
        try {
            Hub h = reader.read(u.getUserID());
            assertEquals(h.getNote().getNumberOfNotes(),0);
            assertEquals(h.getReminder().numberOfReminders(),0);
            assertEquals(h.getNotifications().getHashMapOfNotifications().size(),0);
            assertEquals(h.getMessageFolder().getMessageFolder().size(),0);
        } catch (IOException e) {
            fail("Couldn't read from file!");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException!");
        }
    }

    @Test
    void testReaderGeneralHub() {
        JsonReader reader = new JsonReader
                ("./data/testReaderGeneralHub.json","./data/testReaderGeneralUserInfo.json");
        try {
            Hub h = reader.read(u.getUserID());
            Note n = h.getNote();
            Reminder r = h.getReminder();
            Notification notif = h.getNotifications();
            assertEquals(n.getNumberOfNotes(),2);
            assertEquals(n.retrieveNote(1), "Note 1 for Test");
            assertEquals(n.retrieveNote(2), "Note 2 for Test");
            assertEquals(r.numberOfReminders(),2);
            assertEquals(r.retrieveReminder(LocalDate.parse("2024-02-25")),"Reminder 1 for Test");
            assertEquals(r.retrieveReminder(LocalDate.parse("2024-02-26")),"Reminder 2 for Test");
            assertEquals(h.getContactList().get(0),"john_doe");
            assertEquals(h.getMessageFolder().getMessageFolder().size(),0);
            assertEquals(h.getNotifications().getNotification
                    (UrgencyLevel.REGULAR),"You have 1 new REGULAR notification!");
            assertEquals(h.getNotifications().getNotification
                    (UrgencyLevel.URGENT),"You have 1 new URGENT notification!");
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException!");
        }
    }
}
