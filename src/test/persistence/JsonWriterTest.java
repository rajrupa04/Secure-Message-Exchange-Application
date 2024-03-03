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
public class JsonWriterTest {
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
    void testWriterInvalidFile() {
        try {
            Hub h = u.getHub();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyHub() {
        try {
            Hub h = u.getHub();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyHub.json");
            writer.open();
            writer.writeHub(u.getUsername(),u.getUserID().toString(),h);
            writer.close();

            JsonReader reader = new JsonReader
                    ("./data/testWriterEmptyHub.json","./data/testWriterEmptyUserInfo.json");
            h = reader.read(u.getUserID());
            assertEquals(h.getNote().getNumberOfNotes(),0);
            assertEquals(h.getReminder().numberOfReminders(),0);
            assertEquals(h.getNotifications().getHashMapOfNotifications().size(),0);
            assertEquals(h.getMessageFolder().getMessageFolder().size(),0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (NoSuchPaddingException e) {
            fail("Exception should not have been thrown");
        } catch (NoSuchAlgorithmException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Hub h = u.getHub();
            h.getNote().addNote(777,"Test note 777");
            h.getReminder().addNewReminder
                    (LocalDate.parse("2024-10-01"),"Wake me up when September ends");
            h.getContactList().add("raj2410");
            h.getNotifications().addNotification(UrgencyLevel.REGULAR,"This is a new REGULAR notification");

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralHub.json");
            writer.open();
            writer.writeHub(u.getUsername(),u.getUserID().toString(),h);
            writer.close();

            JsonReader reader = new JsonReader
                    ("./data/testWriterGeneralHub.json","./data/testWriterGeneralUserInfo.json");
            h = reader.read(u.getUserID());
            Note n = h.getNote();
            Reminder r = h.getReminder();
            Notification notif = h.getNotifications();
            assertEquals(n.getNumberOfNotes(),1);
            assertEquals(n.retrieveNote(777), "Test note 777");
            assertEquals(r.numberOfReminders(),1);
            assertEquals(r.retrieveReminder(LocalDate.parse("2024-10-01")),"Wake me up when September ends");
            h.getContactList().add("raj2410");
            assertEquals(h.getMessageFolder().getMessageFolder().size(),0);
            assertEquals(h.getNotifications().getNotification
                    (UrgencyLevel.REGULAR),"This is a new REGULAR notification");

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (NoSuchPaddingException e) {
            fail("Exception should not have been thrown");
        } catch (NoSuchAlgorithmException e) {
            fail("Exception should not have been thrown");
        }
    }
}
