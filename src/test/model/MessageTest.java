package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
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




}
