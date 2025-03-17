package com.mycalendar.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import com.mycalendar.events.TypeEvent;
import com.mycalendar.user.User;

/**
 * Classe responsable de la gestion du menu des types d'événements.
 * Cette classe utilise l'enum TypeEvent pour générer dynamiquement les options de menu.
 */
public class EventTypeMenu {
    private final Scanner scanner;
    private final EventInputHelper eventInputHelper;
    private final Map<TypeEvent, String> descriptions;
    private final Map<TypeEvent, Consumer<User>> actions;
    
    public EventTypeMenu(Scanner scanner, EventInputHelper eventInputHelper) {
        this.scanner = scanner;
        this.eventInputHelper = eventInputHelper;
        this.descriptions = initDescriptions();
        this.actions = initActions();
    }
    
    /**
     * Initialise les descriptions des types d'événements.
     * @return Une map associant chaque type d'événement à sa description
     */
    private Map<TypeEvent, String> initDescriptions() {
        Map<TypeEvent, String> map = new HashMap<>();
        map.put(TypeEvent.RDV_PERSONNEL, "rendez-vous personnel");
        map.put(TypeEvent.REUNION, "réunion");
        map.put(TypeEvent.PERIODIQUE, "événement périodique");
        return map;
    }
    
    /**
     * Initialise les actions associées à chaque type d'événement.
     * @return Une map associant chaque type d'événement à son action
     */
    private Map<TypeEvent, Consumer<User>> initActions() {
        Map<TypeEvent, Consumer<User>> map = new HashMap<>();
        map.put(TypeEvent.RDV_PERSONNEL, eventInputHelper::inputPersonalEvent);
        map.put(TypeEvent.REUNION, eventInputHelper::inputMeetingEvent);
        map.put(TypeEvent.PERIODIQUE, eventInputHelper::inputPeriodicEvent);
        return map;
    }
    
    /**
     * Retourne la description d'un type d'événement.
     * @param type Le type d'événement
     * @return La description du type d'événement
     */
    public String getDescription(TypeEvent type) {
        return descriptions.getOrDefault(type, "événement");
    }
    
    /**
     * Affiche le menu des types d'événements.
     * @param user L'utilisateur connecté
     * @return true si une action a été effectuée, false sinon
     */
    public boolean displayMenu(User user) {
        System.out.println("\n=== Menu d'ajout d'événements ===");
        
        List<TypeEvent> types = new ArrayList<>(descriptions.keySet());
        for (int i = 0; i < types.size(); i++) {
            TypeEvent type = types.get(i);
            String article = type == TypeEvent.REUNION ? "une" : "un";
            System.out.println((i + 1) + " - Ajouter " + article + " " + getDescription(type));
        }
        
        System.out.println((types.size() + 1) + " - Retour");
        System.out.print("Votre choix : ");
        
        try {
            int choix = Integer.parseInt(scanner.nextLine());
            
            if (choix >= 1 && choix <= types.size()) {
                TypeEvent selectedType = types.get(choix - 1);
                Consumer<User> action = actions.get(selectedType);
                
                if (action != null) {
                    action.accept(user);
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Choix invalide.");
        }
        
        return false;
    }
    
    /**
     * Exécute l'action associée à un type d'événement.
     * @param type Le type d'événement
     * @param user L'utilisateur connecté
     */
    public void executeAction(TypeEvent type, User user) {
        Consumer<User> action = actions.get(type);
        
        if (action != null) {
            action.accept(user);
        }
    }
}
