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

// Some tests referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

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
            assertNotNull(h);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            assertTrue(writer instanceof JsonWriter);
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // This exception is expected
        }
    }

    @Test
    void testWriterEmptyHub() {
        try {
            Hub h = u.getHub();
            assertNotNull(h);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyHub.json");
            assertTrue(writer instanceof JsonWriter);
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
            assertNotNull(h);
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
        assertTrue(writer instanceof JsonWriter);
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
    void testWriteUserSuccessFull() {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralUserInfo.json");
        assertTrue(writer instanceof JsonWriter);
        try {
            writer.openInAppendMode();
            writer.writeUser(u);
            writer.close();

            JsonReader reader = new JsonReader
                    ("./data/testWriterGeneralHub.json", "./data/testWriterGeneralUserInfo.json");
            JSONObject jsonData = reader.returnJsonObject("./data/testWriterGeneralUserInfo.json");
            assertNotNull(jsonData);


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

    @Test
    void testClose() {
        JsonWriter jsonWriter = new JsonWriter("./data/testWriterEmptyUserInfo.json");
        assertTrue(jsonWriter instanceof JsonWriter);
        try {
            jsonWriter.open();
            jsonWriter.close();
        } catch (IOException e) {
            fail("Unexpected IOException!");
        }

        try {
            jsonWriter.openInAppendMode();
            jsonWriter.close();
        } catch (IOException e) {
            fail("Unexpected IOException!");
        }

        jsonWriter = new JsonWriter(null);
        jsonWriter.setWriterAppend(null);
        assertNull(jsonWriter.getWriterAppend());
        assertNotNull(jsonWriter);
        try {
            jsonWriter.close();
        } catch (Exception e) {
            fail("Unexpected Exception!");
        }

        JSONObject data = new JSONObject();
        JSONArray usersArray = new JSONArray();
        data.put("Users", usersArray);


        String filePath = "./data/testWriterEmptyUserInfo.json";


        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.write(data.toString(4));
        } catch (IOException e) {
            fail("Unexpected IOException!");
        }
    }



}
