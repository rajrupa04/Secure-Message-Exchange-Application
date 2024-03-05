package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

    @Test
    void testOpenInAppendMode() {
        JsonWriter writer = new JsonWriter("./data/testWriterEmptyUserInfo.json");
        try {
            writer.openInAppendMode();
            PrintWriter printWriter = writer.getWriterAppend();

            assertNotNull(printWriter);


            JsonReader reader = new JsonReader("./data/testWriterEmptyHub.json"
                    ,"./data/testWriterEmptyUserInfo.json");
            JSONObject jsonData = reader.returnJsonObject("./data/testWriterEmptyUserInfo.json");

            assertNotNull(jsonData);


        } catch (IOException e) {
            fail("Unexpected IOException!");
        }

    }

    @Test
    void testWriteUser() {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralUserInfo.json");
        try {
            writer.openInAppendMode();
            writer.writeUser(u);
            writer.close();
            // Read the file and check if the user data is correctly appended
            JsonReader reader = new JsonReader
                    ("./data/testWriterGeneralHub.json", "./data/testWriterGeneralUserInfo.json");
            JSONObject jsonData = reader.returnJsonObject("./data/testWriterGeneralUserInfo.json");
            assertNotNull(jsonData);

            // Check if the user data is correctly appended to the Users array
            JSONArray usersArray = jsonData.getJSONArray("Users");
            JSONObject lastUser = usersArray.getJSONObject(usersArray.length() - 1);
            assertEquals("testuser1", lastUser.getString("Username"));
            assertEquals("p@ssw0rd", lastUser.getString("Password"));
            assertEquals(12345678, lastUser.getInt("UserID"));
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

    }


}
