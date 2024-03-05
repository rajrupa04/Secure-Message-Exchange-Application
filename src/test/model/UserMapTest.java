package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapTest {
    private User user1 = new User();
    private User user2 = new User();
    private UserMap u;

    @BeforeEach
    public void setUp() {
        user1.createNewUser("user1","987987");
        user2.createNewUser("user2","123123");
        u = new UserMap();
    }

    @Test
    public void constructorTest() {
        assertNotNull(u);
        assertEquals(u.getUserMap().size(),0);
    }

    @Test
    public void addNewUserTest() {
        assertEquals(u.getUserMap().size(),0);
        u.addUser(user1.getUserID(),user1);
        assertEquals(u.getUser(user1.getUserID()),user1);
        assertEquals(u.getUserMap().size(),1);
        u.addUser(user2.getUserID(),user2);
        assertEquals(u.getUserMap().size(),2);
        assertEquals(u.getUser(user2.getUserID()),user2);

    }

    @Test
    public void getUserTest() {
        u.addUser(user1.getUserID(),user1);
        u.addUser(user2.getUserID(),user2);
        assertEquals(user1,u.getUser(user1.getUserID()));
        assertEquals(user2,u.getUser(user2.getUserID()));
    }

    @Test
    public void containsUserTest() {
        u.addUser(user1.getUserID(),user1);
        assertTrue(u.containsUser(user1.getUserID()));
        assertFalse(u.containsUser(user2.getUserID()));
        u.addUser(user2.getUserID(),user2);
        assertTrue(u.containsUser(user1.getUserID()));
        assertTrue(u.containsUser(user2.getUserID()));

    }

    @Test
    public void toJsonTest() {
        u.addUser(user1.getUserID(),user1);
        u.addUser(user2.getUserID(),user2);
        JSONObject userMapJson = u.toJson();
        assertNotNull(userMapJson);
        JSONObject user1Retrieved = userMapJson.getJSONObject(user1.getUserID().toString());
        JSONObject user2Retrieved = userMapJson.getJSONObject(user2.getUserID().toString());
        assertEquals(user1.getUserID(), user1Retrieved.getInt("UserID"));
        assertEquals("user1", user1Retrieved.getString("Username"));
        assertEquals("987987", user1Retrieved.getString("Password"));
        assertEquals(user2.getUserID(), user2Retrieved.getInt("UserID"));
        assertEquals("user2", user2Retrieved.getString("Username"));
        assertEquals("123123", user2Retrieved.getString("Password"));

    }
}
