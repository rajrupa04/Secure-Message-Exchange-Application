package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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



}
