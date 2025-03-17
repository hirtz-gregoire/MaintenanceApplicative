package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class MeetingEventTest {

    @Test
    void testConstructorWithAllParameters() {
        EventId id = new EventId("test-id");
        TitleEvent title = new TitleEvent("Test Meeting");
        OwnerEvent owner = new OwnerEvent("Test Owner");
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        DurationEvent duration = new DurationEvent(60);
        PlaceEvent place = new PlaceEvent("Meeting Room");
        ParticipantEvent participants = new ParticipantEvent("John, Jane, Bob");
        
        MeetingEvent event = new MeetingEvent(id, title, owner, startDate, duration, place, participants);
        
        assertEquals(id, event.getId());
        assertEquals(title, event.getTitle());
        assertEquals(owner, event.getOwner());
        assertEquals(startDate, event.getStartDate());
        assertEquals(duration, event.getDuration());
        assertEquals(place, event.getPlace());
        assertEquals(participants, event.getParticipants());
        assertEquals(TypeEvent.REUNION, event.getType());
    }
    
    @Test
    void testConstructorWithStringParameters() {
        String title = "Test Meeting";
        String owner = "Test Owner";
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        DurationEvent duration = new DurationEvent(60);
        String place = "Meeting Room";
        String participants = "John, Jane, Bob";
        
        MeetingEvent event = new MeetingEvent(title, owner, startDate, duration, place, participants);
        
        assertNotNull(event.getId());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(startDate, event.getStartDate());
        assertEquals(duration, event.getDuration());
        assertEquals(place, event.getPlace().getValue());
        assertEquals(participants, event.getParticipants().toString());
        assertEquals(TypeEvent.REUNION, event.getType());
    }
    
    @Test
    void testDescription() {
        MeetingEvent event = new MeetingEvent(
            "Test Meeting",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60),
            "Meeting Room",
            "John, Jane, Bob"
        );
        
        String description = event.description();
        assertTrue(description.contains("Réunion"));
        assertTrue(description.contains("Test Meeting"));
        assertTrue(description.contains("Meeting Room"));
        assertTrue(description.contains("John, Jane, Bob"));
    }
    
    @Test
    void testGetEndDate() {
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        DurationEvent duration = new DurationEvent(60);
        
        MeetingEvent event = new MeetingEvent(
            "Test Meeting",
            "Test Owner",
            startDate,
            duration,
            "Meeting Room",
            "John, Jane, Bob"
        );
        
        DateEvent endDate = event.getEndDate();
        assertEquals(LocalDateTime.of(2023, 3, 15, 11, 0), endDate.getDateTime());
    }
    
    @Test
    void testConflictsWith() {
        // Réunion 1: 10:00 - 11:00
        MeetingEvent event1 = new MeetingEvent(
            "Meeting 1",
            "Owner 1",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60),
            "Room 1",
            "John, Jane"
        );
        
        // Réunion 2: 10:30 - 11:30 (conflit)
        MeetingEvent event2 = new MeetingEvent(
            "Meeting 2",
            "Owner 2",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 30)),
            new DurationEvent(60),
            "Room 2",
            "Bob, Alice"
        );
        
        // Réunion 3: 11:00 - 12:00 (pas de conflit)
        MeetingEvent event3 = new MeetingEvent(
            "Meeting 3",
            "Owner 3",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 11, 0)),
            new DurationEvent(60),
            "Room 3",
            "Charlie, Dave"
        );
        
        assertTrue(event1.conflictsWith(event2));
        assertTrue(event2.conflictsWith(event1));
        
        assertFalse(event1.conflictsWith(event3));
        assertFalse(event3.conflictsWith(event1));
    }
    
    @Test
    void testOccursInPeriod() {
        MeetingEvent event = new MeetingEvent(
            "Test Meeting",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60),
            "Meeting Room",
            "John, Jane, Bob"
        );
        
        // Période 1: 2023-03-15 00:00 - 2023-03-16 00:00 (la réunion est dans cette période)
        DateEvent start1 = new DateEvent(LocalDateTime.of(2023, 3, 15, 0, 0));
        DateEvent end1 = new DateEvent(LocalDateTime.of(2023, 3, 16, 0, 0));
        
        // Période 2: 2023-03-16 00:00 - 2023-03-17 00:00 (la réunion n'est pas dans cette période)
        DateEvent start2 = new DateEvent(LocalDateTime.of(2023, 3, 16, 0, 0));
        DateEvent end2 = new DateEvent(LocalDateTime.of(2023, 3, 17, 0, 0));
        
        assertTrue(event.occursInPeriod(start1, end1));
        assertFalse(event.occursInPeriod(start2, end2));
    }
}
