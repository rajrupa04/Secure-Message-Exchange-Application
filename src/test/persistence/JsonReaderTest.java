package persistence;

import model.*;
import org.json.JSONArray;
import persistence.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

// Some tests referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonReaderTest {
    private User user1;
    private Message m;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("testuser1");
        user1.setPassword("p@ssw0rd");
        user1.setUserID(12345678);
        user1.setHub(new Hub());
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile1.json","./data/noSuchFile2.json");
        assertNotNull(reader);
        try {
            Hub h = reader.read(user1.getUserID());
            fail("IOException expected");
        } catch (IOException e) {
            // This exception is expected
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
        assertNotNull(reader);
        try {
            Hub h = reader.read(user1.getUserID());
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
        assertNotNull(reader);
        try {
            Hub h = reader.read(user1.getUserID());
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

    @Test
    void testReturnJsonObject() {
        try {
            JSONObject j = JsonReader.returnJsonObject("./data/testReaderGeneralHub.json");
            assertNotNull(j);
            assertEquals("testuser1", j.getString("Username"));
            assertTrue(j.getJSONObject("Hub") instanceof JSONObject);

        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    void testAddMessageFolder(){

        JsonReader reader = new JsonReader
                ("./data/testReaderEmptyHub.json","./data/testReaderGeneralUserInfo.json");
        assertNotNull(reader);


        try {
            JSONObject hubJsonObject = reader.returnJsonObject("./data/testReaderEmptyHub.json");
            JSONObject userJsonObject = reader.returnJsonObject("./data/testReaderGeneralUserInfo.json");
            JSONObject userJsonDataOriginalObject = reader.returnJsonObject("./data/userinfo.json");

            assertTrue(hubJsonObject instanceof JSONObject);
            assertTrue(userJsonObject instanceof JSONObject);
            assertTrue(userJsonDataOriginalObject instanceof JSONObject);

            User sender = reader.getUserByID(99432041,userJsonDataOriginalObject);
            User recipient = reader.getUserByID
                    (12345678,userJsonObject);

            assertTrue(sender instanceof User);
            assertTrue(recipient instanceof User);

            Message m = new Message(sender, recipient,"New Message!",UrgencyLevel.REGULAR);
            assertNotNull(m);
            Hub h = reader.read(user1.getUserID());
            assertNotNull(h);
            h.getMessageFolder().addNewMessage(m.getMessageID(),m);

            Hub hnew = new Hub();


            JSONObject messageFolderJson = new JSONObject();
            assertTrue(messageFolderJson instanceof JSONObject);

            JSONArray messages = new JSONArray();
            JSONObject messageJson = new JSONObject();
            messageJson.put("SenderUserID", sender.getUserID());
            messageJson.put("RecipientUserID", recipient.getUserID());
            messageJson.put("MessageID", m.getMessageID());
            messageJson.put("DecryptedMessageText", "New Message!");
            messageJson.put("EncryptedMessageText", m.encryptMessage("New Message!",m.getSharedKey()));
            messageJson.put("Urgency Level", m.getUrgencyLevel().toString());
            messageJson.put("SharedKey", Base64.getEncoder().encodeToString(m.getSharedKey().toString().getBytes()));
            messages.put(messageJson);
            messageFolderJson.put("MessageFolder", messages);
            reader.addMessageFolder(hnew,messageFolderJson, userJsonObject);

            assertNotNull(hnew.getMessageFolder().getMessageByID(m.getMessageID()));
            assertTrue(hnew.getMessageFolder().containsMessage(m.getMessageID()));
            assertEquals(hnew.getMessageFolder().getMessageByID(m.getMessageID()).decryptMessage
                    (m.encryptMessage("New Message!",m.getSharedKey())
                            ,m.getSharedKey()),"New Message!");


        } catch (IOException e) {
            fail("Couldn't read from file" + e.getMessage());
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException!");
        } catch (IllegalBlockSizeException e) {
            fail("Unexpected IllegalBlockSizeException!");
        } catch (BadPaddingException e) {
            fail("Unexpected BadPaddingException!");
        } catch (InvalidKeyException e) {
            fail("Unexpected InvalidKeyException!");
        }

    }

    @Test
    void testGetUserByID() {
        JsonReader reader = new JsonReader
                ("./data/testReaderEmptyHub.json","./data/testReaderGeneralUserInfo.json");
        assertNotNull(reader);
        assertTrue(reader instanceof JsonReader);
        try {
            String userJsonData = reader.readFile("./data/testReaderGeneralUserInfo.json");
            assertTrue(userJsonData instanceof String);
            JSONObject userJsonObject = new JSONObject(userJsonData);
            User u = reader.getUserByID(user1.getUserID(),userJsonObject);

            assertTrue(u instanceof User);
            assertEquals(u.getUserID(),user1.getUserID());
            assertEquals(u.getUsername(),user1.getUsername());
            assertEquals(u.getPassword(),user1.getPassword());

        } catch (IOException e) {
            fail("Unexpected IOException!");
        }

    }

    @Test
    void testGetUserByIDDoesNotExist() {
        JsonReader reader = new JsonReader
                ("./data/testReaderEmptyHub.json","./data/testReaderGeneralUserInfo.json");
        assertNotNull(reader);
        assertTrue(reader instanceof JsonReader);
        try {
            String userJsonData = reader.readFile("./data/testReaderGeneralUserInfo.json");
            assertTrue(userJsonData instanceof String);
            JSONObject userJsonObject = new JSONObject(userJsonData);
            assertTrue(userJsonObject instanceof JSONObject);
            User u = reader.getUserByID(00000000,userJsonObject);

            assertNull(u.getUsername());
            assertNull(u.getPassword());
        } catch (IOException e) {
            fail("Unexpected IOException!");
        }

    }


    @Test
    void testStringToSecretKey() {
        JsonReader reader = new JsonReader
                ("./data/testReaderEmptyHub.json","./data/testReaderGeneralUserInfo.json");

        assertNotNull(reader);
        assertTrue(reader instanceof JsonReader);
        String toConvert = "aGVsbG8gd29ybGQ=";
        SecretKey sk = reader.stringToSecretKey(toConvert);
        assertTrue(sk instanceof SecretKey);
        byte[] encodedKey = sk.getEncoded();
        String result = Base64.getEncoder().encodeToString(encodedKey);

        assertEquals(result,toConvert);
        assertEquals("DES", sk.getAlgorithm());
    }


}
