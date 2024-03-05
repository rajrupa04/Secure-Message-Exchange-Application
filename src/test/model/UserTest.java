package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user1;
    private Integer userID;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.createNewUser("user1","157157");
        userID = user1.getUserID();
    }

    @Test
    public void getPasswordTest() {
        assertEquals("157157",user1.getPassword());
    }

    @Test
    public void getUsernameTest() {
        assertEquals("user1",user1.getUsername());
    }

    @Test
    public void getUserIDTest() {
        assertEquals(userID,user1.getUserID());
    }

    @Test
    public void userLogInTest() {
        String un = user1.getUsername();
        String pwd = user1.getPassword();
        Integer uid = user1.getUserID();
        assertTrue(user1.userLogIn(uid,un,pwd));
        assertFalse(user1.userLogIn(uid-1,un,pwd));
        assertFalse(user1.userLogIn(uid,"",pwd));
        assertFalse(user1.userLogIn(uid,un, ""));
    }

    @Test
    public void createNewUserTest() {
        User userNew = new User();
        userNew.createNewUser("newUser","12345");
        assertEquals("newUser",userNew.getUsername());
        assertEquals("12345",userNew.getPassword());
        assertNotNull(userNew.getHub());
        assertNotNull(userNew.getUserID());
    }

    @Test
    public void generateIDTest() {
        assertTrue((user1.generateID() >= 10000000) && (user1.generateID() <= 99999999));
    }

    @Test
    public void getHubTest() {
        assertNotNull(user1.getHub());
    }

    @Test
    public void toJsonTest() {
        Hub h = new Hub();
        user1.setHub(h);
        assertNotNull(user1.getHub());
        JSONObject j = user1.toJson();
        assertNotNull(j);
        assertEquals(user1.getUserID(),j.getInt("UserID"));
        assertNotNull(j.getJSONObject("Hub"));
    }

    @Test
    public void addUserToJsonTest() {
        JSONObject j = user1.addUserToJson();
        assertNotNull(j);
        assertEquals(user1.getUserID(), j.getInt("UserID"));
        assertEquals("user1", j.getString("Username"));
        assertEquals("157157", j.getString("Password"));

    }
}
