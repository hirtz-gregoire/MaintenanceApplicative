package com.mycalendar.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe représentant un rendez-vous personnel.
 */
public class PersonalEvent extends AbstractEvent {
    
    @JsonCreator
    public PersonalEvent(
            @JsonProperty("id") EventId id,
            @JsonProperty("title") TitleEvent title,
            @JsonProperty("owner") OwnerEvent owner,
            @JsonProperty("startDate") DateEvent startDate,
            @JsonProperty("duration") DurationEvent duration) {
        super(id, title, owner, startDate, duration);
    }
    
    public PersonalEvent(String title, String owner, DateEvent startDate, DurationEvent duration) {
        super(title, owner, startDate, duration);
    }
    
    @Override
    public String description() {
        return "RDV : " + getTitle() + " à " + getStartDate();
    }
    
    @Override
    public TypeEvent getType() {
        return TypeEvent.RDV_PERSONNEL;
    }
}
