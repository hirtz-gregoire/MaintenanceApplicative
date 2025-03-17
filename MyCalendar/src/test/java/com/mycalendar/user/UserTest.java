package com.mycalendar.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testConstructorWithValidParameters() {
        User user = new User("john.doe", "password123");
        assertEquals("john.doe", user.getUsername());
        assertTrue(user.checkPassword("password123"));
    }
    
    @Test
    void testConstructorWithNullUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "password123"));
    }
    
    @Test
    void testConstructorWithEmptyUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "password123"));
    }
    
    @Test
    void testConstructorWithBlankUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User("   ", "password123"));
    }
    
    @Test
    void testConstructorWithNullPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User("john.doe", null));
    }
    
    @Test
    void testConstructorWithEmptyPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User("john.doe", ""));
    }
    
    @Test
    void testConstructorWithBlankPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User("john.doe", "   "));
    }
    
    @Test
    void testConstructorTrimsUsername() {
        User user = new User("  john.doe  ", "password123");
        assertEquals("john.doe", user.getUsername());
    }
    
    @Test
    void testCheckPasswordWithCorrectPassword() {
        User user = new User("john.doe", "password123");
        assertTrue(user.checkPassword("password123"));
    }
    
    @Test
    void testCheckPasswordWithIncorrectPassword() {
        User user = new User("john.doe", "password123");
        assertFalse(user.checkPassword("wrongpassword"));
    }
    
    @Test
    void testEquals() {
        User user1 = new User("john.doe", "password123");
        User user2 = new User("john.doe", "differentpassword");
        User user3 = new User("jane.doe", "password123");
        
        // Deux utilisateurs avec le même nom d'utilisateur sont considérés comme égaux
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, "not a User");
    }
    
    @Test
    void testHashCode() {
        User user1 = new User("john.doe", "password123");
        User user2 = new User("john.doe", "differentpassword");
        
        assertEquals(user1.hashCode(), user2.hashCode());
    }
    
    @Test
    void testToString() {
        User user = new User("john.doe", "password123");
        assertEquals("john.doe", user.toString());
    }
}
