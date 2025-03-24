package com.mycalendar.events;

import java.util.Objects;

/**
 * Value Object représentant la priorité d'une tâche.
 * Cette classe est immuable.
 */
public class PriorityEvent {
    private final String value;
    
    public PriorityEvent(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            throw new IllegalArgumentException("La priorité ne peut pas être vide");
        }
        this.value = priority.trim().toUpperCase();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriorityEvent that = (PriorityEvent) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
