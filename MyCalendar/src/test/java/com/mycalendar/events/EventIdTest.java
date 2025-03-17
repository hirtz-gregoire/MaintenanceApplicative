package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EventIdTest {

    @Test
    void testDefaultConstructor() {
        EventId eventId = new EventId();
        assertNotNull(eventId.getValue());
        assertFalse(eventId.getValue().isEmpty());
    }
    
    @Test
    void testConstructorWithValidId() {
        String id = "test-id-123";
        EventId eventId = new EventId(id);
        assertEquals(id, eventId.getValue());
    }
    
    @Test
    void testConstructorWithNullIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new EventId(null));
    }
    
    @Test
    void testConstructorWithEmptyIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new EventId(""));
    }
    
    @Test
    void testConstructorWithBlankIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new EventId("   "));
    }
    
    @Test
    void testConstructorTrimsId() {
        EventId eventId = new EventId("  test-id-123  ");
        assertEquals("test-id-123", eventId.getValue());
    }
    
    @Test
    void testEquals() {
        EventId eventId1 = new EventId("test-id-123");
        EventId eventId2 = new EventId("test-id-123");
        EventId eventId3 = new EventId("test-id-456");
        
        assertEquals(eventId1, eventId2);
        assertNotEquals(eventId1, eventId3);
        assertNotEquals(eventId1, null);
        assertNotEquals(eventId1, "not an EventId");
    }
    
    @Test
    void testHashCode() {
        EventId eventId1 = new EventId("test-id-123");
        EventId eventId2 = new EventId("test-id-123");
        
        assertEquals(eventId1.hashCode(), eventId2.hashCode());
    }
    
    @Test
    void testToString() {
        String id = "test-id-123";
        EventId eventId = new EventId(id);
        assertEquals(id, eventId.toString());
    }
    
    @Test
    void testUniqueIds() {
        EventId eventId1 = new EventId();
        EventId eventId2 = new EventId();
        
        assertNotEquals(eventId1.getValue(), eventId2.getValue());
    }
}
