package com.mycalendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mycalendar.events.DateEvent;
import com.mycalendar.events.EventFactory;
import com.mycalendar.events.EventId;
import com.mycalendar.events.IEvent;

public class CalendarManager {
    private final Map<EventId, IEvent> events;
    private final EventFactory eventFactory;

    public CalendarManager() {
        this.events = new HashMap<>();
        this.eventFactory = new EventFactory();
    }

    /**
     * Ajoute un événement au calendrier.
     * @param event L'événement à ajouter
     */
    public void ajouterEvent(IEvent event) {
        events.put(event.getId(), event);
    }

    /**
     * Ajoute un événement au calendrier en fonction de son type.
     * @param type Le type d'événement
     * @param title Le titre de l'événement
     * @param proprietaire Le propriétaire de l'événement
     * @param dateDebut La date de début de l'événement
     * @param dureeMinutes La durée de l'événement en minutes
     * @param lieu Le lieu de l'événement (pour les réunions)
     * @param participants Les participants à l'événement (pour les réunions)
     * @param frequenceJours La fréquence de l'événement en jours (pour les événements périodiques)
     */
    public void ajouterEvent(String type, String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes,
                             String lieu, String participants, int frequenceJours) {
        IEvent event = eventFactory.createEvent(type, title, proprietaire, dateDebut, dureeMinutes, lieu, participants, frequenceJours);
        ajouterEvent(event);
    }

    /**
     * Retourne la liste des événements qui se produisent dans la période spécifiée.
     * @param debut La date de début de la période
     * @param fin La date de fin de la période
     * @return La liste des événements dans la période
     */
    public List<IEvent> eventsDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        DateEvent debutEvent = new DateEvent(debut);
        DateEvent finEvent = new DateEvent(fin);
        
        return events.values().stream()
                .filter(event -> event.occursInPeriod(debutEvent, finEvent))
                .collect(Collectors.toList());
    }

    /**
     * Vérifie s'il y a un conflit entre deux événements.
     * @param e1 Le premier événement
     * @param e2 Le second événement
     * @return true s'il y a un conflit, false sinon
     */
    public boolean conflit(IEvent e1, IEvent e2) {
        return e1.conflictsWith(e2);
    }

    /**
     * Affiche tous les événements du calendrier.
     */
    public void afficherEvenements() {
        for (IEvent e : events.values()) {
            System.out.println(e.description());
        }
    }
    
    /**
     * Supprime un événement du calendrier par son identifiant.
     * @param eventId L'identifiant de l'événement à supprimer
     * @return true si l'événement a été supprimé, false s'il n'existait pas
     */
    public boolean supprimerEvent(EventId eventId) {
        return events.remove(eventId) != null;
    }
    
    /**
     * Retourne la liste des événements en conflit avec un événement donné.
     * @param event L'événement à vérifier
     * @return La liste des événements en conflit
     */
    public List<IEvent> evenementsEnConflit(IEvent event) {
        return events.values().stream()
                .filter(e -> !e.equals(event) && e.conflictsWith(event))
                .collect(Collectors.toList());
    }
    
    /**
     * Vérifie si un événement est en conflit avec d'autres événements du calendrier.
     * @param event L'événement à vérifier
     * @return true si l'événement est en conflit, false sinon
     */
    public boolean estEnConflit(IEvent event) {
        return !evenementsEnConflit(event).isEmpty();
    }
    
    /**
     * Retourne tous les événements du calendrier.
     * @return La liste de tous les événements
     */
    public List<IEvent> getAllEvents() {
        return new ArrayList<>(events.values());
    }
}
