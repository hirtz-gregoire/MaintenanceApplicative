package com.mycalendar.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Classe générique pour représenter un menu.
 * @param <T> Le type de retour du menu
 */
public class Menu<T> {
    private final Scanner scanner;
    private final String title;
    private final Map<String, MenuOption<T>> options;
    private final Function<String, T> defaultAction;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param title Le titre du menu
     * @param defaultAction L'action par défaut à exécuter si l'option n'existe pas
     */
    public Menu(Scanner scanner, String title, Function<String, T> defaultAction) {
        this.scanner = scanner;
        this.title = title;
        this.options = new LinkedHashMap<>();
        this.defaultAction = defaultAction;
    }
    
    /**
     * Ajoute une option au menu.
     * @param key La clé de l'option
     * @param description La description de l'option
     * @param option L'option à exécuter
     * @return Le menu
     */
    public Menu<T> addOption(String key, String description, MenuOption<T> option) {
        options.put(key, option);
        return this;
    }
    
    /**
     * Affiche le menu et exécute l'option choisie.
     * @return Le résultat de l'exécution de l'option
     */
    public T display() {
        System.out.println("\n=== " + title + " ===");
        
        options.forEach((key, option) -> {
            System.out.println(key + " - " + getOptionDescription(key));
        });
        
        System.out.print("Votre choix : ");
        String choice = scanner.nextLine();
        
        return Optional.ofNullable(options.get(choice))
                .map(MenuOption::execute)
                .orElseGet(() -> defaultAction.apply(choice));
    }
    
    /**
     * Retourne la description d'une option.
     * @param key La clé de l'option
     * @return La description de l'option
     */
    private String getOptionDescription(String key) {
        return key;
    }
}
