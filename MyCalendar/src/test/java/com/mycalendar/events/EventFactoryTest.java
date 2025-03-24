package com.mycalendar.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EventFactoryTest {
    private EventFactory factory;
    
    @BeforeEach
    public void setUp() {
        factory = new EventFactory();
    }
    
    @Test
    public void testCreatePersonalEvent() {
        // Given
        String title = "Rendez-vous médecin";
        String owner = "John";
        LocalDateTime startDate = LocalDateTime.of(2025, 4, 15, 10, 0);
        int durationMinutes = 60;
        
        // When
        Event event = factory.createEvent(TypeEvent.RDV_PERSONNEL, title, owner, startDate, durationMinutes, null, null, 0);
        
        // Then
        assertNotNull(event);
        assertEquals(TypeEvent.RDV_PERSONNEL, event.getType());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(startDate, event.getStartDate().getDateTime());
        assertEquals(durationMinutes, event.getDuration().getMinutes());
        assertTrue(event instanceof PersonalEvent);
    }
    
    @Test
    public void testCreateMeetingEvent() {
        // Given
        String title = "Réunion projet";
        String owner = "John";
        LocalDateTime startDate = LocalDateTime.of(2025, 4, 15, 14, 0);
        int durationMinutes = 120;
        String place = "Salle A";
        String participants = "Alice, Bob";
        
        // When
        Event event = factory.createEvent(TypeEvent.REUNION, title, owner, startDate, durationMinutes, place, participants, 0);
        
        // Then
        assertNotNull(event);
        assertEquals(TypeEvent.REUNION, event.getType());
        assertTrue(event instanceof MeetingEvent);
        MeetingEvent meetingEvent = (MeetingEvent) event;
        assertEquals(place, meetingEvent.getPlace().getValue());
        assertEquals(participants, meetingEvent.getParticipants().toString());
    }
    
    @Test
    public void testCreatePeriodicEvent() {
        // Given
        String title = "Événement hebdomadaire";
        String owner = "John";
        LocalDateTime startDate = LocalDateTime.of(2025, 4, 15, 9, 0);
        int frequencyDays = 7;
        
        // When
        Event event = factory.createEvent(TypeEvent.PERIODIQUE, title, owner, startDate, 0, null, null, frequencyDays);
        
        // Then
        assertNotNull(event);
        assertEquals(TypeEvent.PERIODIQUE, event.getType());
        assertTrue(event instanceof PeriodicEvent);
        PeriodicEvent periodicEvent = (PeriodicEvent) event;
        assertEquals(frequencyDays, periodicEvent.getFrequency().getDays());
    }
    
    @Test
    public void testCreateTaskEvent() {
        // Given
        String title = "Terminer le rapport";
        String owner = "John";
        LocalDateTime deadline = LocalDateTime.of(2025, 4, 22, 17, 0);
        String priority = "HIGH";
        
        // When
        Event event = factory.createEvent(TypeEvent.TASK, title, owner, deadline, 0, priority, null, 0);
        
        // Then
        assertNotNull(event);
        assertEquals(TypeEvent.TASK, event.getType());
        assertTrue(event instanceof TaskEvent);
        TaskEvent taskEvent = (TaskEvent) event;
        assertEquals(priority, taskEvent.getPriority().getValue());
    }
    
    @Test
    public void testCreateReminderEvent() {
        // Given
        String title = "Rappel important";
        String owner = "John";
        LocalDateTime date = LocalDateTime.of(2025, 4, 15, 10, 0);
        String message = "Ne pas oublier de prendre les documents";
        
        // When
        Event event = factory.createEvent(TypeEvent.RAPPEL, title, owner, date, 0, message, null, 0);
        
        // Then
        assertNotNull(event);
        assertEquals(TypeEvent.RAPPEL, event.getType());
        assertTrue(event instanceof ReminderEvent);
        ReminderEvent reminderEvent = (ReminderEvent) event;
        assertEquals(message, reminderEvent.getMessage().getValue());
    }
    
    @Test
    public void testCreateEventWithStringType() {
        // Given
        String type = "RAPPEL";
        String title = "Rappel important";
        String owner = "John";
        LocalDateTime date = LocalDateTime.of(2025, 4, 15, 10, 0);
        String message = "Ne pas oublier de prendre les documents";
        
        // When
        Event event = factory.createEvent(type, title, owner, date, 0, message, null, 0);
        
        // Then
        assertNotNull(event);
        assertEquals(TypeEvent.RAPPEL, event.getType());
        assertTrue(event instanceof ReminderEvent);
    }
    
    @Test
    public void testCreateEventWithInvalidType() {
        // Given
        String invalidType = "INVALID_TYPE";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createEvent(invalidType, "Title", "Owner", LocalDateTime.now(), 0, null, null, 0);
        });
    }
}
