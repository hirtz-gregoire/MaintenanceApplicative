package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventFactoryTest {
    
    private EventFactory eventFactory;
    
    @BeforeEach
    void setUp() {
        eventFactory = new EventFactory();
    }
    
    @Test
    void testCreatePersonalEvent() {
        String title = "Test Personal Event";
        String owner = "Test Owner";
        LocalDateTime dateTime = LocalDateTime.of(2023, 3, 15, 10, 0);
        int duration = 60;
        
        Event event = eventFactory.createEvent(TypeEvent.RDV_PERSONNEL, title, owner, dateTime, duration, "", "", 0);
        
        assertNotNull(event);
        assertEquals(TypeEvent.RDV_PERSONNEL, event.getType());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(dateTime, event.getStartDate().getDateTime());
        assertEquals(duration, event.getDuration().getMinutes());
        assertTrue(event instanceof PersonalEvent);
    }
    
    @Test
    void testCreateMeetingEvent() {
        String title = "Test Meeting";
        String owner = "Test Owner";
        LocalDateTime dateTime = LocalDateTime.of(2023, 3, 15, 10, 0);
        int duration = 60;
        String place = "Meeting Room";
        String participants = "John, Jane, Bob";
        
        Event event = eventFactory.createEvent(TypeEvent.REUNION, title, owner, dateTime, duration, place, participants, 0);
        
        assertNotNull(event);
        assertEquals(TypeEvent.REUNION, event.getType());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(dateTime, event.getStartDate().getDateTime());
        assertEquals(duration, event.getDuration().getMinutes());
        assertTrue(event instanceof MeetingEvent);
        
        MeetingEvent meetingEvent = (MeetingEvent) event;
        assertEquals(place, meetingEvent.getPlace().getValue());
        assertEquals(participants, meetingEvent.getParticipants().toString());
    }
    
    @Test
    void testCreatePeriodicEvent() {
        String title = "Test Periodic Event";
        String owner = "Test Owner";
        LocalDateTime dateTime = LocalDateTime.of(2023, 3, 15, 10, 0);
        int frequency = 7;
        
        Event event = eventFactory.createEvent(TypeEvent.PERIODIQUE, title, owner, dateTime, 0, "", "", frequency);
        
        assertNotNull(event);
        assertEquals(TypeEvent.PERIODIQUE, event.getType());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(dateTime, event.getStartDate().getDateTime());
        assertTrue(event instanceof PeriodicEvent);
        
        PeriodicEvent periodicEvent = (PeriodicEvent) event;
        assertEquals(frequency, periodicEvent.getFrequency().getDays());
    }
    
    @Test
    void testCreateEventWithInvalidTypeThrowsException() {
        TypeEvent nullType = null;
        assertThrows(IllegalArgumentException.class, () -> {
            eventFactory.createEvent(nullType, "Test Event", "Test Owner", LocalDateTime.now(), 60, "", "", 0);
        });
    }
}
