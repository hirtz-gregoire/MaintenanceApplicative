package com.mycalendar;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mycalendar.events.DateEvent;
import com.mycalendar.events.EventFactory;
import com.mycalendar.events.EventId;
import com.mycalendar.events.Event;
import com.mycalendar.json.JsonUtils;

public class CalendarManager {
    private final Map<EventId, Event> events;
    private final EventFactory eventFactory;

    public CalendarManager() {
        this.events = new HashMap<>();
        this.eventFactory = new EventFactory();
    }

    /**
     * Ajoute un événement au calendrier.
     * @param event L'événement à ajouter
     */
    public void ajouterEvent(Event event) {
        events.put(event.getId(), event);
        saveEventsAutomatically();
    }
    
    /**
     * Sauvegarde automatiquement les événements après chaque modification.
     */
    private void saveEventsAutomatically() {
        try {
            saveEventsToDefaultFile();
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde automatique : " + e.getMessage());
        }
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
        Event event = eventFactory.createEvent(type, title, proprietaire, dateDebut, dureeMinutes, lieu, participants, frequenceJours);
        ajouterEvent(event);
    }

    /**
     * Retourne la liste des événements qui se produisent dans la période spécifiée.
     * @param debut La date de début de la période
     * @param fin La date de fin de la période
     * @return La liste des événements dans la période
     */
    public List<Event> eventsDansPeriode(LocalDateTime debut, LocalDateTime fin) {
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
    public boolean conflit(Event e1, Event e2) {
        return e1.conflictsWith(e2);
    }

    /**
     * Affiche tous les événements du calendrier.
     */
    public void afficherEvenements() {
        for (Event e : events.values()) {
            System.out.println(e.description());
        }
    }
    
    /**
     * Supprime un événement du calendrier par son identifiant.
     * @param eventId L'identifiant de l'événement à supprimer
     * @return true si l'événement a été supprimé, false s'il n'existait pas
     */
    public boolean supprimerEvent(EventId eventId) {
        boolean result = events.remove(eventId) != null;
        if (result) {
            saveEventsAutomatically();
        }
        return result;
    }
    
    /**
     * Retourne la liste des événements en conflit avec un événement donné.
     * @param event L'événement à vérifier
     * @return La liste des événements en conflit
     */
    public List<Event> evenementsEnConflit(Event event) {
        return events.values().stream()
                .filter(e -> !e.equals(event) && e.conflictsWith(event))
                .collect(Collectors.toList());
    }
    
    /**
     * Vérifie si un événement est en conflit avec d'autres événements du calendrier.
     * @param event L'événement à vérifier
     * @return true si l'événement est en conflit, false sinon
     */
    public boolean estEnConflit(Event event) {
        return !evenementsEnConflit(event).isEmpty();
    }
    
    /**
     * Retourne tous les événements du calendrier.
     * @return La liste de tous les événements
     */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }
    
    /**
     * Sauvegarde tous les événements du calendrier dans un fichier JSON.
     * @param file Le fichier où sauvegarder les événements
     * @throws IOException En cas d'erreur lors de la sauvegarde
     */
    public void saveEventsToJson(File file) throws IOException {
        List<Event> eventList = getAllEvents();
        JsonUtils.saveToFile(eventList, file);
    }
    
    /**
     * Charge les événements depuis un fichier JSON.
     * @param file Le fichier contenant les événements
     * @throws IOException En cas d'erreur lors du chargement
     */
    public void loadEventsFromJson(File file) throws IOException {
        // Vider la liste actuelle d'événements
        events.clear();
        
        try {
            // Charger les événements depuis le fichier
            List<Event> loadedEvents = JsonUtils.loadListFromFile(file, Event.class);
            
            // Ajouter les événements chargés sans déclencher de sauvegarde automatique
            for (Event event : loadedEvents) {
                events.put(event.getId(), event);
            }
            
            System.out.println("Chargement réussi : " + loadedEvents.size() + " événements chargés.");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des événements : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Sauvegarde tous les événements du calendrier dans un fichier JSON par défaut.
     * @throws IOException En cas d'erreur lors de la sauvegarde
     */
    public void saveEventsToDefaultFile() throws IOException {
        File defaultFile = new File("calendar_events.json");
        saveEventsToJson(defaultFile);
    }
    
    /**
     * Charge les événements depuis un fichier JSON par défaut.
     * @throws IOException En cas d'erreur lors du chargement
     */
    public void loadEventsFromDefaultFile() throws IOException {
        File defaultFile = new File("calendar_events.json");
        if (defaultFile.exists()) {
            loadEventsFromJson(defaultFile);
        }
    }
}
