package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTimeInputHelperTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        // Save the original System.out
        originalOut = System.out;
        
        // Redirect System.out to capture output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        // Restore the original System.out
        System.setOut(originalOut);
    }
    
    @Test
    void testInputDateTime() {
        // Create input for the scanner
        String input = "2023\n3\n15\n14\n30\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create the helper
        DateTimeInputHelper helper = new DateTimeInputHelper(scanner);
        
        // Call the method
        LocalDateTime result = helper.inputDateTime();
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Année (AAAA) : "));
        assertTrue(output.contains("Mois (1-12) : "));
        assertTrue(output.contains("Jour (1-31) : "));
        assertTrue(output.contains("Heure début (0-23) : "));
        assertTrue(output.contains("Minute début (0-59) : "));
        
        // Verify the result
        assertEquals(2023, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }
    
    @Test
    void testInputDuration() {
        // Create input for the scanner
        String input = "120\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create the helper
        DateTimeInputHelper helper = new DateTimeInputHelper(scanner);
        
        // Call the method
        int result = helper.inputDuration();
        
        // Verify the output prompt
        String output = outputStream.toString();
        assertTrue(output.contains("Durée (en minutes) : "));
        
        // Verify the result
        assertEquals(120, result);
    }
    
    @Test
    void testInputFrequency() {
        // Create input for the scanner
        String input = "7\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create the helper
        DateTimeInputHelper helper = new DateTimeInputHelper(scanner);
        
        // Call the method
        int result = helper.inputFrequency();
        
        // Verify the output prompt
        String output = outputStream.toString();
        assertTrue(output.contains("Fréquence (en jours) : "));
        
        // Verify the result
        assertEquals(7, result);
    }
    
    @Test
    void testInputMonthPeriod() {
        // Create input for the scanner
        String input = "2023\n4\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create the helper
        DateTimeInputHelper helper = new DateTimeInputHelper(scanner);
        
        // Call the method
        LocalDateTime[] result = helper.inputMonthPeriod();
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Entrez l'année (AAAA) : "));
        assertTrue(output.contains("Entrez le mois (1-12) : "));
        
        // Verify the result
        assertEquals(2023, result[0].getYear());
        assertEquals(4, result[0].getMonthValue());
        assertEquals(1, result[0].getDayOfMonth());
        assertEquals(0, result[0].getHour());
        assertEquals(0, result[0].getMinute());
        
        assertEquals(2023, result[1].getYear());
        assertEquals(4, result[1].getMonthValue());
        assertEquals(30, result[1].getDayOfMonth());
        assertEquals(23, result[1].getHour());
        assertEquals(59, result[1].getMinute());
        assertEquals(59, result[1].getSecond());
        // Don't test the nanos as it might vary
    }
    
    @Test
    void testInputWeekPeriod() {
        // Create input for the scanner
        String input = "2023\n15\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create the helper
        DateTimeInputHelper helper = new DateTimeInputHelper(scanner);
        
        // Call the method
        LocalDateTime[] result = helper.inputWeekPeriod();
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Entrez l'année (AAAA) : "));
        assertTrue(output.contains("Entrez le numéro de semaine (1-52) : "));
        
        // Verify the result
        assertEquals(2023, result[0].getYear());
        assertEquals(0, result[0].getHour());
        assertEquals(0, result[0].getMinute());
        
        // The end date should be 7 days after the start date
        assertEquals(result[0].plusDays(7).minusSeconds(1), result[1]);
    }
    
    @Test
    void testInputDayPeriod() {
        // Create input for the scanner
        String input = "2023\n5\n20\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create the helper
        DateTimeInputHelper helper = new DateTimeInputHelper(scanner);
        
        // Call the method
        LocalDateTime[] result = helper.inputDayPeriod();
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Entrez l'année (AAAA) : "));
        assertTrue(output.contains("Entrez le mois (1-12) : "));
        assertTrue(output.contains("Entrez le jour (1-31) : "));
        
        // Verify the result
        assertEquals(2023, result[0].getYear());
        assertEquals(5, result[0].getMonthValue());
        assertEquals(20, result[0].getDayOfMonth());
        assertEquals(0, result[0].getHour());
        assertEquals(0, result[0].getMinute());
        
        // The end date should be 1 day after the start date
        assertEquals(result[0].plusDays(1).minusSeconds(1), result[1]);
    }
}
