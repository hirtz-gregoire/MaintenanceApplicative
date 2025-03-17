package com.mycalendar.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Classe pour fournir des descriptions pour les options de menu.
 * @param <T> Le type de l'option
 */
public class DescriptionProvider<T> {
    private final Map<T, String> descriptions;
    private final Function<T, String> defaultProvider;
    
    /**
     * Constructeur.
     * @param defaultProvider Le fournisseur de description par défaut
     */
    public DescriptionProvider(Function<T, String> defaultProvider) {
        this.descriptions = new HashMap<>();
        this.defaultProvider = defaultProvider;
    }
    
    /**
     * Ajoute une description pour une option.
     * @param option L'option
     * @param description La description
     * @return Le fournisseur de description
     */
    public DescriptionProvider<T> addDescription(T option, String description) {
        descriptions.put(option, description);
        return this;
    }
    
    /**
     * Retourne la description d'une option.
     * @param option L'option
     * @return La description
     */
    public String getDescription(T option) {
        return descriptions.getOrDefault(option, defaultProvider.apply(option));
    }
    
    /**
     * Crée un nouveau fournisseur de description.
     * @param <T> Le type de l'option
     * @param defaultProvider Le fournisseur de description par défaut
     * @return Le fournisseur de description
     */
    public static <T> DescriptionProvider<T> create(Function<T, String> defaultProvider) {
        return new DescriptionProvider<>(defaultProvider);
    }
}
