package model;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import model.Message;
import model.MessageFolder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


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

    @Test
    public void getMessageByIdTest() {
        Message newmsg = null;
        try {
            newmsg = new Message(user,recipient,"test!!", UrgencyLevel.REGULAR);
            f.addNewMessage(newmsg.getMessageID(), newmsg);
            assertTrue(f.containsMessage(newmsg.getMessageID()));
            assertEquals(f.getMessageByID(newmsg.getMessageID()), newmsg);
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }

    }

    @Test
    public void getMessageFolderTest() {
        Message newmsg = null;
        try {
            newmsg = new Message(user,recipient,"test!!", UrgencyLevel.REGULAR);
            HashMap<Integer,Message> messageHashMap = new HashMap<>();
            f.addNewMessage(newmsg.getMessageID(), newmsg);
            messageHashMap.put(newmsg.getMessageID(),newmsg);
            assertEquals(f.getMessageFolder(),messageHashMap);
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        }

    }

    @Test
    void toJsonTest() {
        try {
            Message newmsg = new Message(user,recipient,"test!!",UrgencyLevel.REGULAR);
            newmsg.setEncryptedMessageText(newmsg.encryptMessage("test!!", newmsg.getSharedKey()));
            f.addNewMessage(newmsg.getMessageID(),newmsg);
            JSONArray jsonArray = f.toJson();


            assertNotNull(jsonArray);
            assertEquals(1,jsonArray.length());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            assertEquals(jsonObject.get("SenderUserID"),user.getUserID());
            assertEquals(jsonObject.get("RecipientUserID"),recipient.getUserID());
            assertEquals(jsonObject.get("MessageID"),newmsg.getMessageID());
            assertEquals(jsonObject.get("DecryptedMessageText"),"test!!");
            assertEquals(jsonObject.get("EncryptedMessageText"),newmsg.getEncryptedMessageText());
            assertEquals(stringToSecretKey(jsonObject.getString("SharedKey")),newmsg.getSharedKey());


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

    public static SecretKey stringToSecretKey(String secretKeyString) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        return new SecretKeySpec(decodedKey, "DES");
    }


}
