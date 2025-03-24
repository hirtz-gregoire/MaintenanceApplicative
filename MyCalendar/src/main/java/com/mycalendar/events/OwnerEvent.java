package com.mycalendar.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Value Object représentant le propriétaire d'un événement.
 * Cette classe est immuable.
 */
public class OwnerEvent {
    private final String value;
    
    @JsonCreator
    public OwnerEvent(@JsonProperty("value") String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Le propriétaire ne peut pas être vide");
        }
        this.value = owner.trim();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnerEvent that = (OwnerEvent) o;
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
