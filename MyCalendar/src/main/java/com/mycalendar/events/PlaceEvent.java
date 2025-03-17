package com.mycalendar.events;

import java.util.Objects;

/**
 * Value Object représentant le lieu d'un événement.
 * Cette classe est immuable.
 */
public class PlaceEvent {
    private final String value;
    
    public PlaceEvent(String place) {
        // Le lieu peut être vide pour certains types d'événements
        this.value = place != null ? place.trim() : "";
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceEvent that = (PlaceEvent) o;
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
