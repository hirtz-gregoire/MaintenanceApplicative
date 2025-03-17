package com.mycalendar.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Classe pour collecter des entrées utilisateur.
 * @param <T> Le type de l'entrée
 */
public class InputCollector<T> {
    private final Scanner scanner;
    private final String prompt;
    private final Function<String, T> parser;
    private final Predicate<String> continueCondition;
    private final List<T> items;
    
    /**
     * Constructeur.
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param prompt Le message à afficher pour demander une entrée
     * @param parser Le parser pour convertir l'entrée en type T
     * @param continueCondition La condition pour continuer à collecter des entrées
     */
    public InputCollector(Scanner scanner, String prompt, Function<String, T> parser, Predicate<String> continueCondition) {
        this.scanner = scanner;
        this.prompt = prompt;
        this.parser = parser;
        this.continueCondition = continueCondition;
        this.items = new ArrayList<>();
    }
    
    /**
     * Collecte les entrées utilisateur.
     * @return La liste des entrées collectées
     */
    public List<T> collect() {
        return collect(new ArrayList<>());
    }
    
    /**
     * Collecte les entrées utilisateur en partant d'une liste initiale.
     * @param initialItems La liste initiale
     * @return La liste des entrées collectées
     */
    public List<T> collect(List<T> initialItems) {
        items.addAll(initialItems);
        
        collectNext();
        
        return new ArrayList<>(items);
    }
    
    /**
     * Collecte la prochaine entrée.
     */
    private void collectNext() {
        System.out.println(prompt);
        String response = scanner.nextLine();
        
        new ContinueCollector(
            () -> {
                items.add(parser.apply(response));
                System.out.println("Ajouter un autre élément ? (oui / non)");
                String answer = scanner.nextLine();
                return continueCondition.test(answer) ? new ContinueAction() : new StopAction();
            },
            () -> {}
        ).execute();
    }
    
    /**
     * Crée un nouveau collecteur d'entrées.
     * @param <T> Le type de l'entrée
     * @param scanner Le scanner pour lire les entrées utilisateur
     * @param prompt Le message à afficher pour demander une entrée
     * @param parser Le parser pour convertir l'entrée en type T
     * @param continueCondition La condition pour continuer à collecter des entrées
     * @return Le collecteur d'entrées
     */
    public static <T> InputCollector<T> create(Scanner scanner, String prompt, Function<String, T> parser, Predicate<String> continueCondition) {
        return new InputCollector<>(scanner, prompt, parser, continueCondition);
    }
    
    /**
     * Interface pour représenter une action de collecte.
     */
    private interface CollectAction {
        void execute();
    }
    
    /**
     * Classe pour représenter l'action de continuer la collecte.
     */
    private class ContinueAction implements CollectAction {
        @Override
        public void execute() {
            collectNext();
        }
    }
    
    /**
     * Classe pour représenter l'action d'arrêter la collecte.
     */
    private class StopAction implements CollectAction {
        @Override
        public void execute() {
            // Ne rien faire
        }
    }
    
    /**
     * Classe pour collecter la prochaine entrée.
     */
    private class ContinueCollector {
        private final Supplier<CollectAction> actionSupplier;
        private final Runnable defaultAction;
        
        /**
         * Constructeur.
         * @param actionSupplier Le fournisseur d'action
         * @param defaultAction L'action par défaut
         */
        public ContinueCollector(Supplier<CollectAction> actionSupplier, Runnable defaultAction) {
            this.actionSupplier = actionSupplier;
            this.defaultAction = defaultAction;
        }
        
        /**
         * Exécute l'action.
         */
        public void execute() {
            try {
                actionSupplier.get().execute();
            } catch (Exception e) {
                defaultAction.run();
            }
        }
    }
}
