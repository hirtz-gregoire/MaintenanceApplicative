package com.mycalendar.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Builder pour faciliter la création de menus.
 * @param <T> Le type de retour du menu
 */
public class MenuBuilder<T> {
    private final Scanner scanner;
    private final String title;
    private final Map<String, String> optionDescriptions;
    private final Map<String, MenuOption<T>> options;
    private Function<String, T> defaultAction;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param title Le titre du menu
     */
    public MenuBuilder(Scanner scanner, String title) {
        this.scanner = scanner;
        this.title = title;
        this.optionDescriptions = new HashMap<>();
        this.options = new HashMap<>();
        this.defaultAction = choice -> null;
    }
    
    /**
     * Ajoute une option au menu.
     * @param key La clé de l'option
     * @param description La description de l'option
     * @param option L'option à exécuter
     * @return Le builder
     */
    public MenuBuilder<T> addOption(String key, String description, MenuOption<T> option) {
        optionDescriptions.put(key, description);
        options.put(key, option);
        return this;
    }
    
    /**
     * Définit l'action par défaut à exécuter si l'option n'existe pas.
     * @param defaultAction L'action par défaut
     * @return Le builder
     */
    public MenuBuilder<T> setDefaultAction(Function<String, T> defaultAction) {
        this.defaultAction = defaultAction;
        return this;
    }
    
    /**
     * Construit le menu.
     * @return Le menu
     */
    public Menu<T> build() {
        Menu<T> menu = new Menu<>(scanner, title, defaultAction);
        
        options.forEach((key, option) -> {
            menu.addOption(key, optionDescriptions.get(key), option);
        });
        
        return menu;
    }
    
    /**
     * Crée un nouveau builder.
     * @param <T> Le type de retour du menu
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param title Le titre du menu
     * @return Le builder
     */
    public static <T> MenuBuilder<T> create(Scanner scanner, String title) {
        return new MenuBuilder<>(scanner, title);
    }
}
