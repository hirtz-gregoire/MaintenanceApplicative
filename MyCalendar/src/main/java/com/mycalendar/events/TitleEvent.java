package com.mycalendar.events;

import java.util.Objects;

/**
 * Value Object représentant le titre d'un événement.
 * Cette classe est immuable.
 */
public class TitleEvent {
    private final String value;
    
    public TitleEvent(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }
        this.value = title.trim();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleEvent that = (TitleEvent) o;
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
