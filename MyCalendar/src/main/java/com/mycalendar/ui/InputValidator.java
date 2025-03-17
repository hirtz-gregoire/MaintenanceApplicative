package com.mycalendar.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Classe pour valider les entrées utilisateur.
 * @param <T> Le type de l'entrée
 * @param <R> Le type du résultat
 */
public class InputValidator<T, R> {
    private final List<ValidationRule<T>> rules;
    private final Function<T, R> transformer;
    
    /**
     * Constructeur.
     * @param transformer Le transformateur de l'entrée
     */
    public InputValidator(Function<T, R> transformer) {
        this.rules = new ArrayList<>();
        this.transformer = transformer;
    }
    
    /**
     * Ajoute une règle de validation.
     * @param predicate Le prédicat de validation
     * @param errorMessage Le message d'erreur
     * @return Le validateur
     */
    public InputValidator<T, R> addRule(Predicate<T> predicate, String errorMessage) {
        rules.add(new ValidationRule<>(predicate, errorMessage));
        return this;
    }
    
    /**
     * Valide une entrée.
     * @param input L'entrée à valider
     * @return Le résultat de la validation
     */
    public ValidationResult<R> validate(T input) {
        return rules.stream()
                .filter(rule -> !rule.isValid(input))
                .findFirst()
                .map(rule -> ValidationResult.<R>invalid(rule.getErrorMessage()))
                .orElseGet(() -> ValidationResult.valid(transformer.apply(input)));
    }
    
    /**
     * Crée un nouveau validateur.
     * @param <T> Le type de l'entrée
     * @param <R> Le type du résultat
     * @param transformer Le transformateur de l'entrée
     * @return Le validateur
     */
    public static <T, R> InputValidator<T, R> create(Function<T, R> transformer) {
        return new InputValidator<>(transformer);
    }
    
    /**
     * Classe pour représenter une règle de validation.
     * @param <T> Le type de l'entrée
     */
    private static class ValidationRule<T> {
        private final Predicate<T> predicate;
        private final String errorMessage;
        
        /**
         * Constructeur.
         * @param predicate Le prédicat de validation
         * @param errorMessage Le message d'erreur
         */
        public ValidationRule(Predicate<T> predicate, String errorMessage) {
            this.predicate = predicate;
            this.errorMessage = errorMessage;
        }
        
        /**
         * Vérifie si l'entrée est valide.
         * @param input L'entrée à valider
         * @return true si l'entrée est valide, false sinon
         */
        public boolean isValid(T input) {
            return predicate.test(input);
        }
        
        /**
         * Retourne le message d'erreur.
         * @return Le message d'erreur
         */
        public String getErrorMessage() {
            return errorMessage;
        }
    }
    
    /**
     * Classe pour représenter le résultat d'une validation.
     * @param <R> Le type du résultat
     */
    public static class ValidationResult<R> {
        private final boolean valid;
        private final R value;
        private final String errorMessage;
        
        /**
         * Constructeur pour un résultat valide.
         * @param value La valeur
         */
        private ValidationResult(R value) {
            this.valid = true;
            this.value = value;
            this.errorMessage = null;
        }
        
        /**
         * Constructeur pour un résultat invalide.
         * @param errorMessage Le message d'erreur
         */
        private ValidationResult(String errorMessage) {
            this.valid = false;
            this.value = null;
            this.errorMessage = errorMessage;
        }
        
        /**
         * Vérifie si le résultat est valide.
         * @return true si le résultat est valide, false sinon
         */
        public boolean isValid() {
            return valid;
        }
        
        /**
         * Retourne la valeur du résultat.
         * @return La valeur du résultat
         */
        public Optional<R> getValue() {
            return Optional.ofNullable(value);
        }
        
        /**
         * Retourne le message d'erreur.
         * @return Le message d'erreur
         */
        public Optional<String> getErrorMessage() {
            return Optional.ofNullable(errorMessage);
        }
        
        /**
         * Crée un résultat valide.
         * @param <R> Le type du résultat
         * @param value La valeur
         * @return Le résultat
         */
        public static <R> ValidationResult<R> valid(R value) {
            return new ValidationResult<>(value);
        }
        
        /**
         * Crée un résultat invalide.
         * @param <R> Le type du résultat
         * @param errorMessage Le message d'erreur
         * @return Le résultat
         */
        public static <R> ValidationResult<R> invalid(String errorMessage) {
            return new ValidationResult<>(errorMessage);
        }
    }
}
