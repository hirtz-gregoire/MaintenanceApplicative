package com.mycalendar.events;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReminderEventTest {
    
    @Test
    public void testReminderEventCreation() {
        // Given
        String title = "Rappel important";
        String owner = "John";
        LocalDateTime dateTime = LocalDateTime.of(2025, 4, 15, 10, 0);
        String message = "Ne pas oublier de prendre les documents";
        
        // When
        ReminderEvent reminder = new ReminderEvent(title, owner, new DateEvent(dateTime), message);
        
        // Then
        assertEquals(title, reminder.getTitle().getValue());
        assertEquals(owner, reminder.getOwner().getValue());
        assertEquals(dateTime, reminder.getStartDate().getDateTime());
        assertEquals(message, reminder.getMessage().getValue());
        assertEquals(TypeEvent.RAPPEL, reminder.getType());
    }
    
    @Test
    public void testReminderEventDescription() {
        // Given
        ReminderEvent reminder = new ReminderEvent(
            "Rappel", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 0)),
            "Message important"
        );
        
        // When
        String description = reminder.description();
        
        // Then
        assertTrue(description.contains("Rappel"));
        assertTrue(description.contains("Message important"));
    }
    
    @Test
    public void testReminderEventDoesNotConflict() {
        // Given
        ReminderEvent reminder = new ReminderEvent(
            "Rappel", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 0)),
            "Message important"
        );
        
        PersonalEvent personalEvent = new PersonalEvent(
            "Rendez-vous", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        // When & Then
        assertFalse(reminder.conflictsWith(personalEvent));
        assertFalse(personalEvent.conflictsWith(reminder));
    }
}
