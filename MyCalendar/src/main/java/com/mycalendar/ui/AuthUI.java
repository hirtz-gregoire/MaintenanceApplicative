package com.mycalendar.ui;

import java.util.Scanner;
import java.util.function.Function;

import com.mycalendar.ui.InputValidator.ValidationResult;
import com.mycalendar.user.User;
import com.mycalendar.user.UserManager;

/**
 * Classe responsable de l'interface utilisateur d'authentification.
 */
public class AuthUI {
    private final Scanner scanner;
    private final UserManager userManager;
    private final CalendarUI calendarUI;
    private final Menu<User> authMenu;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param userManager Le gestionnaire d'utilisateurs
     * @param calendarUI L'interface utilisateur du calendrier
     */
    public AuthUI(Scanner scanner, UserManager userManager, CalendarUI calendarUI) {
        this.scanner = scanner;
        this.userManager = userManager;
        this.calendarUI = calendarUI;
        this.authMenu = createAuthMenu();
    }
    
    /**
     * Affiche le menu d'authentification.
     * @return L'utilisateur authentifié ou null si l'authentification a échoué
     */
    public User displayAuthMenu() {
        calendarUI.displayLogo();
        return authMenu.display();
    }
    
    /**
     * Crée le menu d'authentification.
     * @return Le menu d'authentification
     */
    private Menu<User> createAuthMenu() {
        return MenuBuilder.<User>create(scanner, "Menu d'authentification")
                .addOption("1", "Se connecter", MenuOption.of(this::login))
                .addOption("2", "Créer un compte", MenuOption.of(this::register))
                .setDefaultAction(choice -> null)
                .build();
    }
    
    /**
     * Gère la connexion d'un utilisateur.
     * @return L'utilisateur authentifié ou null si l'authentification a échoué
     */
    private User login() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine();
        
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();
        
        User user = userManager.authenticateUser(username, password);
        
        new MessagePrinter(
            () -> user == null,
            "Nom d'utilisateur ou mot de passe incorrect."
        ).print();
        
        return user;
    }
    
    /**
     * Gère l'enregistrement d'un nouvel utilisateur.
     * @return Le nouvel utilisateur enregistré ou null si l'enregistrement a échoué
     */
    private User register() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine();
        
        ValidationResult<String> usernameValidation = validateUsername(username);
        
        return usernameValidation.getValue()
                .map(this::registerWithValidUsername)
                .orElseGet(() -> {
                    usernameValidation.getErrorMessage().ifPresent(System.out::println);
                    return null;
                });
    }
    
    /**
     * Enregistre un utilisateur avec un nom d'utilisateur valide.
     * @param username Le nom d'utilisateur
     * @return L'utilisateur enregistré ou null si l'enregistrement a échoué
     */
    private User registerWithValidUsername(String username) {
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();
        
        System.out.print("Répéter mot de passe: ");
        String confirmPassword = scanner.nextLine();
        
        ValidationResult<String> passwordValidation = validatePassword(password, confirmPassword);
        
        return passwordValidation.getValue()
                .map(validPassword -> registerUser(username, validPassword))
                .orElseGet(() -> {
                    passwordValidation.getErrorMessage().ifPresent(System.out::println);
                    return null;
                });
    }
    
    /**
     * Enregistre un utilisateur.
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe
     * @return L'utilisateur enregistré ou null si l'enregistrement a échoué
     */
    private User registerUser(String username, String password) {
        boolean success = userManager.registerUser(username, password);
        
        return new RegistrationResultHandler(
            success,
            () -> {
                System.out.println("Compte créé avec succès.");
                return userManager.authenticateUser(username, password);
            },
            () -> {
                System.out.println("Erreur lors de la création du compte.");
                return null;
            }
        ).handle();
    }
    
    /**
     * Valide un nom d'utilisateur.
     * @param username Le nom d'utilisateur
     * @return Le résultat de la validation
     */
    private ValidationResult<String> validateUsername(String username) {
        return InputValidator.<String, String>create(Function.identity())
                .addRule(name -> !userManager.userExists(name), "Ce nom d'utilisateur existe déjà.")
                .validate(username);
    }
    
    /**
     * Valide un mot de passe.
     * @param password Le mot de passe
     * @param confirmPassword La confirmation du mot de passe
     * @return Le résultat de la validation
     */
    private ValidationResult<String> validatePassword(String password, String confirmPassword) {
        return InputValidator.<String, String>create(Function.identity())
                .addRule(pwd -> pwd.equals(confirmPassword), "Les mots de passe ne correspondent pas...")
                .validate(password);
    }
    
    /**
     * Classe pour gérer le résultat de l'enregistrement.
     */
    private static class RegistrationResultHandler {
        private final boolean success;
        private final Supplier<User> successAction;
        private final Supplier<User> failureAction;
        
        /**
         * Constructeur.
         * @param success Le résultat de l'enregistrement
         * @param successAction L'action à exécuter en cas de succès
         * @param failureAction L'action à exécuter en cas d'échec
         */
        public RegistrationResultHandler(boolean success, Supplier<User> successAction, Supplier<User> failureAction) {
            this.success = success;
            this.successAction = successAction;
            this.failureAction = failureAction;
        }
        
        /**
         * Gère le résultat de l'enregistrement.
         * @return L'utilisateur enregistré ou null si l'enregistrement a échoué
         */
        public User handle() {
            return success ? successAction.get() : failureAction.get();
        }
    }
    
    /**
     * Classe pour afficher un message.
     */
    private static class MessagePrinter {
        private final Supplier<Boolean> condition;
        private final String message;
        
        /**
         * Constructeur.
         * @param condition La condition pour afficher le message
         * @param message Le message à afficher
         */
        public MessagePrinter(Supplier<Boolean> condition, String message) {
            this.condition = condition;
            this.message = message;
        }
        
        /**
         * Affiche le message si la condition est vraie.
         */
        public void print() {
            new MessagePrinterAction(
                condition.get(),
                () -> System.out.println(message),
                () -> {}
            ).execute();
        }
    }
    
    /**
     * Classe pour exécuter une action d'affichage de message.
     */
    private static class MessagePrinterAction {
        private final boolean condition;
        private final Runnable trueAction;
        private final Runnable falseAction;
        
        /**
         * Constructeur.
         * @param condition La condition
         * @param trueAction L'action à exécuter si la condition est vraie
         * @param falseAction L'action à exécuter si la condition est fausse
         */
        public MessagePrinterAction(boolean condition, Runnable trueAction, Runnable falseAction) {
            this.condition = condition;
            this.trueAction = trueAction;
            this.falseAction = falseAction;
        }
        
        /**
         * Exécute l'action.
         */
        public void execute() {
            new ConditionalExecutor(
                condition,
                trueAction,
                falseAction
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
    
    /**
     * Interface pour représenter un fournisseur.
     * @param <T> Le type du résultat
     */
    @FunctionalInterface
    private interface Supplier<T> {
        /**
         * Fournit un résultat.
         * @return Le résultat
         */
        T get();
    }
}
