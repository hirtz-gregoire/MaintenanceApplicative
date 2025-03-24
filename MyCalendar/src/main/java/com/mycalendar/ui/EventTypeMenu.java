package com.mycalendar.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mycalendar.events.TypeEvent;
import com.mycalendar.user.User;

/**
 * Classe responsable de la gestion du menu des types d'événements.
 * Cette classe utilise l'enum TypeEvent pour générer dynamiquement les options de menu.
 */
public class EventTypeMenu {
    private final Scanner scanner;
    private final EventInputHelper eventInputHelper;
    private final DescriptionProvider<TypeEvent> descriptionProvider;
    private Menu<Boolean> eventTypeMenu;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param eventInputHelper L'aide à la saisie d'événements
     */
    public EventTypeMenu(Scanner scanner, EventInputHelper eventInputHelper) {
        this.scanner = scanner;
        this.eventInputHelper = eventInputHelper;
        this.descriptionProvider = createDescriptionProvider();
        this.eventTypeMenu = createEventTypeMenu();
    }
    
    /**
     * Crée le fournisseur de descriptions pour les types d'événements.
     * @return Le fournisseur de descriptions
     */
    private DescriptionProvider<TypeEvent> createDescriptionProvider() {
        return DescriptionProvider.<TypeEvent>create(type -> "événement")
                .addDescription(TypeEvent.RDV_PERSONNEL, "rendez-vous personnel")
                .addDescription(TypeEvent.REUNION, "réunion")
                .addDescription(TypeEvent.PERIODIQUE, "événement périodique")
                .addDescription(TypeEvent.TASK, "tâche")
                .addDescription(TypeEvent.RAPPEL, "rappel");
    }
    
    /**
     * Crée le menu des types d'événements.
     * @return Le menu des types d'événements
     */
    private Menu<Boolean> createEventTypeMenu() {
        MenuBuilder<Boolean> builder = MenuBuilder.create(scanner, "Menu d'ajout d'événements");
        
        List<TypeEvent> types = new ArrayList<>();
        types.add(TypeEvent.RDV_PERSONNEL);
        types.add(TypeEvent.REUNION);
        types.add(TypeEvent.PERIODIQUE);
        types.add(TypeEvent.TASK);
        types.add(TypeEvent.RAPPEL);
        
        for (int i = 0; i < types.size(); i++) {
            TypeEvent type = types.get(i);
            String article = type == TypeEvent.REUNION ? "une" : "un";
            String description = "Ajouter " + article + " " + descriptionProvider.getDescription(type);
            
            builder.addOption(String.valueOf(i + 1), description, MenuOption.of(() -> {
                executeAction(type, currentUser);
                return true;
            }));
        }
        
        builder.addOption(String.valueOf(types.size() + 1), "Retour", MenuOption.of(() -> false));
        builder.setDefaultAction(choice -> false);
        
        return builder.build();
    }
    
    /**
     * Retourne la description d'un type d'événement.
     * @param type Le type d'événement
     * @return La description du type d'événement
     */
    public String getDescription(TypeEvent type) {
        return descriptionProvider.getDescription(type);
    }
    
    // Variable pour stocker l'utilisateur courant
    private User currentUser;
    
    /**
     * Affiche le menu des types d'événements.
     * @param user L'utilisateur connecté
     * @return true si une action a été effectuée, false sinon
     */
    public boolean displayMenu(User user) {
        this.currentUser = user;
        this.eventTypeMenu = createEventTypeMenu();
        return eventTypeMenu.display();
    }
    
    /**
     * Exécute l'action associée à un type d'événement.
     * @param type Le type d'événement
     * @param user L'utilisateur connecté
     */
    public void executeAction(TypeEvent type, User user) {
        if (type == TypeEvent.TASK) {
            eventInputHelper.inputTaskEvent(user);
        } else if (type == TypeEvent.RAPPEL) {
            eventInputHelper.inputReminderEvent(user);
        } else {
            new EventTypeAction(
                type,
                user,
                TypeEvent.RDV_PERSONNEL, () -> eventInputHelper.inputPersonalEvent(user),
                TypeEvent.REUNION, () -> eventInputHelper.inputMeetingEvent(user),
                TypeEvent.PERIODIQUE, () -> eventInputHelper.inputPeriodicEvent(user)
            ).execute();
        }
    }
    
    /**
     * Classe pour exécuter une action en fonction du type d'événement.
     */
    private static class EventTypeAction {
        private final TypeEvent type;
        private final TypeEvent type1;
        private final Runnable action1;
        private final TypeEvent type2;
        private final Runnable action2;
        private final TypeEvent type3;
        private final Runnable action3;
        
        /**
         * Constructeur.
         * @param type Le type d'événement
         * @param user L'utilisateur connecté (non utilisé)
         * @param type1 Le premier type d'événement
         * @param action1 L'action associée au premier type d'événement
         * @param type2 Le deuxième type d'événement
         * @param action2 L'action associée au deuxième type d'événement
         * @param type3 Le troisième type d'événement
         * @param action3 L'action associée au troisième type d'événement
         */
        public EventTypeAction(TypeEvent type, User user, TypeEvent type1, Runnable action1, TypeEvent type2, Runnable action2, TypeEvent type3, Runnable action3) {
            this.type = type;
            this.type1 = type1;
            this.action1 = action1;
            this.type2 = type2;
            this.action2 = action2;
            this.type3 = type3;
            this.action3 = action3;
        }
        
        /**
         * Exécute l'action.
         */
        public void execute() {
            new TypeEventMatcher(
                type,
                type1, action1,
                type2, action2,
                type3, action3
            ).execute();
        }
    }
    
    /**
     * Classe pour exécuter une action en fonction du type d'événement.
     */
    private static class TypeEventMatcher {
        private final TypeEvent type;
        private final TypeEvent type1;
        private final Runnable action1;
        private final TypeEvent type2;
        private final Runnable action2;
        private final TypeEvent type3;
        private final Runnable action3;
        
        /**
         * Constructeur.
         * @param type Le type d'événement
         * @param type1 Le premier type d'événement
         * @param action1 L'action associée au premier type d'événement
         * @param type2 Le deuxième type d'événement
         * @param action2 L'action associée au deuxième type d'événement
         * @param type3 Le troisième type d'événement
         * @param action3 L'action associée au troisième type d'événement
         */
        public TypeEventMatcher(TypeEvent type, TypeEvent type1, Runnable action1, TypeEvent type2, Runnable action2, TypeEvent type3, Runnable action3) {
            this.type = type;
            this.type1 = type1;
            this.action1 = action1;
            this.type2 = type2;
            this.action2 = action2;
            this.type3 = type3;
            this.action3 = action3;
        }
        
        /**
         * Exécute l'action.
         */
        public void execute() {
            new TypeEventExecutor(
                type.equals(type1),
                action1,
                () -> new TypeEventExecutor(
                    type.equals(type2),
                    action2,
                    () -> new TypeEventExecutor(
                        type.equals(type3),
                        action3,
                        () -> {}
                    ).execute()
                ).execute()
            ).execute();
        }
    }
    
    /**
     * Classe pour exécuter une action en fonction d'une condition.
     */
    private static class TypeEventExecutor {
        private final boolean condition;
        private final Runnable trueAction;
        private final Runnable falseAction;
        
        /**
         * Constructeur.
         * @param condition La condition
         * @param trueAction L'action à exécuter si la condition est vraie
         * @param falseAction L'action à exécuter si la condition est fausse
         */
        public TypeEventExecutor(boolean condition, Runnable trueAction, Runnable falseAction) {
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
