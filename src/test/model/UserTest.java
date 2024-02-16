package model;

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
}
