package model;

//contains tests for the Encryption class in the model package

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.*;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {
    private Encryption eTest;
    String message = "test!";


    @BeforeEach
    public void setUp() {
        try {
            eTest = new Encryption();
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        }

    }

    @Test
    public void encryptMessageSuccessTest() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("DES");
            SecretKey privateKey = keyGen.generateKey();
            String encryptedMessage = eTest.encryptMessage(message, privateKey);

            assertFalse(encryptedMessage.equals(null));
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException");
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            fail("Unexpected Exceptions thrown!!");
        }

    }

    @Test
    public void decryptMessageSuccessTest() {
        String message = "test!";
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("DES");
            SecretKey privateKey = keyGen.generateKey();
            String encryptedMessage = eTest.encryptMessage(message, privateKey);
            String decryptedMessage = eTest.decryptMessage(encryptedMessage, privateKey);

            assertNotNull(decryptedMessage);
            assertEquals(message, decryptedMessage);

        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            fail("Unexpected NoSuchPaddingException");
        } catch (UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            fail("Unexpected UnsupportedEncodingException");
        }


    }
}