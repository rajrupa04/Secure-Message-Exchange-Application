package model;


//This class stores information about users who make an account.

import java.util.HashMap;

public class UserMap {
    private HashMap<Integer, User> userMap;

    public UserMap() {
        userMap = new HashMap<>();
    }

    //EFFECTS: Adds a new entry to the hashmap with key = userID and value = user
    public void addUser(Integer userID, User user) {
        userMap.put(userID, user);
    }

    public User getUser(Integer userID) {
        return userMap.get(userID);
    }

    //EFFECTS: checks if the user has an account in the system
    public boolean containsUser(Integer userID) {
        return userMap.containsKey(userID);

    }

    public HashMap<Integer, User> getUserMap() {
        return userMap;
    }
}
