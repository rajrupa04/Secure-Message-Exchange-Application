package model;

//contains tests for the Encryption class in the model package

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {
    private Encryption eTest;

    @BeforeEach
    public void setUp() throws NoSuchPaddingException, NoSuchAlgorithmException {
        eTest = new Encryption();
    }



}