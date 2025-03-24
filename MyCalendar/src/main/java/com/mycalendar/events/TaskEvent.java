package com.mycalendar.events;

/**
 * Classe représentant une tâche avec une date limite.
 */
public class TaskEvent extends AbstractEvent {
    private final PriorityEvent priority;
    
    public TaskEvent(EventId id, TitleEvent title, OwnerEvent owner, DateEvent deadline, PriorityEvent priority) {
        super(id, title, owner, deadline, new DurationEvent(0)); // Les tâches n'ont pas de durée fixe
        this.priority = priority;
    }
    
    public TaskEvent(String title, String owner, DateEvent deadline, String priority) {
        super(title, owner, deadline, new DurationEvent(0));
        this.priority = new PriorityEvent(priority);
    }
    
    public PriorityEvent getPriority() {
        return priority;
    }
    
    @Override
    public boolean conflictsWith(Event other) {
        // Les tâches ne sont pas considérées comme étant en conflit avec d'autres événements
        return false;
    }
    
    @Override
    public String description() {
        return "Tâche : " + getTitle() + " (Priorité: " + priority + ") - Deadline: " + getStartDate();
    }
    
    @Override
    public TypeEvent getType() {
        return TypeEvent.TASK;
    }
}
