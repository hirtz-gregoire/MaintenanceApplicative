package com.mycalendar.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

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
    private final Menu<Boolean> mainMenu;
    private final Menu<Void> viewEventsMenu;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param calendarManager Le gestionnaire de calendrier
     */
    public CalendarUI(Scanner scanner, CalendarManager calendarManager) {
        this.scanner = scanner;
        this.calendarManager = calendarManager;
        this.dateTimeHelper = new DateTimeInputHelper(scanner);
        this.eventInputHelper = new EventInputHelper(scanner, calendarManager);
        this.eventTypeMenu = new EventTypeMenu(scanner, eventInputHelper);
        this.mainMenu = createMainMenu();
        this.viewEventsMenu = createViewEventsMenu();
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
        return mainMenu.display();
    }
    
    /**
     * Crée le menu principal.
     * @return Le menu principal
     */
    private Menu<Boolean> createMainMenu() {
        return MenuBuilder.<Boolean>create(scanner, "Menu Gestionnaire d'Événements")
                .addOption("1", "Voir les événements", MenuOption.of(() -> {
                    displayViewEventsMenu();
                    return true;
                }))
                .addOption("2", "Ajouter un événement", MenuOption.of(() -> {
                    eventTypeMenu.displayMenu(null);
                    return true;
                }))
                .addOption("3", "Se déconnecter", MenuOption.of(() -> {
                    System.out.println("Déconnexion ! Voulez-vous continuer ? (oui/non)");
                    return scanner.nextLine().trim().equalsIgnoreCase("oui");
                }))
                .setDefaultAction(choice -> false)
                .build();
    }
    
    /**
     * Crée le menu de visualisation des événements.
     * @return Le menu de visualisation des événements
     */
    private Menu<Void> createViewEventsMenu() {
        return MenuBuilder.<Void>create(scanner, "Menu de visualisation d'Événements")
                .addOption("1", "Afficher TOUS les événements", MenuOption.of(() -> {
                    calendarManager.afficherEvenements();
                    return null;
                }))
                .addOption("2", "Afficher les événements d'un MOIS précis", MenuOption.of(() -> {
                    LocalDateTime[] periodeMois = dateTimeHelper.inputMonthPeriod();
                    displayEventList(calendarManager.eventsDansPeriode(periodeMois[0], periodeMois[1]));
                    return null;
                }))
                .addOption("3", "Afficher les événements d'une SEMAINE précise", MenuOption.of(() -> {
                    LocalDateTime[] periodeSemaine = dateTimeHelper.inputWeekPeriod();
                    displayEventList(calendarManager.eventsDansPeriode(periodeSemaine[0], periodeSemaine[1]));
                    return null;
                }))
                .addOption("4", "Afficher les événements d'un JOUR précis", MenuOption.of(() -> {
                    LocalDateTime[] periodeJour = dateTimeHelper.inputDayPeriod();
                    displayEventList(calendarManager.eventsDansPeriode(periodeJour[0], periodeJour[1]));
                    return null;
                }))
                .addOption("5", "Retour", MenuOption.of(() -> null))
                .setDefaultAction(choice -> null)
                .build();
    }
    
    /**
     * Affiche le menu de visualisation des événements.
     */
    private void displayViewEventsMenu() {
        viewEventsMenu.display();
    }
    
    /**
     * Affiche une liste d'événements.
     * @param evenements La liste d'événements à afficher
     */
    private void displayEventList(List<Event> evenements) {
        new EventListPrinter(evenements).print();
    }
    
    /**
     * Classe pour afficher une liste d'événements.
     */
    private static class EventListPrinter {
        private final List<Event> events;
        
        /**
         * Constructeur.
         * @param events La liste d'événements
         */
        public EventListPrinter(List<Event> events) {
            this.events = events;
        }
        
        /**
         * Affiche la liste d'événements.
         */
        public void print() {
            new EventListPrinterAction(
                events.isEmpty(),
                () -> System.out.println("Aucun événement trouvé pour cette période."),
                () -> {
                    System.out.println("Événements trouvés : ");
                    events.forEach(e -> System.out.println("- " + e.description()));
                }
            ).execute();
        }
    }
    
    /**
     * Classe pour exécuter une action d'affichage de liste d'événements.
     */
    private static class EventListPrinterAction {
        private final boolean isEmpty;
        private final Runnable emptyAction;
        private final Runnable nonEmptyAction;
        
        /**
         * Constructeur.
         * @param isEmpty Indique si la liste est vide
         * @param emptyAction L'action à exécuter si la liste est vide
         * @param nonEmptyAction L'action à exécuter si la liste n'est pas vide
         */
        public EventListPrinterAction(boolean isEmpty, Runnable emptyAction, Runnable nonEmptyAction) {
            this.isEmpty = isEmpty;
            this.emptyAction = emptyAction;
            this.nonEmptyAction = nonEmptyAction;
        }
        
        /**
         * Exécute l'action.
         */
        public void execute() {
            new ConditionalExecutor(
                isEmpty,
                emptyAction,
                nonEmptyAction
            ).execute();
        }
    }
    
    /**
     * Classe pour exécuter une action en fonction d'une condition.
     */
    private static class ConditionalExecutor {
        private final boolean condition;
        private final Runnable trueAction;
        private final Runnable falseAction;
        
        /**
         * Constructeur.
         * @param condition La condition
         * @param trueAction L'action à exécuter si la condition est vraie
         * @param falseAction L'action à exécuter si la condition est fausse
         */
        public ConditionalExecutor(boolean condition, Runnable trueAction, Runnable falseAction) {
            this.condition = condition;
            this.trueAction = trueAction;
            this.falseAction = falseAction;
        }
        
        /**
         * Exécute l'action.
         */
        public void execute() {
            (condition ? trueAction : falseAction).run();
        }
    }
}
