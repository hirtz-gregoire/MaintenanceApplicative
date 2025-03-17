package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.util.Scanner;

import com.mycalendar.CalendarManager;
import com.mycalendar.events.TypeEvent;
import com.mycalendar.user.User;

public class EventInputHelper {
    private final Scanner scanner;
    private final DateTimeInputHelper dateTimeHelper;
    private final EventFactory eventFactory;
    
    public EventInputHelper(Scanner scanner, CalendarManager calendarManager) {
        this.scanner = scanner;
        this.dateTimeHelper = new DateTimeInputHelper(scanner);
        this.eventFactory = new EventFactory(calendarManager);
    }
    
    public void inputPersonalEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int duree = dateTimeHelper.inputDuration();
        
        eventFactory.createPersonalEvent(titre, user.getUsername(), dateTime, duree);
        
        System.out.println("Événement ajouté.");
    }
    
    public void inputMeetingEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int duree = dateTimeHelper.inputDuration();
        
        System.out.print("Lieu : ");
        String lieu = scanner.nextLine();
        
        String participants = user.getUsername();
        
        System.out.println("Ajouter un participant ? (oui / non)");
        while (scanner.nextLine().equalsIgnoreCase("oui")) {
            System.out.print("Participant : ");
            participants += ", " + scanner.nextLine();
            System.out.println("Ajouter un autre participant ? (oui / non)");
        }
        
        eventFactory.createMeetingEvent(titre, user.getUsername(), dateTime, duree, lieu, participants);
        
        System.out.println("Événement ajouté.");
    }
    
    public void inputPeriodicEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int frequence = dateTimeHelper.inputFrequency();
        
        eventFactory.createPeriodicEvent(titre, user.getUsername(), dateTime, frequence);
        
        System.out.println("Événement ajouté.");
    }
}
