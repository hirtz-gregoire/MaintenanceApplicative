package com.mycalendar.ui;

import java.io.IOException;
import java.util.Scanner;

import com.mycalendar.CalendarManager;
import com.mycalendar.user.User;
import com.mycalendar.user.UserManager;

/**
 * Classe pour gérer l'application.
 */
public class Application {
    private final Scanner scanner;
    private final CalendarManager calendarManager;
    private final UserManager userManager;
    private final AuthUI authUI;
    private final CalendarUI calendarUI;
    
    /**
     * Constructeur.
     */
    public Application() {
        this.scanner = new Scanner(System.in);
        this.calendarManager = new CalendarManager();
        this.userManager = new UserManager();
        this.calendarUI = new CalendarUI(scanner, calendarManager);
        this.authUI = new AuthUI(scanner, userManager, calendarUI);
        
        // Charger les événements depuis le fichier par défaut
        loadEventsFromDefaultFile();
    }
    
    /**
     * Charge les événements depuis le fichier par défaut.
     */
    private void loadEventsFromDefaultFile() {
        try {
            calendarManager.loadEventsFromDefaultFile();
            System.out.println("Événements chargés depuis le fichier par défaut.");
        } catch (IOException e) {
            System.out.println("Aucun fichier d'événements trouvé. Un nouveau calendrier sera créé.");
        }
    }
    
    /**
     * Exécute l'application.
     */
    public void run() {
        try {
            ApplicationState state = new AuthenticationState();
            
            while (!state.isTerminal()) {
                state = state.execute();
            }
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Classe pour représenter l'état d'authentification.
     */
    private class AuthenticationState implements ApplicationState {
        @Override
        public ApplicationState execute() {
            User user = authUI.displayAuthMenu();
            
            return user != null ? new MainMenuState(user) : this;
        }
    }
    
    /**
     * Classe pour représenter l'état du menu principal.
     */
    private class MainMenuState implements ApplicationState {
        private final User user;
        
        /**
         * Constructeur.
         * @param user L'utilisateur connecté
         */
        public MainMenuState(User user) {
            this.user = user;
        }
        
        @Override
        public ApplicationState execute() {
            boolean continuer = calendarUI.displayMainMenu(user);
            
            return continuer ? this : new AuthenticationState();
        }
    }
}
