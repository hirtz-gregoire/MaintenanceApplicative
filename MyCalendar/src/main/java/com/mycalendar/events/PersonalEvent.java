package com.mycalendar.events;

/**
 * Classe représentant un rendez-vous personnel.
 */
public class PersonalEvent extends AbstractEvent {
    
    public PersonalEvent(EventId id, TitleEvent title, OwnerEvent owner, DateEvent startDate, DurationEvent duration) {
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
