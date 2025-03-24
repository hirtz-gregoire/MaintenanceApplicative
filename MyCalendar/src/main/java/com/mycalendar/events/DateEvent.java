package com.mycalendar.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object représentant la date et l'heure d'un événement.
 * Cette classe est immuable.
 */
public class DateEvent {
    private final LocalDateTime dateTime;
    
    @JsonCreator
    public DateEvent(@JsonProperty("dateTime") LocalDateTime dateTime) {
        this.dateTime = Objects.requireNonNull(dateTime, "La date et l'heure ne peuvent pas être nulles");
    }
    
    public DateEvent(int year, int month, int day, int hour, int minute) {
        this(LocalDateTime.of(year, month, day, hour, minute));
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public DateEvent plusMinutes(DurationEvent duration) {
        return new DateEvent(dateTime.plusMinutes(duration.getMinutes()));
    }
    
    public DateEvent plusDays(int days) {
        return new DateEvent(dateTime.plusDays(days));
    }
    
    public boolean isBefore(DateEvent other) {
        return this.dateTime.isBefore(other.dateTime);
    }
    
    public boolean isAfter(DateEvent other) {
        return this.dateTime.isAfter(other.dateTime);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateEvent dateEvent = (DateEvent) o;
        return Objects.equals(dateTime, dateEvent.dateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }
    
    @Override
    public String toString() {
        return dateTime.toString();
    }
}
