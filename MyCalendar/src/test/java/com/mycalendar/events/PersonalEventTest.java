package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PersonalEventTest {

    @Test
    void testConstructorWithAllParameters() {
        EventId id = new EventId("test-id");
        TitleEvent title = new TitleEvent("Test Event");
        OwnerEvent owner = new OwnerEvent("Test Owner");
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        DurationEvent duration = new DurationEvent(60);
        
        PersonalEvent event = new PersonalEvent(id, title, owner, startDate, duration);
        
        assertEquals(id, event.getId());
        assertEquals(title, event.getTitle());
        assertEquals(owner, event.getOwner());
        assertEquals(startDate, event.getStartDate());
        assertEquals(duration, event.getDuration());
        assertEquals(TypeEvent.RDV_PERSONNEL, event.getType());
    }
    
    @Test
    void testConstructorWithStringParameters() {
        String title = "Test Event";
        String owner = "Test Owner";
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        DurationEvent duration = new DurationEvent(60);
        
        PersonalEvent event = new PersonalEvent(title, owner, startDate, duration);
        
        assertNotNull(event.getId());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(startDate, event.getStartDate());
        assertEquals(duration, event.getDuration());
        assertEquals(TypeEvent.RDV_PERSONNEL, event.getType());
    }
    
    @Test
    void testDescription() {
        PersonalEvent event = new PersonalEvent(
            "Test Event",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        String description = event.description();
        assertTrue(description.contains("RDV"));
        assertTrue(description.contains("Test Event"));
        assertTrue(description.contains("2023-03-15T10:00"));
    }
    
    @Test
    void testGetEndDate() {
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        DurationEvent duration = new DurationEvent(60);
        
        PersonalEvent event = new PersonalEvent("Test Event", "Test Owner", startDate, duration);
        
        DateEvent endDate = event.getEndDate();
        assertEquals(LocalDateTime.of(2023, 3, 15, 11, 0), endDate.getDateTime());
    }
    
    @Test
    void testConflictsWith() {
        // Événement 1: 10:00 - 11:00
        PersonalEvent event1 = new PersonalEvent(
            "Event 1",
            "Owner 1",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        // Événement 2: 10:30 - 11:30 (conflit)
        PersonalEvent event2 = new PersonalEvent(
            "Event 2",
            "Owner 2",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 30)),
            new DurationEvent(60)
        );
        
        // Événement 3: 11:00 - 12:00 (pas de conflit)
        PersonalEvent event3 = new PersonalEvent(
            "Event 3",
            "Owner 3",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 11, 0)),
            new DurationEvent(60)
        );
        
        // Événement 4: 09:00 - 10:00 (pas de conflit)
        PersonalEvent event4 = new PersonalEvent(
            "Event 4",
            "Owner 4",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 9, 0)),
            new DurationEvent(60)
        );
        
        assertTrue(event1.conflictsWith(event2));
        assertTrue(event2.conflictsWith(event1));
        
        assertFalse(event1.conflictsWith(event3));
        assertFalse(event3.conflictsWith(event1));
        
        assertFalse(event1.conflictsWith(event4));
        assertFalse(event4.conflictsWith(event1));
    }
    
    @Test
    void testOccursInPeriod() {
        PersonalEvent event = new PersonalEvent(
            "Test Event",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        // Période 1: 2023-03-15 00:00 - 2023-03-16 00:00 (l'événement est dans cette période)
        DateEvent start1 = new DateEvent(LocalDateTime.of(2023, 3, 15, 0, 0));
        DateEvent end1 = new DateEvent(LocalDateTime.of(2023, 3, 16, 0, 0));
        
        // Période 2: 2023-03-16 00:00 - 2023-03-17 00:00 (l'événement n'est pas dans cette période)
        DateEvent start2 = new DateEvent(LocalDateTime.of(2023, 3, 16, 0, 0));
        DateEvent end2 = new DateEvent(LocalDateTime.of(2023, 3, 17, 0, 0));
        
        assertTrue(event.occursInPeriod(start1, end1));
        assertFalse(event.occursInPeriod(start2, end2));
    }
}
