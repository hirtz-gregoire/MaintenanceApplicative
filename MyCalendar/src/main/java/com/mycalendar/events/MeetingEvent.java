package com.mycalendar.events;

/**
 * Classe représentant une réunion.
 */
public class MeetingEvent extends AbstractEvent {
    private final PlaceEvent place;
    private final ParticipantEvent participants;
    
    public MeetingEvent(EventId id, TitleEvent title, OwnerEvent owner, DateEvent startDate, DurationEvent duration,
                        PlaceEvent place, ParticipantEvent participants) {
        super(id, title, owner, startDate, duration);
        this.place = place;
        this.participants = participants;
    }
    
    public MeetingEvent(String title, String owner, DateEvent startDate, DurationEvent duration,
                        String place, String participants) {
        super(title, owner, startDate, duration);
        this.place = new PlaceEvent(place);
        this.participants = new ParticipantEvent(participants);
    }
    
    public PlaceEvent getPlace() {
        return place;
    }
    
    public ParticipantEvent getParticipants() {
        return participants;
    }
    
    @Override
    public String description() {
        return "Réunion : " + getTitle() + " à " + place + " avec " + participants;
    }
    
    @Override
    public TypeEvent getType() {
        return TypeEvent.REUNION;
    }
}
