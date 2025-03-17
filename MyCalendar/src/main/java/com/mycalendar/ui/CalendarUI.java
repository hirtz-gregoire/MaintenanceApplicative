package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.mycalendar.CalendarManager;
import com.mycalendar.events.IEvent;
import com.mycalendar.user.User;

public class CalendarUI {
    private final Scanner scanner;
    private final CalendarManager calendarManager;
    private final DateTimeInputHelper dateTimeHelper;
    private final EventInputHelper eventInputHelper;
    
    public CalendarUI(Scanner scanner, CalendarManager calendarManager) {
        this.scanner = scanner;
        this.calendarManager = calendarManager;
        this.dateTimeHelper = new DateTimeInputHelper(scanner);
        this.eventInputHelper = new EventInputHelper(scanner, calendarManager);
    }
    
    public void displayLogo() {
        System.out.println("  _____         _                   _                __  __");
        System.out.println(" / ____|       | |                 | |              |  \\/  |");
        System.out.println("| |       __ _ | |  ___  _ __    __| |  __ _  _ __  | \\  / |  __ _  _ __    __ _   __ _   ___  _ __");
        System.out.println("| |      / _` || | / _ \\| '_ \\  / _` | / _` || '__| | |\\/| | / _` || '_ \\  / _` | / _` | / _ \\| '__|");
        System.out.println("| |____ | (_| || ||  __/| | | || (_| || (_| || |    | |  | || (_| || | | || (_| || (_| ||  __/| |");
        System.out.println(" \\_____| \\__,_||_| \\___||_| |_| \\__,_| \\__,_||_|    |_|  |_| \\__,_||_| |_| \\__,_| \\__, | \\___||_|");
        System.out.println("                                                                                   __/ |");
        System.out.println("                                                                                  |___/");
    }
    
    public boolean displayMainMenu(User user) {
        System.out.println("\nBonjour, " + user.getUsername());
        System.out.println("=== Menu Gestionnaire d'Événements ===");
        System.out.println("1 - Voir les événements");
        System.out.println("2 - Ajouter un rendez-vous perso");
        System.out.println("3 - Ajouter une réunion");
        System.out.println("4 - Ajouter un évènement périodique");
        System.out.println("5 - Se déconnecter");
        System.out.print("Votre choix : ");
        
        String choix = scanner.nextLine();
        
        switch (choix) {
            case "1":
                displayViewEventsMenu();
                return true;
            case "2":
                eventInputHelper.inputPersonalEvent(user);
                return true;
            case "3":
                eventInputHelper.inputMeetingEvent(user);
                return true;
            case "4":
                eventInputHelper.inputPeriodicEvent(user);
                return true;
            default:
                System.out.println("Déconnexion ! Voulez-vous continuer ? (oui/non)");
                return scanner.nextLine().trim().equalsIgnoreCase("oui");
        }
    }
    
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
    
    private void displayEventList(List<IEvent> evenements) {
        if (evenements.isEmpty()) {
            System.out.println("Aucun événement trouvé pour cette période.");
        } else {
            System.out.println("Événements trouvés : ");
            for (IEvent e : evenements) {
                System.out.println("- " + e.description());
            }
        }
    }
}
