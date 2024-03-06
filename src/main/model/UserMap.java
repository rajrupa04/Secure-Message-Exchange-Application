package model;


//This class stores information about users who make an account.

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class UserMap {
    private HashMap<Integer, User> userMap;

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: constructs a new userMap with an empty HashMap
    public UserMap() {
        userMap = new HashMap<>();
    }

    //REQUIRES: none
    //MODIFIES: this
    //EFFECTS: Adds a new entry to the hashmap with key = userID and value = user
    public void addUser(Integer userID, User user) {
        userMap.put(userID, user);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: gets the user with the specified user ID from the userMap
    public User getUser(Integer userID) {
        return userMap.get(userID);
    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: checks if the user has an account in the system
    public boolean containsUser(Integer userID) {
        return userMap.containsKey(userID);

    }

    //REQUIRES: none
    //MODIFIES: none
    //EFFECTS: returns the userMap (hashMap)
    public HashMap<Integer, User> getUserMap() {
        return userMap;
    }


    //EFFECTS: converts the userMap to a Json Object
    public JSONObject toJson() {
        JSONObject userMapJson = new JSONObject();
        for (Integer userID : userMap.keySet()) {
            userMapJson.put(userID.toString(),userMap.get(userID).addUserToJson());
        }
        return userMapJson;
    }
}
