package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DurationEventTest {

    @Test
    void testConstructorWithValidDuration() {
        DurationEvent durationEvent = new DurationEvent(30);
        assertEquals(30, durationEvent.getMinutes());
    }
    
    @Test
    void testConstructorWithZeroDuration() {
        DurationEvent durationEvent = new DurationEvent(0);
        assertEquals(0, durationEvent.getMinutes());
    }
    
    @Test
    void testConstructorWithNegativeDurationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new DurationEvent(-1));
    }
    
    @Test
    void testEquals() {
        DurationEvent durationEvent1 = new DurationEvent(30);
        DurationEvent durationEvent2 = new DurationEvent(30);
        DurationEvent durationEvent3 = new DurationEvent(60);
        
        assertEquals(durationEvent1, durationEvent2);
        assertNotEquals(durationEvent1, durationEvent3);
        assertNotEquals(durationEvent1, null);
        assertNotEquals(durationEvent1, "not a DurationEvent");
    }
    
    @Test
    void testHashCode() {
        DurationEvent durationEvent1 = new DurationEvent(30);
        DurationEvent durationEvent2 = new DurationEvent(30);
        
        assertEquals(durationEvent1.hashCode(), durationEvent2.hashCode());
    }
    
    @Test
    void testToStringMinutesOnly() {
        DurationEvent durationEvent = new DurationEvent(30);
        assertEquals("30min", durationEvent.toString());
    }
    
    @Test
    void testToStringHoursAndMinutes() {
        DurationEvent durationEvent = new DurationEvent(90);
        assertEquals("1h30min", durationEvent.toString());
    }
    
    @Test
    void testToStringHoursOnly() {
        DurationEvent durationEvent = new DurationEvent(120);
        assertEquals("2h", durationEvent.toString());
    }
}
