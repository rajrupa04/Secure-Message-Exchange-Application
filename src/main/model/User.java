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

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: constructs a new User with a username, password, user ID and personal hub
    public void createNewUser(String username, String password) {

        this.username = username;
        this.password = password;
        this.userID = generateID();
        this.hub = new Hub();


    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: validates a user's credentials while logging in, checks if the userID, username and password match.
    public boolean userLogIn(Integer userID, String username, String password) {
        return this.userID.equals(userID)
                && this.username.equals(username)
                && this.password.equals(password);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the user ID of the current user
    public Integer getUserID() {
        return this.userID;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the username of the current user
    public String getUsername() {
        return this.username;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the password of the currenrt user
    public String getPassword() {
        return this.password;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: generates a random user ID within the specified limits
    public Integer generateID() {
        int generatedID = (int)Math.floor(minimumLimitForUserID
                + ((maximumLimitForUserID - minimumLimitForUserID + 1) * Math.random()));

        return generatedID;
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the user's personal hub
    public Hub getHub() {
        return this.hub;
    }




}
