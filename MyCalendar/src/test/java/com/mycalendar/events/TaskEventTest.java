package com.mycalendar.events;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskEventTest {

    @Test
    public void testCreateTaskEvent() {
        // Given
        String title = "Complete project";
        String owner = "John";
        LocalDateTime deadline = LocalDateTime.of(2025, 4, 15, 17, 0);
        String priority = "HIGH";
        
        // When
        TaskEvent taskEvent = new TaskEvent(title, owner, new DateEvent(deadline), priority);
        
        // Then
        assertEquals(new TitleEvent(title), taskEvent.getTitle());
        assertEquals(new OwnerEvent(owner), taskEvent.getOwner());
        assertEquals(new DateEvent(deadline), taskEvent.getStartDate());
        assertEquals(new PriorityEvent(priority), taskEvent.getPriority());
        assertEquals(TypeEvent.TASK, taskEvent.getType());
    }
    
    @Test
    public void testTaskEventDescription() {
        // Given
        TaskEvent taskEvent = new TaskEvent(
            "Complete project", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 17, 0)),
            "HIGH"
        );
        
        // When
        String description = taskEvent.description();
        
        // Then
        assertTrue(description.contains("Tâche"));
        assertTrue(description.contains("Complete project"));
        assertTrue(description.contains("HIGH"));
    }
    
    @Test
    public void testTaskEventOccursInPeriod() {
        // Given
        LocalDateTime taskDate = LocalDateTime.of(2025, 4, 15, 17, 0);
        TaskEvent taskEvent = new TaskEvent(
            "Complete project", 
            "John", 
            new DateEvent(taskDate),
            "HIGH"
        );
        
        DateEvent periodStart = new DateEvent(LocalDateTime.of(2025, 4, 10, 0, 0));
        DateEvent periodEnd = new DateEvent(LocalDateTime.of(2025, 4, 20, 0, 0));
        
        // When & Then
        assertTrue(taskEvent.occursInPeriod(periodStart, periodEnd));
        
        // When period is before task
        DateEvent earlyStart = new DateEvent(LocalDateTime.of(2025, 4, 1, 0, 0));
        DateEvent earlyEnd = new DateEvent(LocalDateTime.of(2025, 4, 10, 0, 0));
        
        // Then
        assertFalse(taskEvent.occursInPeriod(earlyStart, earlyEnd));
    }
    
    @Test
    public void testTaskEventDoesNotConflict() {
        // Given
        LocalDateTime taskDate = LocalDateTime.of(2025, 4, 15, 17, 0);
        TaskEvent taskEvent = new TaskEvent(
            "Complete project", 
            "John", 
            new DateEvent(taskDate),
            "HIGH"
        );
        
        PersonalEvent personalEvent = new PersonalEvent(
            "Meeting", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 14, 0)), 
            new DurationEvent(60)
        );
        
        // When & Then - Tasks don't conflict with other events
        assertFalse(taskEvent.conflictsWith(personalEvent));
        assertFalse(personalEvent.conflictsWith(taskEvent));
    }
}
