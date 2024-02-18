package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import static org.junit.jupiter.api.Assertions.*;
import model.*;

public class HubTest {
    User sender = new User();
    User recipient = new User();
    Hub senderHub;
    Hub recipientHub;

    @BeforeEach
    public void setUp() {
        sender.createNewUser("sender","11111");
        recipient.createNewUser("recipient","456456");
        senderHub = sender.getHub();
        recipientHub = recipient.getHub();
    }

    @Test
    public void constructorTest() {
        assertNotNull(senderHub.getNote());
        assertNotNull(senderHub.getReminder());
        assertNotNull(senderHub.getContactList());
        assertTrue(senderHub.getContactList().isEmpty());
        assertNotNull(senderHub.getMessageFolder());
        assertNotNull(senderHub.getNotifications());
        assertNotNull(recipientHub.getNote());
        assertNotNull(recipientHub.getReminder());
        assertNotNull(recipientHub.getContactList());
        assertTrue(recipientHub.getContactList().isEmpty());
        assertNotNull(recipientHub.getMessageFolder());
        assertNotNull(recipientHub.getNotifications());
    }

    @Test
    public void sendmessageTest() {
        String messageContent = "test";
        UrgencyLevel urgency = UrgencyLevel.REGULAR;
        try {
            Integer messageId = senderHub.sendMessage(sender,recipient,messageContent,urgency);
            assertNotNull(messageId);
            assertTrue(recipientHub.getMessageFolder().containsMessage(messageId));
            assertFalse(senderHub.getMessageFolder().containsMessage(messageId));
            assertEquals(recipientHub.getNotifications().getNotification(urgency),
                    "You have 1 new message of type: REGULAR");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException");
        } catch (IllegalBlockSizeException e) {
            fail("Unexpected IllegalBlockSizeException");
        } catch (BadPaddingException e) {
            fail("Unexpected BadPaddingException");
        } catch (InvalidKeyException e) {
            fail("Unexpected InvalidKeyException");
        }

    }

    @Test
    public void receiveMessageTest() {
        String messageContent = "test";
        UrgencyLevel urgency = UrgencyLevel.REGULAR;
        Integer messageId = null;
        try {
            messageId = senderHub.sendMessage(sender,recipient,messageContent,urgency);
            String received = recipientHub.receiveMessage(recipient, messageId);
            assertNotNull(received);
            assertEquals(messageContent,received);
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException");
        } catch (IllegalBlockSizeException e) {
            fail("Unexpected IllegalBlockSizeException");
        } catch (BadPaddingException e) {
            fail("Unexpected BadPaddingException");
        } catch (InvalidKeyException e) {
            fail("Unexpected InvalidKeyException");
        }

    }

    @Test
    public void getNoteTest() {
        senderHub.getNote().addNote(1,"test note 1");
        senderHub.getNote().addNote(2,"test note 2");
        recipientHub.getNote().addNote(3,"test note 3");
        recipientHub.getNote().addNote(4,"test note 4");

        HashMap<Integer,String> noteTestSender = new HashMap<>();
        noteTestSender.put(1,"test note 1");
        noteTestSender.put(2,"test note 2");

        HashMap<Integer,String> noteTestRecipient = new HashMap<>();
        noteTestRecipient.put(3,"test note 3");
        noteTestRecipient.put(4,"test note 4");

        Note senderNotes = senderHub.getNote();
        Note recipientNotes = recipientHub.getNote();
        assertEquals(senderNotes.getNumberOfNotes(),2);
        assertEquals(recipientNotes.getNumberOfNotes(),2);
        assertEquals(noteTestSender,senderNotes.getListOfNotes());
        assertEquals(noteTestRecipient,recipientNotes.getListOfNotes());



    }

    @Test
    public void getReminderTest() {
        senderHub.getReminder().addNewReminder(LocalDate.of(2024,10,01),"wake me up");
        senderHub.getReminder().addNewReminder(LocalDate.of(2024,11,01),"test");

        HashMap<LocalDate,String> reminderTest = new HashMap<>();
        reminderTest.put(LocalDate.of(2024,10,01),"wake me up");
        reminderTest.put(LocalDate.of(2024,11,01),"test");

        Reminder senderReminders = senderHub.getReminder();

        assertEquals(senderReminders.getAllReminders().size(),2);
        assertEquals(senderReminders.getAllReminders(),reminderTest);
    }

    @Test
    public void getContactListTest() {
        senderHub.getContactList().add(recipient);
        recipientHub.getContactList().add(sender);

        ArrayList<User> senderContactList = new ArrayList<>();
        senderContactList.add(recipient);
        ArrayList<User> recipientContactList = new ArrayList<>();
        recipientContactList.add(sender);

        assertEquals(senderHub.getContactList().size(),1);
        assertEquals(recipientHub.getContactList().size(),1);
        assertEquals(senderHub.getContactList(),senderContactList);
        assertEquals(recipientHub.getContactList(),recipientContactList);
    }

    @Test
    public void getNotificationsTest() {
        senderHub.getNotifications().addNotification(UrgencyLevel.REGULAR,"regular notification!");
        senderHub.getNotifications().addNotification(UrgencyLevel.EMERGENCY,"emergency notification!");

        HashMap<UrgencyLevel,String> senderNotifs = new HashMap<>();
        senderNotifs.put(UrgencyLevel.REGULAR,"regular notification!");
        senderNotifs.put(UrgencyLevel.EMERGENCY,"emergency notification!");

        assertEquals(senderHub.getNotifications().getHashMapOfNotifications(),senderNotifs);
        assertEquals(senderHub.getNotifications().getHashMapOfNotifications().size(),2);
        assertEquals(senderHub.getNotifications().getNotification(UrgencyLevel.REGULAR),"regular notification!");
        assertEquals(senderHub.getNotifications().getNotification(UrgencyLevel.EMERGENCY),
                "emergency notification!");

    }

    @Test
    public void getMessageFolderTest() {
        HashMap<Integer, Message> rmf = new HashMap<>();
        MessageFolder recipientMessageFolder = recipientHub.getMessageFolder();
        try {
            Integer mID = senderHub.sendMessage(sender,recipient,"test message",UrgencyLevel.REGULAR);
            assertTrue(recipientMessageFolder.containsMessage(mID));
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException");
        } catch (IllegalBlockSizeException e) {
            fail("Unexpected IllegalBlockSizeException");
        } catch (BadPaddingException e) {
            fail("Unexpected BadPaddingException");
        } catch (InvalidKeyException e) {
            fail("Unexpected InvalidKeyException");
        }
    }





}
