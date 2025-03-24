package com.mycalendar.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface représentant un événement dans le calendrier.
 * Cette interface définit les méthodes communes à tous les types d'événements.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PersonalEvent.class, name = "RDV_PERSONNEL"),
    @JsonSubTypes.Type(value = MeetingEvent.class, name = "REUNION"),
    @JsonSubTypes.Type(value = PeriodicEvent.class, name = "PERIODIQUE"),
    @JsonSubTypes.Type(value = TaskEvent.class, name = "TASK"),
    @JsonSubTypes.Type(value = ReminderEvent.class, name = "RAPPEL")
})
public interface Event {
    
    /**
     * Retourne l'identifiant unique de l'événement.
     * @return L'identifiant de l'événement
     */
    EventId getId();
    
    /**
     * Retourne le titre de l'événement.
     * @return Le titre de l'événement
     */
    TitleEvent getTitle();
    
    /**
     * Retourne le propriétaire de l'événement.
     * @return Le propriétaire de l'événement
     */
    OwnerEvent getOwner();
    
    /**
     * Retourne la date de début de l'événement.
     * @return La date de début de l'événement
     */
    DateEvent getStartDate();
    
    /**
     * Retourne la durée de l'événement.
     * @return La durée de l'événement
     */
    DurationEvent getDuration();
    
    /**
     * Retourne la date de fin de l'événement.
     * @return La date de fin de l'événement
     */
    DateEvent getEndDate();
    
    /**
     * Vérifie si l'événement est en conflit avec un autre événement.
     * @param other L'autre événement à vérifier
     * @return true si les événements sont en conflit, false sinon
     */
    boolean conflictsWith(Event other);
    
    /**
     * Génère une description textuelle de l'événement.
     * @return La description de l'événement
     */
    String description();
    
    /**
     * Vérifie si l'événement se produit dans la période spécifiée.
     * @param start Date de début de la période
     * @param end Date de fin de la période
     * @return true si l'événement se produit dans la période, false sinon
     */
    boolean occursInPeriod(DateEvent start, DateEvent end);
    
    /**
     * Retourne le type de l'événement.
     * @return Le type de l'événement
     */
    TypeEvent getType();
}
