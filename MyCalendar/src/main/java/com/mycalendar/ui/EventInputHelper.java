package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.util.Scanner;

import com.mycalendar.CalendarManager;
import com.mycalendar.user.User;

public class EventInputHelper {
    private final Scanner scanner;
    private final DateTimeInputHelper dateTimeHelper;
    private final CalendarManager calendarManager;
    
    public EventInputHelper(Scanner scanner, CalendarManager calendarManager) {
        this.scanner = scanner;
        this.calendarManager = calendarManager;
        this.dateTimeHelper = new DateTimeInputHelper(scanner);
    }
    
    public void inputPersonalEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int duree = dateTimeHelper.inputDuration();
        
        calendarManager.ajouterEvent("RDV_PERSONNEL", titre, user.getUsername(),
                dateTime, duree, "", "", 0);
        
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
        
        calendarManager.ajouterEvent("REUNION", titre, user.getUsername(),
                dateTime, duree, lieu, participants, 0);
        
        System.out.println("Événement ajouté.");
    }
    
    public void inputPeriodicEvent(User user) {
        System.out.print("Titre de l'événement : ");
        String titre = scanner.nextLine();
        
        LocalDateTime dateTime = dateTimeHelper.inputDateTime();
        int frequence = dateTimeHelper.inputFrequency();
        
        calendarManager.ajouterEvent("PERIODIQUE", titre, user.getUsername(),
                dateTime, 0, "", "", frequence);
        
        System.out.println("Événement ajouté.");
    }
}
