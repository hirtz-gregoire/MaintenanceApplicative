package com.mycalendar.ui;

import java.util.function.Supplier;

/**
 * Interface fonctionnelle pour représenter une option de menu.
 * @param <T> Le type de retour de l'option
 */
@FunctionalInterface
public interface MenuOption<T> {
    /**
     * Exécute l'option de menu.
     * @return Le résultat de l'exécution
     */
    T execute();
    
    /**
     * Crée une option de menu à partir d'un Supplier.
     * @param <T> Le type de retour de l'option
     * @param supplier Le fournisseur de résultat
     * @return L'option de menu
     */
    static <T> MenuOption<T> of(Supplier<T> supplier) {
        return supplier::get;
    }
}
