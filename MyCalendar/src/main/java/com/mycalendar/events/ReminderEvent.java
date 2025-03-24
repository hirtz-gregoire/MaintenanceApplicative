package com.mycalendar.events;

/**
 * Classe représentant un rappel simple.
 */
public class ReminderEvent extends AbstractEvent {
    private final MessageEvent message;
    
    public ReminderEvent(EventId id, TitleEvent title, OwnerEvent owner, DateEvent date, MessageEvent message) {
        super(id, title, owner, date, new DurationEvent(0)); // Les rappels n'ont pas de durée
        this.message = message;
    }
    
    public ReminderEvent(String title, String owner, DateEvent date, String message) {
        super(title, owner, date, new DurationEvent(0));
        this.message = new MessageEvent(message);
    }
    
    public MessageEvent getMessage() {
        return message;
    }
    
    @Override
    public boolean conflictsWith(Event other) {
        // Les rappels ne sont pas considérés comme étant en conflit avec d'autres événements
        return false;
    }
    
    @Override
    public String description() {
        return "Rappel : " + getTitle() + " - " + message + " (" + getStartDate() + ")";
    }
    
    @Override
    public TypeEvent getType() {
        return TypeEvent.RAPPEL;
    }
}
