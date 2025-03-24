package com.mycalendar.ui;

import java.util.function.Supplier;

/**
 * Classe pour représenter une option de menu.
 * @param <T> Le type de retour de l'option
 */
public class MenuOption<T> {
    private final Supplier<T> action;
    private final String description;
    
    /**
     * Constructeur.
     * @param description La description de l'option
     * @param action L'action à exécuter
     */
    public MenuOption(String description, Supplier<T> action) {
        this.description = description;
        this.action = action;
    }
    
    /**
     * Exécute l'option de menu.
     * @return Le résultat de l'exécution
     */
    public T execute() {
        return action.get();
    }
    
    /**
     * Retourne la description de l'option.
     * @return La description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Crée une option de menu à partir d'un Supplier.
     * @param <T> Le type de retour de l'option
     * @param supplier Le fournisseur de résultat
     * @return L'option de menu
     */
    public static <T> MenuOption<T> of(Supplier<T> supplier) {
        return new MenuOption<>("", supplier);
    }
}
