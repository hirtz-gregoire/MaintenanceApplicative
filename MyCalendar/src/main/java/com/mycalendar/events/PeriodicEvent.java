package com.mycalendar.events;

/**
 * Classe représentant un événement périodique.
 */
public class PeriodicEvent extends AbstractEvent {
    private final FrequencyEvent frequency;
    
    public PeriodicEvent(EventId id, TitleEvent title, OwnerEvent owner, DateEvent startDate, FrequencyEvent frequency) {
        super(id, title, owner, startDate, new DurationEvent(0)); // Les événements périodiques n'ont pas de durée fixe
        this.frequency = frequency;
    }
    
    public PeriodicEvent(String title, String owner, DateEvent startDate, int frequencyDays) {
        super(title, owner, startDate, new DurationEvent(0));
        this.frequency = new FrequencyEvent(frequencyDays);
    }
    
    public FrequencyEvent getFrequency() {
        return frequency;
    }
    
    @Override
    public boolean conflictsWith(Event other) {
        // Les événements périodiques ne sont pas considérés comme étant en conflit avec d'autres événements
        return false;
    }
    
    @Override
    public boolean occursInPeriod(DateEvent start, DateEvent end) {
        DateEvent currentOccurrence = getStartDate();
        
        // Vérifie si une occurrence de l'événement périodique se produit dans la période spécifiée
        while (!currentOccurrence.isAfter(end)) {
            if (!currentOccurrence.isBefore(start)) {
                return true;
            }
            currentOccurrence = currentOccurrence.plusDays(frequency.getDays());
        }
        
        return false;
    }
    
    @Override
    public String description() {
        return "Événement périodique : " + getTitle() + " " + frequency;
    }
    
    @Override
    public TypeEvent getType() {
        return TypeEvent.PERIODIQUE;
    }
}
