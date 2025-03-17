package com.mycalendar.ui;

import java.util.Scanner;

import com.mycalendar.user.User;
import com.mycalendar.user.UserManager;

public class AuthUI {
    private final Scanner scanner;
    private final UserManager userManager;
    private final CalendarUI calendarUI;
    
    public AuthUI(Scanner scanner, UserManager userManager, CalendarUI calendarUI) {
        this.scanner = scanner;
        this.userManager = userManager;
        this.calendarUI = calendarUI;
    }
    
    public User displayAuthMenu() {
        calendarUI.displayLogo();
        
        System.out.println("1 - Se connecter");
        System.out.println("2 - Créer un compte");
        System.out.print("Choix : ");
        
        String choix = scanner.nextLine();
        
        switch (choix) {
            case "1":
                return login();
            case "2":
                return register();
            default:
                return null;
        }
    }
    
    private User login() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine();
        
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();
        
        User user = userManager.authenticateUser(username, password);
        
        if (user == null) {
            System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
        }
        
        return user;
    }

    private User register() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine();
        
        if (userManager.userExists(username)) {
            System.out.println("Ce nom d'utilisateur existe déjà.");
            return null;
        }
        
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();
        
        System.out.print("Répéter mot de passe: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Les mots de passe ne correspondent pas...");
            return null;
        }
        
        boolean success = userManager.registerUser(username, password);
        
        if (success) {
            System.out.println("Compte créé avec succès.");
            return userManager.authenticateUser(username, password);
        } else {
            System.out.println("Erreur lors de la création du compte.");
            return null;
        }
    }
}
