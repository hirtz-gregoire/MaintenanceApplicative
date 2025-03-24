package com.mycalendar.json;

import com.mycalendar.events.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {
    
    @TempDir
    File tempDir;
    
    @Test
    public void testSerializeDeserializePersonalEvent() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PersonalEvent event = new PersonalEvent(
            "Rendez-vous médecin",
            "John",
            new DateEvent(now),
            new DurationEvent(60)
        );
        
        // When
        String json = JsonUtils.toJson(event);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("Rendez-vous médecin"));
        assertTrue(json.contains("John"));
        
        // When deserializing
        PersonalEvent deserializedEvent = JsonUtils.fromJson(json, PersonalEvent.class);
        
        // Then
        assertEquals(event.getTitle().getValue(), deserializedEvent.getTitle().getValue());
        assertEquals(event.getOwner().getValue(), deserializedEvent.getOwner().getValue());
        assertEquals(event.getStartDate().getDateTime(), deserializedEvent.getStartDate().getDateTime());
        assertEquals(event.getDuration().getMinutes(), deserializedEvent.getDuration().getMinutes());
    }
    
    @Test
    public void testSerializeDeserializeMeetingEvent() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        MeetingEvent event = new MeetingEvent(
            "Réunion projet",
            "John",
            new DateEvent(now),
            new DurationEvent(120),
            "Salle A",
            "Alice, Bob"
        );
        
        // When
        String json = JsonUtils.toJson(event);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("Réunion projet"));
        assertTrue(json.contains("Salle A"));
        assertTrue(json.contains("Alice, Bob"));
        
        // When deserializing
        MeetingEvent deserializedEvent = JsonUtils.fromJson(json, MeetingEvent.class);
        
        // Then
        assertEquals(event.getTitle().getValue(), deserializedEvent.getTitle().getValue());
        assertEquals(event.getPlace().getValue(), deserializedEvent.getPlace().getValue());
        assertEquals(event.getParticipants().toString(), deserializedEvent.getParticipants().toString());
    }
    
    @Test
    public void testSerializeDeserializeReminderEvent() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        ReminderEvent event = new ReminderEvent(
            "Rappel important",
            "John",
            new DateEvent(now),
            "Ne pas oublier les documents"
        );
        
        // When
        String json = JsonUtils.toJson(event);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("Rappel important"));
        assertTrue(json.contains("Ne pas oublier les documents"));
        
        // When deserializing
        ReminderEvent deserializedEvent = JsonUtils.fromJson(json, ReminderEvent.class);
        
        // Then
        assertEquals(event.getTitle().getValue(), deserializedEvent.getTitle().getValue());
        assertEquals(event.getMessage().getValue(), deserializedEvent.getMessage().getValue());
    }
    
    @Test
    public void testSerializeDeserializeEventList() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PersonalEvent event1 = new PersonalEvent(
            "Rendez-vous médecin",
            "John",
            new DateEvent(now),
            new DurationEvent(60)
        );
        
        ReminderEvent event2 = new ReminderEvent(
            "Rappel important",
            "John",
            new DateEvent(now.plusHours(2)),
            "Ne pas oublier les documents"
        );
        
        List<Event> events = Arrays.asList(event1, event2);
        
        // When
        String json = JsonUtils.toJson(events);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("Rendez-vous médecin"));
        assertTrue(json.contains("Rappel important"));
    }
    
    @Test
    public void testSaveLoadToFile() throws IOException {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PersonalEvent event = new PersonalEvent(
            "Rendez-vous médecin",
            "John",
            new DateEvent(now),
            new DurationEvent(60)
        );
        
        File file = new File(tempDir, "event.json");
        
        // When
        JsonUtils.saveToFile(event, file);
        
        // Then
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // When loading
        PersonalEvent loadedEvent = JsonUtils.loadFromFile(file, PersonalEvent.class);
        
        // Then
        assertEquals(event.getTitle().getValue(), loadedEvent.getTitle().getValue());
        assertEquals(event.getOwner().getValue(), loadedEvent.getOwner().getValue());
    }
    
    @Test
    public void testSaveLoadListToFile() throws IOException {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PersonalEvent event1 = new PersonalEvent(
            "Rendez-vous médecin",
            "John",
            new DateEvent(now),
            new DurationEvent(60)
        );
        
        ReminderEvent event2 = new ReminderEvent(
            "Rappel important",
            "John",
            new DateEvent(now.plusHours(2)),
            "Ne pas oublier les documents"
        );
        
        List<Event> events = Arrays.asList(event1, event2);
        
        File file = new File(tempDir, "events.json");
        
        // When
        JsonUtils.saveToFile(events, file);
        
        // Then
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
}
