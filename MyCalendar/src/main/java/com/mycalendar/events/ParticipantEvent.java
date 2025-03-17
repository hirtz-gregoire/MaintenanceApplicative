package com.mycalendar.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object représentant les participants à un événement.
 * Cette classe est immuable.
 */
public class ParticipantEvent {
    private final List<String> participants;
    
    public ParticipantEvent(String participantsString) {
        List<String> list = new ArrayList<>();
        
        if (participantsString != null && !participantsString.trim().isEmpty()) {
            String[] parts = participantsString.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    list.add(trimmed);
                }
            }
        }
        
        this.participants = Collections.unmodifiableList(list);
    }
    
    public ParticipantEvent(List<String> participants) {
        List<String> list = new ArrayList<>();
        
        if (participants != null) {
            for (String participant : participants) {
                if (participant != null && !participant.trim().isEmpty()) {
                    list.add(participant.trim());
                }
            }
        }
        
        this.participants = Collections.unmodifiableList(list);
    }
    
    public ParticipantEvent(String... participants) {
        this(Arrays.asList(participants));
    }
    
    public List<String> getParticipants() {
        return participants;
    }
    
    public boolean isEmpty() {
        return participants.isEmpty();
    }
    
    public int count() {
        return participants.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantEvent that = (ParticipantEvent) o;
        return Objects.equals(participants, that.participants);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(participants);
    }
    
    @Override
    public String toString() {
        if (participants.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < participants.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(participants.get(i));
        }
        return sb.toString();
    }
}
