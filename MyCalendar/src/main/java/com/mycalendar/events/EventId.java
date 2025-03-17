package com.mycalendar.events;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object représentant l'identifiant unique d'un événement.
 * Cette classe est immuable.
 */
public class EventId {
    private final String value;
    
    public EventId() {
        this(UUID.randomUUID().toString());
    }
    
    public EventId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être vide");
        }
        this.value = id.trim();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventId eventId = (EventId) o;
        return Objects.equals(value, eventId.value);
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
