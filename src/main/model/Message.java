package model;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import model.Encryption;
import org.json.JSONObject;
import persistence.Writable;

//This class represents a message. Each individual message will have properties like a sender, recipient,
//individual message ID, level of urgency and whether it has been encrypted successfully or not.

public class Message extends Encryption implements Writable {
    private final User sender;
    private final User recipient;
    private final Integer messageID;
    private String decryptedMessageText;
    private String encryptedMessageText;
    private UrgencyLevel urgencyLevel;
    private Boolean encryptionStatus;
    private int minimumLimitForMessageID = 1000;
    private int maximumLimitForMessageID = 999999999;
    private SecretKey sharedKey;

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: constructs a new message with an encryption status, shared key, sender, recipient, message ID
    public Message(User sender, User recipient, String msgText, UrgencyLevel urgencyLevel)
            throws NoSuchAlgorithmException, NoSuchPaddingException {
        super();
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        this.sender = sender;
        this.recipient = recipient;
        this.messageID = generateMessageID();
        this.urgencyLevel = urgencyLevel;
        this.encryptionStatus = false;
        this.sharedKey = kg.generateKey();

    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: sets the encryptedMessageText field
    public void setEncryptedMessageText(String encryptedMessage) {
        this.encryptedMessageText = encryptedMessage;
    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: sets the SharedKey field
    public void setSharedKey(SecretKey sk) {
        this.sharedKey = sk;
    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: gets the encryptedMessageText field
    public String getEncryptedMessageText() {
        return this.encryptedMessageText;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the Sender of the message
    public User getSender() {
        return this.sender;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the recipient of the message
    public User getRecipient() {
        return this.recipient;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the shared key of the message
    public SecretKey getSharedKey() {
        return this.sharedKey;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the message ID of the message
    public Integer getMessageID() {
        return this.messageID;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the urgency level of the message
    public UrgencyLevel getUrgencyLevel() {
        return this.urgencyLevel;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the encryption status of the message
    public boolean getEncryptionStatus() {
        return this.encryptionStatus;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: generates a random message ID within the specified limits
    public Integer generateMessageID() {
        int generatedID = (int)Math.floor(minimumLimitForMessageID
                + ((maximumLimitForMessageID - minimumLimitForMessageID + 1) * Math.random()));

        return generatedID;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("SenderUserID",sender.getUserID());
        json.put("RecipientUserID",recipient.getUserID());
        json.put("MessageID",messageID);
        json.put("DecryptedMessageText",decryptedMessageText);
        json.put("EncryptedMessageText",encryptedMessageText);
        json.put("Urgency Level",urgencyLevel.toString());
        json.put("SharedKey",secretKeyToString(sharedKey));
        
        return json;
    }

    private String secretKeyToString(SecretKey sharedKey) {
        byte[] encodedKey = sharedKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }
}
