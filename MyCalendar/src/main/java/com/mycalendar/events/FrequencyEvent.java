package com.mycalendar.events;

import java.util.Objects;

/**
 * Value Object représentant la fréquence d'un événement périodique en jours.
 * Cette classe est immuable.
 */
public class FrequencyEvent {
    private final int days;
    
    public FrequencyEvent(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("La fréquence doit être positive");
        }
        this.days = days;
    }
    
    public int getDays() {
        return days;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequencyEvent that = (FrequencyEvent) o;
        return days == that.days;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(days);
    }
    
    @Override
    public String toString() {
        if (days == 1) {
            return "tous les jours";
        } else if (days == 7) {
            return "toutes les semaines";
        } else if (days == 30 || days == 31) {
            return "tous les mois";
        } else if (days == 365 || days == 366) {
            return "tous les ans";
        } else {
            return "tous les " + days + " jours";
        }
    }
}
