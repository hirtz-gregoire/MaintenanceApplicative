package com.mycalendar.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PeriodicEventTest {

    @Test
    void testConstructorWithAllParameters() {
        EventId id = new EventId("test-id");
        TitleEvent title = new TitleEvent("Test Periodic Event");
        OwnerEvent owner = new OwnerEvent("Test Owner");
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        FrequencyEvent frequency = new FrequencyEvent(7); // Hebdomadaire
        
        PeriodicEvent event = new PeriodicEvent(id, title, owner, startDate, frequency);
        
        assertEquals(id, event.getId());
        assertEquals(title, event.getTitle());
        assertEquals(owner, event.getOwner());
        assertEquals(startDate, event.getStartDate());
        assertEquals(frequency, event.getFrequency());
        assertEquals(TypeEvent.PERIODIQUE, event.getType());
    }
    
    @Test
    void testConstructorWithStringParameters() {
        String title = "Test Periodic Event";
        String owner = "Test Owner";
        DateEvent startDate = new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0));
        int frequencyDays = 7; // Hebdomadaire
        
        PeriodicEvent event = new PeriodicEvent(title, owner, startDate, frequencyDays);
        
        assertNotNull(event.getId());
        assertEquals(title, event.getTitle().getValue());
        assertEquals(owner, event.getOwner().getValue());
        assertEquals(startDate, event.getStartDate());
        assertEquals(frequencyDays, event.getFrequency().getDays());
        assertEquals(TypeEvent.PERIODIQUE, event.getType());
    }
    
    @Test
    void testDescription() {
        PeriodicEvent event = new PeriodicEvent(
            "Test Periodic Event",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            7 // Hebdomadaire
        );
        
        String description = event.description();
        assertTrue(description.contains("Événement périodique"));
        assertTrue(description.contains("Test Periodic Event"));
        assertTrue(description.contains("toutes les semaines"));
    }
    
    @Test
    void testGetDuration() {
        PeriodicEvent event = new PeriodicEvent(
            "Test Periodic Event",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            7 // Hebdomadaire
        );
        
        // Les événements périodiques ont une durée de 0 par défaut
        assertEquals(0, event.getDuration().getMinutes());
    }
    
    @Test
    void testConflictsWith() {
        PeriodicEvent event1 = new PeriodicEvent(
            "Periodic Event 1",
            "Owner 1",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            7 // Hebdomadaire
        );
        
        PeriodicEvent event2 = new PeriodicEvent(
            "Periodic Event 2",
            "Owner 2",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            7 // Hebdomadaire
        );
        
        // Les événements périodiques ne sont pas considérés comme étant en conflit
        assertFalse(event1.conflictsWith(event2));
        assertFalse(event2.conflictsWith(event1));
    }
    
    @Test
    void testOccursInPeriod() {
        PeriodicEvent event = new PeriodicEvent(
            "Test Periodic Event",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            7 // Hebdomadaire
        );
        
        // Période 1: 2023-03-15 00:00 - 2023-03-16 00:00 (l'événement est dans cette période)
        DateEvent start1 = new DateEvent(LocalDateTime.of(2023, 3, 15, 0, 0));
        DateEvent end1 = new DateEvent(LocalDateTime.of(2023, 3, 16, 0, 0));
        
        // Période 2: 2023-03-16 00:00 - 2023-03-23 00:00 (l'événement est dans cette période car il se répète)
        DateEvent start2 = new DateEvent(LocalDateTime.of(2023, 3, 16, 0, 0));
        DateEvent end2 = new DateEvent(LocalDateTime.of(2023, 3, 23, 0, 0));
        
        // Période 3: 2023-03-01 00:00 - 2023-03-14 00:00 (l'événement n'est pas dans cette période)
        DateEvent start3 = new DateEvent(LocalDateTime.of(2023, 3, 1, 0, 0));
        DateEvent end3 = new DateEvent(LocalDateTime.of(2023, 3, 14, 0, 0));
        
        assertTrue(event.occursInPeriod(start1, end1));
        assertTrue(event.occursInPeriod(start2, end2));
        assertFalse(event.occursInPeriod(start3, end3));
    }
    
    @Test
    void testOccursInPeriodWithMultipleOccurrences() {
        PeriodicEvent event = new PeriodicEvent(
            "Test Periodic Event",
            "Test Owner",
            new DateEvent(LocalDateTime.of(2023, 3, 1, 10, 0)),
            7 // Hebdomadaire
        );
        
        // Période: 2023-03-01 00:00 - 2023-04-01 00:00 (l'événement se produit plusieurs fois dans cette période)
        DateEvent start = new DateEvent(LocalDateTime.of(2023, 3, 1, 0, 0));
        DateEvent end = new DateEvent(LocalDateTime.of(2023, 4, 1, 0, 0));
        
        assertTrue(event.occursInPeriod(start, end));
    }
}
