package model;

import model.MessageFolder;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
            NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {

        byte[] byteString = message.getBytes(StandardCharsets.UTF_8);
        String encryptedMessage = "";
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        byte[] encryptedText = cipher.doFinal(byteString);
        try {

            encryptedMessage = new String(encryptedText);
            this.encryptionStatus = true;
            return encryptedMessage;

        } finally {
            return Base64.getEncoder().encodeToString(encryptedText);
        }


    }

    public String decryptMessage(String encryptedMessage, SecretKey privateKey) {

        String decryptedMessage = "";
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedMessage = new String(decryptedBytes,StandardCharsets.UTF_8);
            this.encryptionStatus = false;

        } catch (InvalidKeyException e) {
            System.err.println("Invalid Key while Decryption!");
        } catch (IllegalBlockSizeException e) {
            System.err.println("Illegal Block Size!");
        } catch (BadPaddingException e) {
            System.err.println("Bad Padding!");
        } finally {
            return decryptedMessage;
        }


    }




}
