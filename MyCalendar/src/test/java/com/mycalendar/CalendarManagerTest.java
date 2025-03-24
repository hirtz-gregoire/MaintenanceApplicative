package com.mycalendar;

import com.mycalendar.events.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarManagerTest {
    private CalendarManager calendarManager;
    private Event personalEvent;
    private Event meetingEvent;
    private Event periodicEvent;
    private Event taskEvent;
    private Event reminderEvent;
    
    @BeforeEach
    public void setUp() {
        calendarManager = new CalendarManager();
        
        // Création d'événements pour les tests
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime nextWeek = now.plusDays(7);
        
        personalEvent = new PersonalEvent(
            "Rendez-vous médecin", 
            "John", 
            new DateEvent(tomorrow.withHour(10).withMinute(0)), 
            new DurationEvent(60)
        );
        
        meetingEvent = new MeetingEvent(
            "Réunion projet", 
            "John", 
            new DateEvent(tomorrow.withHour(14).withMinute(0)), 
            new DurationEvent(120),
            "Salle A",
            "Alice, Bob"
        );
        
        periodicEvent = new PeriodicEvent(
            "Événement hebdomadaire", 
            "John", 
            new DateEvent(now.withHour(9).withMinute(0)),
            7
        );
        
        taskEvent = new TaskEvent(
            "Terminer le rapport", 
            "John", 
            new DateEvent(nextWeek.withHour(17).withMinute(0)),
            "HIGH"
        );
        
        reminderEvent = new ReminderEvent(
            "Rappel important", 
            "John", 
            new DateEvent(tomorrow.withHour(8).withMinute(0)),
            "Ne pas oublier de prendre les documents"
        );
    }
    
    @Test
    public void testAjouterEvent() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(meetingEvent);
        
        List<Event> allEvents = calendarManager.getAllEvents();
        assertEquals(2, allEvents.size());
        assertTrue(allEvents.contains(personalEvent));
        assertTrue(allEvents.contains(meetingEvent));
    }
    
    @Test
    public void testEventsDansPeriode() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(meetingEvent);
        calendarManager.ajouterEvent(periodicEvent);
        calendarManager.ajouterEvent(taskEvent);
        
        LocalDateTime startPeriod = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
        LocalDateTime endPeriod = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59);
        
        List<Event> eventsInPeriod = calendarManager.eventsDansPeriode(startPeriod, endPeriod);
        
        assertEquals(2, eventsInPeriod.size());
        assertTrue(eventsInPeriod.contains(personalEvent));
        assertTrue(eventsInPeriod.contains(meetingEvent));
        // Note: L'événement périodique n'est pas détecté dans la période car il commence aujourd'hui
    }
    
    @Test
    public void testConflit() {
        Event event1 = new PersonalEvent(
            "Événement 1", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 0)), 
            new DurationEvent(60)
        );
        
        Event event2 = new PersonalEvent(
            "Événement 2", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 30)), 
            new DurationEvent(60)
        );
        
        Event event3 = new PersonalEvent(
            "Événement 3", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 11, 0)), 
            new DurationEvent(60)
        );
        
        assertTrue(calendarManager.conflit(event1, event2));
        assertFalse(calendarManager.conflit(event1, event3));
    }
    
    @Test
    public void testSupprimerEvent() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(meetingEvent);
        
        assertEquals(2, calendarManager.getAllEvents().size());
        
        boolean result = calendarManager.supprimerEvent(personalEvent.getId());
        assertTrue(result);
        assertEquals(1, calendarManager.getAllEvents().size());
        assertFalse(calendarManager.getAllEvents().contains(personalEvent));
        
        result = calendarManager.supprimerEvent(new EventId());
        assertFalse(result);
    }
    
    @Test
    public void testEvenementsEnConflit() {
        Event event1 = new PersonalEvent(
            "Événement 1", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 0)), 
            new DurationEvent(60)
        );
        
        Event event2 = new PersonalEvent(
            "Événement 2", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 10, 30)), 
            new DurationEvent(60)
        );
        
        Event event3 = new PersonalEvent(
            "Événement 3", 
            "John", 
            new DateEvent(LocalDateTime.of(2025, 4, 15, 11, 30)), 
            new DurationEvent(60)
        );
        
        calendarManager.ajouterEvent(event1);
        calendarManager.ajouterEvent(event2);
        calendarManager.ajouterEvent(event3);
        
        List<Event> conflictingEvents = calendarManager.evenementsEnConflit(event1);
        assertEquals(1, conflictingEvents.size());
        assertTrue(conflictingEvents.contains(event2));
    }
    
    @Test
    public void testTaskEventDoesNotConflict() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(taskEvent);
        
        // Les tâches ne sont pas en conflit avec d'autres événements
        List<Event> conflictingEvents = calendarManager.evenementsEnConflit(taskEvent);
        assertTrue(conflictingEvents.isEmpty());
        
        // Les autres événements ne sont pas en conflit avec les tâches
        List<Event> conflictingWithPersonal = calendarManager.evenementsEnConflit(personalEvent);
        assertTrue(conflictingWithPersonal.isEmpty());
    }
    
    @Test
    public void testReminderEventDoesNotConflict() {
        calendarManager.ajouterEvent(personalEvent);
        calendarManager.ajouterEvent(reminderEvent);
        
        // Les rappels ne sont pas en conflit avec d'autres événements
        List<Event> conflictingEvents = calendarManager.evenementsEnConflit(reminderEvent);
        assertTrue(conflictingEvents.isEmpty());
        
        // Les autres événements ne sont pas en conflit avec les rappels
        List<Event> conflictingWithPersonal = calendarManager.evenementsEnConflit(personalEvent);
        assertTrue(conflictingWithPersonal.isEmpty());
    }
    
    @Test
    public void testReminderEventInPeriod() {
        calendarManager.ajouterEvent(reminderEvent);
        
        LocalDateTime startPeriod = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
        LocalDateTime endPeriod = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59);
        
        List<Event> eventsInPeriod = calendarManager.eventsDansPeriode(startPeriod, endPeriod);
        
        assertEquals(1, eventsInPeriod.size());
        assertTrue(eventsInPeriod.contains(reminderEvent));
    }
}
