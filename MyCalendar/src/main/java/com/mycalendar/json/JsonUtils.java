package com.mycalendar.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mycalendar.events.Event;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Classe utilitaire pour la sérialisation/désérialisation JSON.
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = createObjectMapper();
    
    /**
     * Crée et configure un ObjectMapper.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Ignorer les propriétés inconnues lors de la désérialisation
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return mapper;
    }
    
    /**
     * Sérialise un événement en JSON.
     */
    public static String toJson(Event event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }
    
    /**
     * Sérialise une liste d'événements en JSON.
     */
    public static String toJson(List<Event> events) throws JsonProcessingException {
        return objectMapper.writeValueAsString(events);
    }
    
    /**
     * Désérialise un événement à partir d'une chaîne JSON.
     */
    public static <T extends Event> T fromJson(String json, Class<T> eventClass) throws JsonProcessingException {
        return objectMapper.readValue(json, eventClass);
    }
    
    /**
     * Désérialise une liste d'événements à partir d'une chaîne JSON.
     */
    public static <T extends Event> List<T> fromJsonList(String json, Class<T> eventClass) throws JsonProcessingException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, eventClass));
    }
    
    /**
     * Sauvegarde un événement dans un fichier JSON.
     */
    public static void saveToFile(Event event, File file) throws IOException {
        objectMapper.writeValue(file, event);
    }
    
    /**
     * Sauvegarde une liste d'événements dans un fichier JSON.
     */
    public static void saveToFile(List<Event> events, File file) throws IOException {
        objectMapper.writeValue(file, events);
    }
    
    /**
     * Charge un événement depuis un fichier JSON.
     */
    public static <T extends Event> T loadFromFile(File file, Class<T> eventClass) throws IOException {
        return objectMapper.readValue(file, eventClass);
    }
    
    /**
     * Charge une liste d'événements depuis un fichier JSON.
     */
    public static <T extends Event> List<T> loadListFromFile(File file, Class<T> eventClass) throws IOException {
        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, eventClass));
        } catch (Exception e) {
            System.out.println("Erreur lors de la désérialisation JSON : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
