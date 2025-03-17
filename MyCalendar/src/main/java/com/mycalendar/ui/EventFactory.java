package com.mycalendar.ui;

import java.time.LocalDateTime;

import com.mycalendar.CalendarManager;
import com.mycalendar.events.DateEvent;
import com.mycalendar.events.DurationEvent;
import com.mycalendar.events.Event;
import com.mycalendar.events.MeetingEvent;
import com.mycalendar.events.PeriodicEvent;
import com.mycalendar.events.PersonalEvent;
import com.mycalendar.events.TypeEvent;

/**
 * Factory pour créer des événements en fonction de leur type.
 */
public class EventFactory {
    private final CalendarManager calendarManager;
    
    public EventFactory(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
    }
    
    /**
     * Crée un événement personnel et l'ajoute au calendrier.
     * @param title Le titre de l'événement
     * @param owner Le propriétaire de l'événement
     * @param dateTime La date et l'heure de l'événement
     * @param duration La durée de l'événement en minutes
     */
    public void createPersonalEvent(String title, String owner, LocalDateTime dateTime, int duration) {
        Event event = new PersonalEvent(title, owner, new DateEvent(dateTime), new DurationEvent(duration));
        calendarManager.ajouterEvent(event);
    }
    
    /**
     * Crée une réunion et l'ajoute au calendrier.
     * @param title Le titre de l'événement
     * @param owner Le propriétaire de l'événement
     * @param dateTime La date et l'heure de l'événement
     * @param duration La durée de l'événement en minutes
     * @param place Le lieu de la réunion
     * @param participants Les participants à la réunion
     */
    public void createMeetingEvent(String title, String owner, LocalDateTime dateTime, int duration, String place, String participants) {
        Event event = new MeetingEvent(title, owner, new DateEvent(dateTime), new DurationEvent(duration), place, participants);
        calendarManager.ajouterEvent(event);
    }
    
    /**
     * Crée un événement périodique et l'ajoute au calendrier.
     * @param title Le titre de l'événement
     * @param owner Le propriétaire de l'événement
     * @param dateTime La date et l'heure de l'événement
     * @param frequency La fréquence de l'événement en jours
     */
    public void createPeriodicEvent(String title, String owner, LocalDateTime dateTime, int frequency) {
        Event event = new PeriodicEvent(title, owner, new DateEvent(dateTime), frequency);
        calendarManager.ajouterEvent(event);
    }
    
    /**
     * Crée un événement en fonction de son type et l'ajoute au calendrier.
     * @param type Le type d'événement
     * @param title Le titre de l'événement
     * @param owner Le propriétaire de l'événement
     * @param dateTime La date et l'heure de l'événement
     * @param duration La durée de l'événement en minutes
     * @param place Le lieu de l'événement (pour les réunions)
     * @param participants Les participants à l'événement (pour les réunions)
     * @param frequency La fréquence de l'événement en jours (pour les événements périodiques)
     */
    public void createEvent(TypeEvent type, String title, String owner, LocalDateTime dateTime, int duration, String place, String participants, int frequency) {
        switch (type) {
            case RDV_PERSONNEL:
                createPersonalEvent(title, owner, dateTime, duration);
                break;
            case REUNION:
                createMeetingEvent(title, owner, dateTime, duration, place, participants);
                break;
            case PERIODIQUE:
                createPeriodicEvent(title, owner, dateTime, frequency);
                break;
        }
    }
}
