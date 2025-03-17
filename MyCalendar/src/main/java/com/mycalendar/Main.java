package com.mycalendar;

import java.util.Scanner;

import com.mycalendar.ui.AuthUI;
import com.mycalendar.ui.CalendarUI;
import com.mycalendar.user.User;
import com.mycalendar.user.UserManager;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalendarManager calendarManager = new CalendarManager();
        UserManager userManager = new UserManager();
        
        CalendarUI calendarUI = new CalendarUI(scanner, calendarManager);
        AuthUI authUI = new AuthUI(scanner, userManager, calendarUI);
        
        try {
            // Boucle prncipale de l'application
            while (true) {
                User currentUser = null;
                while (currentUser == null) {
                    currentUser = authUI.displayAuthMenu();
                }
                
                boolean continuer = true;
                while (continuer && currentUser != null) {
                    continuer = calendarUI.displayMainMenu(currentUser);
                    
                    if (!continuer) {
                        currentUser = null;
                    }
                }
            }
        } finally {
            scanner.close();
        }
    }
}
