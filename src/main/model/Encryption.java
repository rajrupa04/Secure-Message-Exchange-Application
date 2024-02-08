package model;

import model.MessageFolder;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//This class represents the process of encryption. The encrypting and decrypting of messages by the sender
//and recipient respectively will be occurring here.

public class Encryption {
    private static boolean encryptionStatus;
    private Cipher cipher;

    public Encryption() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.encryptionStatus = false;
        this.cipher = Cipher.getInstance("DES");
    }

    public String encryptMessage(String message, SecretKey privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, UnsupportedEncodingException {

        byte[] byteString = message.getBytes("UTF-16");
        try {
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);
            byte[] encryptedText = cipher.doFinal(byteString);
            String encryptedMessage = new String(encryptedText);
            this.encryptionStatus = true;
            return encryptedMessage;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    public String decryptMessage(String encryptedMessage, SecretKey privateKey) {

        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedText = cipher.doFinal(encryptedMessage.getBytes("UTF-16"));
            String decryptedMessage = new String(decryptedText);
            this.encryptionStatus = false;
            return decryptedMessage;
        } catch (InvalidKeyException | IllegalBlockSizeException
                 | UnsupportedEncodingException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean getEncryptionStatus(Integer messageID) {
        return this.encryptionStatus;
    }
}
