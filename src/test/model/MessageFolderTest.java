package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import model.Message;
import model.MessageFolder;

import javax.crypto.NoSuchPaddingException;


public class MessageFolderTest {
    User user = new User();
    User recipient = new User();
    MessageFolder f;

    @BeforeEach
    public void setUp() {
        user.createNewUser("testuser","123123");
        recipient.createNewUser("testrecipient","456456");
        f = user.getHub().getMessageFolder();
    }

    @Test
    public void addNewMessageTest() {
        try {
            Message newmsg = new Message(user,recipient,"test!!",UrgencyLevel.REGULAR);
            f.addNewMessage(newmsg.getMessageID(), newmsg);
            Message retrievedMsg = f.getMessageByID(newmsg.getMessageID());
            assertEquals(newmsg,retrievedMsg);
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }
    }

    @Test
    public void containsMessageTest() {
        Message newmsg = null;
        try {
            newmsg = new Message(user,recipient,"test!!", UrgencyLevel.REGULAR);
            f.addNewMessage(newmsg.getMessageID(), newmsg);
            assertTrue(f.containsMessage(newmsg.getMessageID()));
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }


    }

    @Test
    public void constructorTest() {
        HashMap<Integer, Message> messageFolder = f.getMessageFolder();
        assertTrue(messageFolder.isEmpty());
    }

}
