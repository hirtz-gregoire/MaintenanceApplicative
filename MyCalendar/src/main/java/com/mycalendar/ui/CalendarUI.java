package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.mycalendar.CalendarManager;
import com.mycalendar.events.Event;
import com.mycalendar.user.User;

/**
 * Classe responsable de l'interface utilisateur du calendrier.
 */
public class CalendarUI {
    private final Scanner scanner;
    private final CalendarManager calendarManager;
    private final DateTimeInputHelper dateTimeHelper;
    private final EventInputHelper eventInputHelper;
    private final EventTypeMenu eventTypeMenu;
    
    public CalendarUI(Scanner scanner, CalendarManager calendarManager) {
        this.scanner = scanner;
        this.calendarManager = calendarManager;
        this.dateTimeHelper = new DateTimeInputHelper(scanner);
        this.eventInputHelper = new EventInputHelper(scanner, calendarManager);
        this.eventTypeMenu = new EventTypeMenu(scanner, eventInputHelper);
    }
    
    /**
     * Affiche le logo de l'application.
     */
    public void displayLogo() {
        System.out.println("░▒█▀▀▄░█▀▀▄░█░░█▀▀░█▀▀▄░█▀▄░█▀▀▄░█▀▀▄░▒█▀▄▀█░█▀▀▄░█▀▀▄░█▀▀▄░█▀▀▀░█▀▀░█▀▀▄");
        System.out.println("░▒█░░░░█▄▄█░█░░█▀▀░█░▒█░█░█░█▄▄█░█▄▄▀░▒█▒█▒█░█▄▄█░█░▒█░█▄▄█░█░▀▄░█▀▀░█▄▄▀");
        System.out.println("░▒█▄▄▀░▀░░▀░▀▀░▀▀▀░▀░░▀░▀▀░░▀░░▀░▀░▀▀░▒█░░▒█░▀░░▀░▀░░▀░▀░░▀░▀▀▀▀░▀▀▀░▀░▀▀");
    }
    
    /**
     * Affiche le menu principal du calendrier.
     * @param user L'utilisateur connecté
     * @return true si l'utilisateur souhaite continuer, false sinon
     */
    public boolean displayMainMenu(User user) {
        System.out.println("\nBonjour, " + user.getUsername());
        System.out.println("=== Menu Gestionnaire d'Événements ===");
        System.out.println("1 - Voir les événements");
        System.out.println("2 - Ajouter un événement");
        System.out.println("3 - Se déconnecter");
        System.out.print("Votre choix : ");
        
        String choix = scanner.nextLine();
        
        switch (choix) {
            case "1":
                displayViewEventsMenu();
                return true;
            case "2":
                eventTypeMenu.displayMenu(user);
                return true;
            default:
                System.out.println("Déconnexion ! Voulez-vous continuer ? (oui/non)");
                return scanner.nextLine().trim().equalsIgnoreCase("oui");
        }
    }
    
    /**
     * Affiche le menu de visualisation des événements.
     */
    private void displayViewEventsMenu() {
        System.out.println("\n=== Menu de visualisation d'Événements ===");
        System.out.println("1 - Afficher TOUS les événements");
        System.out.println("2 - Afficher les événements d'un MOIS précis");
        System.out.println("3 - Afficher les événements d'une SEMAINE précise");
        System.out.println("4 - Afficher les événements d'un JOUR précis");
        System.out.println("5 - Retour");
        System.out.print("Votre choix : ");
        
        String choix = scanner.nextLine();
        
        switch (choix) {
            case "1":
                calendarManager.afficherEvenements();
                break;
            case "2":
                LocalDateTime[] periodeMois = dateTimeHelper.inputMonthPeriod();
                displayEventList(calendarManager.eventsDansPeriode(periodeMois[0], periodeMois[1]));
                break;
            case "3":
                LocalDateTime[] periodeSemaine = dateTimeHelper.inputWeekPeriod();
                displayEventList(calendarManager.eventsDansPeriode(periodeSemaine[0], periodeSemaine[1]));
                break;
            case "4":
                LocalDateTime[] periodeJour = dateTimeHelper.inputDayPeriod();
                displayEventList(calendarManager.eventsDansPeriode(periodeJour[0], periodeJour[1]));
                break;
        }
    }
    
    /**
     * Affiche une liste d'événements.
     * @param evenements La liste d'événements à afficher
     */
    private void displayEventList(List<Event> evenements) {
        if (evenements.isEmpty()) {
            System.out.println("Aucun événement trouvé pour cette période.");
        } else {
            System.out.println("Événements trouvés : ");
            for (Event e : evenements) {
                System.out.println("- " + e.description());
            }
        }
    }
}
