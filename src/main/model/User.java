package model;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;


//This class represents a user who has an account in the system. Each user will possess
//their own individual user ID, password, and hub.

public class User {
    private Integer userID;
    private String username;
    private String password;
    private SecretKey privateKey;
    private Hub hub;
    private Integer minimumLimitForUserID = 10000000;
    private Integer maximumLimitForUserID = 99999999;

    public void createNewUser(String username, String password) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        this.username = username;
        this.password = password;
        this.userID = generateID();
        this.hub = new Hub();
        this.privateKey = keyGen.generateKey();

    }

    public boolean userLogIn(Integer userID, String username, String password) {
        return (this.userID == userID)
                &&
                (this.username == username)
                &&
                (this.password == password);
    }

    public Integer getUserID() {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Integer generateID() {
        int generatedID = (int)Math.floor(minimumLimitForUserID
                + ((maximumLimitForUserID - minimumLimitForUserID + 1) * Math.random()));

        return generatedID;
    }

    public Hub getHub() {
        return this.hub;
    }


}
