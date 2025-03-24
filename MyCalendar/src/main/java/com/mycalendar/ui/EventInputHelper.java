package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import com.mycalendar.CalendarManager;
import com.mycalendar.user.User;

/**
 * Classe utilitaire pour faciliter la saisie des événements.
 */
public class EventInputHelper {
    private final Scanner scanner;
    private final DateTimeInputHelper dateTimeHelper;
    private final EventFactory eventFactory;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param calendarManager Le gestionnaire de calendrier
     */
    public EventInputHelper(Scanner scanner, CalendarManager calendarManager) {
        this.scanner = scanner;
        this.dateTimeHelper = new DateTimeInputHelper(scanner);
        this.eventFactory = new EventFactory(calendarManager);
    }
    
    /**
     * Demande à l'utilisateur de saisir un rendez-vous personnel.
     * @param user L'utilisateur qui crée l'événement
     */
    public void inputPersonalEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int duree = dateTimeHelper.inputDuration();
        
        eventFactory.createPersonalEvent(titre, user.getUsername(), dateTime, duree);
        
        System.out.println("Événement ajouté.");
    }
    
    /**
     * Demande à l'utilisateur de saisir une réunion.
     * @param user L'utilisateur qui crée l'événement
     */
    public void inputMeetingEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int duree = dateTimeHelper.inputDuration();
        
        System.out.print("Lieu : ");
        String lieu = scanner.nextLine();
        
        String participants = user.getUsername();
        
        List<String> additionalParticipants = collectParticipants();
        
        for (String participant : additionalParticipants) {
            participants += ", " + participant;
        }
        
        eventFactory.createMeetingEvent(titre, user.getUsername(), dateTime, duree, lieu, participants);
        
        System.out.println("Événement ajouté.");
    }
    
    /**
     * Collecte les participants à une réunion.
     * @return La liste des participants
     */
    private List<String> collectParticipants() {
        return InputCollector.create(
            scanner,
            "Participant : ",
            Function.identity(),
            response -> response.equalsIgnoreCase("oui")
        ).collect();
    }
    
    /**
     * Demande à l'utilisateur de saisir un événement périodique.
     * @param user L'utilisateur qui crée l'événement
     */
    public void inputPeriodicEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int frequence = dateTimeHelper.inputFrequency();
        
        eventFactory.createPeriodicEvent(titre, user.getUsername(), dateTime, frequence);
        
        System.out.println("Événement ajouté.");
    }
    
    /**
     * Demande à l'utilisateur de saisir une tâche.
     * @param user L'utilisateur qui crée l'événement
     */
    public void inputTaskEvent(User user) {
        System.out.print("Titre de la tâche : ");
        String titre = scanner.nextLine();
        
        LocalDateTime deadline = dateTimeHelper.inputDateTime();
        
        System.out.print("Priorité (BASSE, MOYENNE, HAUTE) : ");
        String priorite = scanner.nextLine().toUpperCase();
        
        eventFactory.createTaskEvent(titre, user.getUsername(), deadline, priorite);
        
        System.out.println("Tâche ajoutée.");
    }
    
    /**
     * Demande à l'utilisateur de saisir un rappel.
     * @param user L'utilisateur qui crée l'événement
     */
    public void inputReminderEvent(User user) {
        System.out.print("Titre du rappel : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        
        System.out.print("Message : ");
        String message = scanner.nextLine();
        
        eventFactory.createReminderEvent(titre, user.getUsername(), dateTime, message);
        
        System.out.println("Rappel ajouté.");
    }
}
