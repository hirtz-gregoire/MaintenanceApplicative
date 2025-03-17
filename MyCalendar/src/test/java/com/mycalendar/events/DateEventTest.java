package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DateEventTest {

    @Test
    void testConstructorWithLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateEvent dateEvent = new DateEvent(now);
        assertEquals(now, dateEvent.getDateTime());
    }
    
    @Test
    void testConstructorWithYearMonthDayHourMinute() {
        DateEvent dateEvent = new DateEvent(2023, 3, 15, 10, 30);
        assertEquals(LocalDateTime.of(2023, 3, 15, 10, 30), dateEvent.getDateTime());
    }
    
    @Test
    void testConstructorWithNullThrowsException() {
        assertThrows(NullPointerException.class, () -> new DateEvent(null));
    }
    
    @Test
    void testPlusMinutes() {
        DateEvent dateEvent = new DateEvent(2023, 3, 15, 10, 30);
        DurationEvent duration = new DurationEvent(30);
        DateEvent newDateEvent = dateEvent.plusMinutes(duration);
        
        assertEquals(LocalDateTime.of(2023, 3, 15, 11, 0), newDateEvent.getDateTime());
        // Vérifier que l'objet original n'a pas été modifié (immuabilité)
        assertEquals(LocalDateTime.of(2023, 3, 15, 10, 30), dateEvent.getDateTime());
    }
    
    @Test
    void testPlusDays() {
        DateEvent dateEvent = new DateEvent(2023, 3, 15, 10, 30);
        DateEvent newDateEvent = dateEvent.plusDays(2);
        
        assertEquals(LocalDateTime.of(2023, 3, 17, 10, 30), newDateEvent.getDateTime());
        // Vérifier que l'objet original n'a pas été modifié (immuabilité)
        assertEquals(LocalDateTime.of(2023, 3, 15, 10, 30), dateEvent.getDateTime());
    }
    
    @Test
    void testIsBefore() {
        DateEvent dateEvent1 = new DateEvent(2023, 3, 15, 10, 30);
        DateEvent dateEvent2 = new DateEvent(2023, 3, 15, 11, 30);
        
        assertTrue(dateEvent1.isBefore(dateEvent2));
        assertFalse(dateEvent2.isBefore(dateEvent1));
        assertFalse(dateEvent1.isBefore(dateEvent1)); // Même date
    }
    
    @Test
    void testIsAfter() {
        DateEvent dateEvent1 = new DateEvent(2023, 3, 15, 10, 30);
        DateEvent dateEvent2 = new DateEvent(2023, 3, 15, 11, 30);
        
        assertFalse(dateEvent1.isAfter(dateEvent2));
        assertTrue(dateEvent2.isAfter(dateEvent1));
        assertFalse(dateEvent1.isAfter(dateEvent1)); // Même date
    }
    
    @Test
    void testEquals() {
        DateEvent dateEvent1 = new DateEvent(2023, 3, 15, 10, 30);
        DateEvent dateEvent2 = new DateEvent(2023, 3, 15, 10, 30);
        DateEvent dateEvent3 = new DateEvent(2023, 3, 15, 11, 30);
        
        assertEquals(dateEvent1, dateEvent2);
        assertNotEquals(dateEvent1, dateEvent3);
        assertNotEquals(dateEvent1, null);
        assertNotEquals(dateEvent1, "not a DateEvent");
    }
    
    @Test
    void testHashCode() {
        DateEvent dateEvent1 = new DateEvent(2023, 3, 15, 10, 30);
        DateEvent dateEvent2 = new DateEvent(2023, 3, 15, 10, 30);
        
        assertEquals(dateEvent1.hashCode(), dateEvent2.hashCode());
    }
    
    @Test
    void testToString() {
        DateEvent dateEvent = new DateEvent(2023, 3, 15, 10, 30);
        assertEquals(LocalDateTime.of(2023, 3, 15, 10, 30).toString(), dateEvent.toString());
    }
}
