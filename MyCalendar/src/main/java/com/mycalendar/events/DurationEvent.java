package com.mycalendar.events;

import java.util.Objects;

/**
 * Value Object représentant la durée d'un événement en minutes.
 * Cette classe est immuable.
 */
public class DurationEvent {
    private final int minutes;
    
    public DurationEvent(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("La durée ne peut pas être négative");
        }
        this.minutes = minutes;
    }
    
    public int getMinutes() {
        return minutes;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DurationEvent that = (DurationEvent) o;
        return minutes == that.minutes;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(minutes);
    }
    
    @Override
    public String toString() {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        
        if (hours > 0) {
            return hours + "h" + (remainingMinutes > 0 ? remainingMinutes + "min" : "");
        } else {
            return minutes + "min";
        }
    }
}
