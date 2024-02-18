package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private Message mTest;
    private User sender = new User();
    private User receiver = new User();
    private Integer messageID;
    private String msgtext;

    @BeforeEach
    public void setUp() throws NoSuchPaddingException, NoSuchAlgorithmException {
        sender.createNewUser("sender","123123");
        receiver.createNewUser("recipient","456456");
        mTest = new Message(sender,receiver,msgtext,UrgencyLevel.REGULAR);
    }

    @Test
    public void constructorTest() {
        assertEquals(sender, mTest.getSender());
        assertEquals(receiver, mTest.getRecipient());
        assertNotNull(mTest.getMessageID());
        assertEquals(UrgencyLevel.REGULAR, mTest.getUrgencyLevel());
        assertFalse(mTest.getEncryptionStatus());
        assertNotNull(mTest.getSharedKey());
    }

    @Test
    public void generateMessageIDTest() {
        assertTrue((mTest.getMessageID() >= 1000) && (mTest.getMessageID() <= 999999999));
    }

    @Test
    public void setEncryptedMessageTextTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            Encryption e = new Encryption();
            String encrypted = e.encryptMessage("test",msg.getSharedKey());
            msg.setEncryptedMessageText(encrypted);
            assertNotNull(msg.getEncryptedMessageText());
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
    public void getEncryptedMessageTextTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            Encryption e = new Encryption();
            String encrypted = e.encryptMessage("test",msg.getSharedKey());
            msg.setEncryptedMessageText(encrypted);
            assertEquals(encrypted,msg.getEncryptedMessageText());
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
    public void getSenderTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            assertEquals(sender,msg.getSender());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }
    }

    @Test
    public void getRecipientTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            assertEquals(receiver,msg.getRecipient());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }
    }

    @Test
    public void getSharedKeyTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            assertNotNull(msg.getSharedKey());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }
    }

    @Test
    public void getMessageIDTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            assertNotNull(msg.getMessageID());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }
    }

    @Test
    public void getUrgencyLevel() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            assertEquals(UrgencyLevel.REGULAR,msg.getUrgencyLevel());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }
    }

    @Test
    public void getEncryptionStatusTest() {
        try {
            Message msg = new Message(sender,receiver,"test",UrgencyLevel.REGULAR);
            assertFalse(msg.getEncryptionStatus());
            Integer mID = sender.getHub().sendMessage(sender,receiver,"test message",UrgencyLevel.REGULAR);
            assertFalse(receiver.getHub().getMessageFolder().getMessageByID(mID).getEncryptionStatus());
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
