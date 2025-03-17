package com.mycalendar.ui;

import java.util.function.Supplier;

/**
 * Interface pour représenter un état de l'application.
 */
public interface ApplicationState {
    /**
     * Exécute l'état.
     * @return Le prochain état
     */
    ApplicationState execute();
    
    /**
     * Vérifie si l'état est terminal.
     * @return true si l'état est terminal, false sinon
     */
    default boolean isTerminal() {
        return false;
    }
    
    /**
     * Crée un état terminal.
     * @return L'état terminal
     */
    static ApplicationState terminal() {
        return new TerminalState();
    }
    
    /**
     * Crée un état à partir d'un fournisseur.
     * @param stateSupplier Le fournisseur d'état
     * @return L'état
     */
    static ApplicationState of(Supplier<ApplicationState> stateSupplier) {
        return new DelegatingState(stateSupplier);
    }
    
    /**
     * Classe pour représenter un état terminal.
     */
    class TerminalState implements ApplicationState {
        @Override
        public ApplicationState execute() {
            return this;
        }
        
        @Override
        public boolean isTerminal() {
            return true;
        }
    }
    
    /**
     * Classe pour représenter un état qui délègue à un fournisseur.
     */
    class DelegatingState implements ApplicationState {
        private final Supplier<ApplicationState> stateSupplier;
        
        /**
         * Constructeur.
         * @param stateSupplier Le fournisseur d'état
         */
        public DelegatingState(Supplier<ApplicationState> stateSupplier) {
            this.stateSupplier = stateSupplier;
        }
        
        @Override
        public ApplicationState execute() {
            return stateSupplier.get();
        }
    }
}
