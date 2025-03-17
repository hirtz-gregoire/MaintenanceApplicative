package com.mycalendar.events;

/**
 * Classe abstraite implémentant les fonctionnalités communes à tous les types d'événements.
 */
public abstract class AbstractEvent implements Event {
    private final EventId id;
    private final TitleEvent title;
    private final OwnerEvent owner;
    private final DateEvent startDate;
    private final DurationEvent duration;
    
    protected AbstractEvent(EventId id, TitleEvent title, OwnerEvent owner, DateEvent startDate, DurationEvent duration) {
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.startDate = startDate;
        this.duration = duration;
    }
    
    protected AbstractEvent(String title, String owner, DateEvent startDate, DurationEvent duration) {
        this(new EventId(), new TitleEvent(title), new OwnerEvent(owner), startDate, duration);
    }
    
    @Override
    public EventId getId() {
        return id;
    }
    
    @Override
    public TitleEvent getTitle() {
        return title;
    }
    
    @Override
    public OwnerEvent getOwner() {
        return owner;
    }
    
    @Override
    public DateEvent getStartDate() {
        return startDate;
    }
    
    @Override
    public DurationEvent getDuration() {
        return duration;
    }
    
    @Override
    public DateEvent getEndDate() {
        return startDate.plusMinutes(duration);
    }
    
    @Override
    public boolean conflictsWith(Event other) {
        // Deux événements sont en conflit si leurs périodes se chevauchent
        // Mais ils ne sont pas en conflit si la fin de l'un est égale au début de l'autre
        return getStartDate().isBefore(other.getEndDate()) && getEndDate().isAfter(other.getStartDate());
    }
    
    @Override
    public boolean occursInPeriod(DateEvent start, DateEvent end) {
        // Par défaut, un événement se produit dans une période si sa date de début est dans cette période
        return !getStartDate().isBefore(start) && !getStartDate().isAfter(end);
    }
}
