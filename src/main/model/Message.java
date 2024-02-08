package model;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import model.Encryption;

//This class represents a message. Each individual message will have properties like a sender, recipient,
//individual message ID, level of urgency and whether it has been encrypted successfully or not.

public class Message {
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

    public Message(User sender, User recipient, String msgText, UrgencyLevel urgencyLevel)
            throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        this.sender = sender;
        this.recipient = recipient;
        this.messageID = generateMessageID();
        this.urgencyLevel = urgencyLevel;
        this.encryptionStatus = findEncryptionStatus(this.messageID);
        this.sharedKey = kg.generateKey();

    }

    public void setEncryptedMessageText(String encryptedMessage) {
        this.encryptedMessageText = encryptedMessage;
    }

    public String getEncryptedMessageText() {
        return this.encryptedMessageText;
    }

    public Boolean findEncryptionStatus(Integer messageID) {
        return true;

    }

    public User getSender() {
        return this.sender;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public SecretKey getSharedKey() {
        return this.sharedKey;
    }

    public Integer getMessageID() {
        return this.messageID;
    }

    public UrgencyLevel getUrgencyLevel() {
        return this.urgencyLevel;
    }

    public boolean getEncryptionStatus() {
        return this.encryptionStatus;
    }

    private Integer generateMessageID() {
        int generatedID = (int)Math.floor(minimumLimitForMessageID
                + ((maximumLimitForMessageID - minimumLimitForMessageID + 1) * Math.random()));

        return generatedID;
    }

}
