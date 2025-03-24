package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuTest {

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
    void testDisplayWithValidOption() {
        // Create a scanner with input "1"
        Scanner scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        
        // Create a menu with a single option
        Menu<String> menu = new Menu<>(scanner, "Test Menu", choice -> "default-" + choice);
        
        // Add an option that returns "option1"
        AtomicBoolean option1Executed = new AtomicBoolean(false);
        menu.addOption("1", "Option 1", MenuOption.of(() -> {
            option1Executed.set(true);
            return "option1";
        }));
        
        // Display the menu and get the result
        String result = menu.display();
        
        // Verify the menu was displayed correctly
        String output = outputStream.toString();
        assertTrue(output.contains("=== Test Menu ==="));
        assertTrue(output.contains("1 - 1"));
        assertTrue(output.contains("Votre choix : "));
        
        // Verify the option was executed
        assertTrue(option1Executed.get());
        
        // Verify the result is correct
        assertEquals("option1", result);
    }
    
    @Test
    void testDisplayWithInvalidOption() {
        // Create a scanner with input "3" (not a valid option)
        Scanner scanner = new Scanner(new ByteArrayInputStream("3\n".getBytes()));
        
        // Create a menu with a default action that returns "default-" + the choice
        Menu<String> menu = new Menu<>(scanner, "Test Menu", choice -> "default-" + choice);
        
        // Add two options
        menu.addOption("1", "Option 1", MenuOption.of(() -> "option1"));
        menu.addOption("2", "Option 2", MenuOption.of(() -> "option2"));
        
        // Display the menu and get the result
        String result = menu.display();
        
        // Verify the menu was displayed correctly
        String output = outputStream.toString();
        assertTrue(output.contains("=== Test Menu ==="));
        assertTrue(output.contains("1 - 1"));
        assertTrue(output.contains("2 - 2"));
        assertTrue(output.contains("Votre choix : "));
        
        // Verify the default action was executed
        assertEquals("default-3", result);
    }
    
    @Test
    void testMultipleOptions() {
        // Create a scanner with input "2"
        Scanner scanner = new Scanner(new ByteArrayInputStream("2\n".getBytes()));
        
        // Create a menu with multiple options
        Menu<String> menu = new Menu<>(scanner, "Test Menu", choice -> "default-" + choice);
        
        // Add multiple options
        AtomicBoolean option1Executed = new AtomicBoolean(false);
        AtomicBoolean option2Executed = new AtomicBoolean(false);
        AtomicBoolean option3Executed = new AtomicBoolean(false);
        
        menu.addOption("1", "Option 1", MenuOption.of(() -> {
            option1Executed.set(true);
            return "option1";
        }));
        
        menu.addOption("2", "Option 2", MenuOption.of(() -> {
            option2Executed.set(true);
            return "option2";
        }));
        
        menu.addOption("3", "Option 3", MenuOption.of(() -> {
            option3Executed.set(true);
            return "option3";
        }));
        
        // Display the menu and get the result
        String result = menu.display();
        
        // Verify the menu was displayed correctly
        String output = outputStream.toString();
        assertTrue(output.contains("=== Test Menu ==="));
        assertTrue(output.contains("1 - 1"));
        assertTrue(output.contains("2 - 2"));
        assertTrue(output.contains("3 - 3"));
        assertTrue(output.contains("Votre choix : "));
        
        // Verify only option 2 was executed
        assertFalse(option1Executed.get());
        assertTrue(option2Executed.get());
        assertFalse(option3Executed.get());
        
        // Verify the result is correct
        assertEquals("option2", result);
    }
}
