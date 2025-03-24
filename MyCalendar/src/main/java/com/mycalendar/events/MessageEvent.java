package com.mycalendar.events;

import java.util.Objects;

/**
 * Value Object représentant le message d'un rappel.
 * Cette classe est immuable.
 */
public class MessageEvent {
    private final String value;
    
    public MessageEvent(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Le message ne peut pas être null");
        }
        this.value = message.trim();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEvent that = (MessageEvent) o;
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
