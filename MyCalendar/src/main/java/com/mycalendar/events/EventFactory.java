package com.mycalendar.events;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory pour créer des événements en fonction de leur type.
 * Cette classe utilise le polymorphisme pour éviter les conditionnels.
 */
public class EventFactory {
    private final Map<TypeEvent, EventCreator> creators;
    
    public EventFactory() {
        creators = new HashMap<>();
        
        // Enregistrement des créateurs d'événements pour chaque type
        creators.put(TypeEvent.RDV_PERSONNEL, this::createPersonalEvent);
        creators.put(TypeEvent.REUNION, this::createMeetingEvent);
        creators.put(TypeEvent.PERIODIQUE, this::createPeriodicEvent);
        creators.put(TypeEvent.TASK, this::createTaskEvent);
        creators.put(TypeEvent.RAPPEL, this::createReminderEvent);
    }
    
    /**
     * Crée un événement en fonction de son type.
     * @param type Le type d'événement
     * @param title Le titre de l'événement
     * @param owner Le propriétaire de l'événement
     * @param startDate La date de début de l'événement
     * @param durationMinutes La durée de l'événement en minutes
     * @param place Le lieu de l'événement (pour les réunions)
     * @param participants Les participants à l'événement (pour les réunions)
     * @param frequencyDays La fréquence de l'événement en jours (pour les événements périodiques)
     * @return L'événement créé
     */
    public Event createEvent(TypeEvent type, String title, String owner, LocalDateTime startDate, 
                             int durationMinutes, String place, String participants, int frequencyDays) {
        EventCreator creator = creators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Type d'événement non reconnu: " + type);
        }
        
        return creator.create(title, owner, startDate, durationMinutes, place, participants, frequencyDays);
    }
    
    /**
     * Crée un événement en fonction de son type (version avec String).
     * @param type Le type d'événement (sous forme de chaîne)
     * @param title Le titre de l'événement
     * @param owner Le propriétaire de l'événement
     * @param startDate La date de début de l'événement
     * @param durationMinutes La durée de l'événement en minutes
     * @param place Le lieu de l'événement (pour les réunions)
     * @param participants Les participants à l'événement (pour les réunions)
     * @param frequencyDays La fréquence de l'événement en jours (pour les événements périodiques)
     * @return L'événement créé
     */
    public Event createEvent(String type, String title, String owner, LocalDateTime startDate, 
                             int durationMinutes, String place, String participants, int frequencyDays) {
        try {
            TypeEvent typeEvent = TypeEvent.valueOf(type);
            return createEvent(typeEvent, title, owner, startDate, durationMinutes, place, participants, frequencyDays);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type d'événement non reconnu: " + type);
        }
    }
    
    private Event createPersonalEvent(String title, String owner, LocalDateTime startDate, 
                                     int durationMinutes, String place, String participants, int frequencyDays) {
        return new PersonalEvent(title, owner, new DateEvent(startDate), new DurationEvent(durationMinutes));
    }
    
    private Event createMeetingEvent(String title, String owner, LocalDateTime startDate, 
                                    int durationMinutes, String place, String participants, int frequencyDays) {
        return new MeetingEvent(title, owner, new DateEvent(startDate), new DurationEvent(durationMinutes), 
                               place, participants);
    }
    
    private Event createPeriodicEvent(String title, String owner, LocalDateTime startDate, 
                                     int durationMinutes, String place, String participants, int frequencyDays) {
        return new PeriodicEvent(title, owner, new DateEvent(startDate), frequencyDays);
    }
    
    private Event createTaskEvent(String title, String owner, LocalDateTime startDate, 
                                 int durationMinutes, String place, String participants, int frequencyDays) {
        // Pour les tâches, on utilise le paramètre place comme priorité
        return new TaskEvent(title, owner, new DateEvent(startDate), place);
    }
    
    private Event createReminderEvent(String title, String owner, LocalDateTime startDate, 
                                     int durationMinutes, String place, String participants, int frequencyDays) {
        // Pour les rappels, on utilise le paramètre place comme message
        return new ReminderEvent(title, owner, new DateEvent(startDate), place);
    }
    
    /**
     * Interface fonctionnelle pour créer des événements.
     */
    @FunctionalInterface
    private interface EventCreator {
        Event create(String title, String owner, LocalDateTime startDate, int durationMinutes, 
                     String place, String participants, int frequencyDays);
    }
}
