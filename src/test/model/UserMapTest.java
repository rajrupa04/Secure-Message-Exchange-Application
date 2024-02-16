package model;

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
}
