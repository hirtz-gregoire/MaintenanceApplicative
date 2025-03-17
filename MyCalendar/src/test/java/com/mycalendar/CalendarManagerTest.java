package com.mycalendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycalendar.events.DateEvent;
import com.mycalendar.events.DurationEvent;
import com.mycalendar.events.EventId;
import com.mycalendar.events.Event;
import com.mycalendar.events.MeetingEvent;
import com.mycalendar.events.PeriodicEvent;
import com.mycalendar.events.PersonalEvent;

class CalendarManagerTest {
    
    private CalendarManager calendarManager;
    private PersonalEvent personalEvent;
    private MeetingEvent meetingEvent;
    private PeriodicEvent periodicEvent;
    
    @BeforeEach
    void setUp() {
        calendarManager = new CalendarManager();
        
        // Création d'un rendez-vous personnel: 15/03/2023 10:00 - 11:00
        personalEvent = new PersonalEvent(
            "RDV Personnel",
            "John Doe",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        // Création d'une réunion: 15/03/2023 14:00 - 15:30
        meetingEvent = new MeetingEvent(
            "Réunion d'équipe",
            "John Doe",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 14, 0)),
            new DurationEvent(90),
            "Salle de conférence",
            "John Doe, Jane Smith, Bob Johnson"
        );
        
        // Création d'un événement périodique: tous les lundis à partir du 13/03/2023
        periodicEvent = new PeriodicEvent(
            "Événement hebdomadaire",
            "John Doe",
            new DateEvent(LocalDateTime.of(2023, 3, 13, 9, 0)),
            7 // Hebdomadaire
        );
    }
    
    @Test
    void testAjouterEvent() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(meetingEvent);
        calendarManager.ajouterEvent(periodicEvent);
        
        List<Event> allEvents = calendarManager.getAllEvents();
        assertEquals(3, allEvents.size());
        assertTrue(allEvents.contains(personalEvent));
        assertTrue(allEvents.contains(meetingEvent));
        assertTrue(allEvents.contains(periodicEvent));
    }
    
    @Test
    void testEventsDansPeriode() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(meetingEvent);
        calendarManager.ajouterEvent(periodicEvent);
        
        // Période: 15/03/2023 00:00 - 16/03/2023 00:00
        LocalDateTime debut = LocalDateTime.of(2023, 3, 15, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2023, 3, 16, 0, 0);
        
        List<Event> events = calendarManager.eventsDansPeriode(debut, fin);
        assertEquals(2, events.size());
        assertTrue(events.contains(personalEvent));
        assertTrue(events.contains(meetingEvent));
        
        // Période: 13/03/2023 00:00 - 14/03/2023 00:00
        LocalDateTime debut2 = LocalDateTime.of(2023, 3, 13, 0, 0);
        LocalDateTime fin2 = LocalDateTime.of(2023, 3, 14, 0, 0);
        
        List<Event> events2 = calendarManager.eventsDansPeriode(debut2, fin2);
        assertEquals(1, events2.size());
        assertTrue(events2.contains(periodicEvent));
        
        // Période: 20/03/2023 00:00 - 21/03/2023 00:00 (l'événement périodique se répète)
        LocalDateTime debut3 = LocalDateTime.of(2023, 3, 20, 0, 0);
        LocalDateTime fin3 = LocalDateTime.of(2023, 3, 21, 0, 0);
        
        List<Event> events3 = calendarManager.eventsDansPeriode(debut3, fin3);
        assertEquals(1, events3.size());
        assertTrue(events3.contains(periodicEvent));
    }
    
    @Test
    void testConflit() {
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
        
        assertTrue(calendarManager.conflit(event1, event2));
        assertFalse(calendarManager.conflit(event1, event3));
    }
    
    @Test
    void testSupprimerEvent() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(meetingEvent);
        
        assertEquals(2, calendarManager.getAllEvents().size());
        
        EventId idToRemove = personalEvent.getId();
        boolean removed = calendarManager.supprimerEvent(idToRemove);
        
        assertTrue(removed);
        assertEquals(1, calendarManager.getAllEvents().size());
        assertFalse(calendarManager.getAllEvents().contains(personalEvent));
        assertTrue(calendarManager.getAllEvents().contains(meetingEvent));
        
        // Tentative de suppression d'un événement qui n'existe pas
        EventId nonExistentId = new EventId();
        boolean removedNonExistent = calendarManager.supprimerEvent(nonExistentId);
        
        assertFalse(removedNonExistent);
        assertEquals(1, calendarManager.getAllEvents().size());
    }
    
    @Test
    void testEvenementsEnConflit() {
        // Événement 1: 10:00 - 11:00
        PersonalEvent event1 = new PersonalEvent(
            "Event 1",
            "Owner 1",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        // Événement 2: 10:30 - 11:30 (conflit avec event1)
        PersonalEvent event2 = new PersonalEvent(
            "Event 2",
            "Owner 2",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 30)),
            new DurationEvent(60)
        );
        
        // Événement 3: 11:00 - 12:00 (conflit avec event2)
        PersonalEvent event3 = new PersonalEvent(
            "Event 3",
            "Owner 3",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 11, 0)),
            new DurationEvent(60)
        );
        
        calendarManager.ajouterEvent(event1);
        calendarManager.ajouterEvent(event2);
        calendarManager.ajouterEvent(event3);
        
        List<Event> conflictsWithEvent1 = calendarManager.evenementsEnConflit(event1);
        assertEquals(1, conflictsWithEvent1.size());
        assertTrue(conflictsWithEvent1.contains(event2));
        
        List<Event> conflictsWithEvent2 = calendarManager.evenementsEnConflit(event2);
        assertEquals(2, conflictsWithEvent2.size());
        assertTrue(conflictsWithEvent2.contains(event1));
        assertTrue(conflictsWithEvent2.contains(event3));
    }
    
    @Test
    void testEstEnConflit() {
        // Événement 1: 10:00 - 11:00
        PersonalEvent event1 = new PersonalEvent(
            "Event 1",
            "Owner 1",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 0)),
            new DurationEvent(60)
        );
        
        // Événement 2: 10:30 - 11:30 (conflit avec event1)
        PersonalEvent event2 = new PersonalEvent(
            "Event 2",
            "Owner 2",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 10, 30)),
            new DurationEvent(60)
        );
        
        // Événement 3: 12:00 - 13:00 (pas de conflit)
        PersonalEvent event3 = new PersonalEvent(
            "Event 3",
            "Owner 3",
            new DateEvent(LocalDateTime.of(2023, 3, 15, 12, 0)),
            new DurationEvent(60)
        );
        
        calendarManager.ajouterEvent(event1);
        
        assertTrue(calendarManager.estEnConflit(event2));
        assertFalse(calendarManager.estEnConflit(event3));
    }
}
