package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {
    private Notification notif;

    @BeforeEach
    public void setUp() {
        notif = new Notification();
    }

    @Test
    public void constructorTest() {
        assertTrue(notif.getHashMapOfNotifications().isEmpty());
    }

    @Test
    public void addNotificationTest() {
        assertTrue(notif.getHashMapOfNotifications().isEmpty());
        notif.addNotification(UrgencyLevel.REGULAR,"regular notification 1");
        assertFalse(notif.getHashMapOfNotifications().isEmpty());
        assertEquals(notif.getHashMapOfNotifications().size(),1);
        notif.addNotification(UrgencyLevel.URGENT,"urgent notification 1");
        assertEquals(notif.getHashMapOfNotifications().size(),2);
        notif.addNotification(UrgencyLevel.URGENT,"urgent notification 2");
        assertEquals(notif.getHashMapOfNotifications().size(),2);

    }

    @Test
    public void removeNotificationTest() {
        notif.addNotification(UrgencyLevel.REGULAR,"regular notification 1");
        notif.addNotification(UrgencyLevel.URGENT,"urgent notification 1");
        notif.removeNotification(UrgencyLevel.REGULAR);
        assertEquals(notif.getNotification(UrgencyLevel.REGULAR),null);
        assertEquals(notif.getNotification(UrgencyLevel.URGENT),"urgent notification 1");
        notif.removeNotification(UrgencyLevel.URGENT);
        assertEquals(notif.getNotification(UrgencyLevel.REGULAR),null);
        assertEquals(notif.getNotification(UrgencyLevel.URGENT),null);
    }

    @Test
    public void clearNotificationTest() {
        notif.addNotification(UrgencyLevel.REGULAR,"regular notification 1");
        notif.addNotification(UrgencyLevel.URGENT,"urgent notification 1");
        notif.clearNotifications();
        assertTrue(notif.getHashMapOfNotifications().isEmpty());

    }

    @Test
    public void updateNotificationTest() {
        notif.addNotification(UrgencyLevel.REGULAR,"regular notification 1");
        notif.updateNotification(UrgencyLevel.REGULAR,"new regular notification 1");
        assertEquals("new regular notification 1",notif.getNotification(UrgencyLevel.REGULAR));
        notif.updateNotification(UrgencyLevel.URGENT,"urgent notification 1");
        assertFalse(notif.getHashMapOfNotifications().containsKey(UrgencyLevel.URGENT));

    }
}
