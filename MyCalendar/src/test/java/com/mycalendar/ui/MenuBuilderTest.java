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

class MenuBuilderTest {

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
    void testCreate() {
        // Create a scanner
        Scanner scanner = new Scanner(new ByteArrayInputStream("".getBytes()));
        
        // Create a menu builder
        MenuBuilder<String> builder = MenuBuilder.create(scanner, "Test Menu");
        
        // Verify the builder is not null
        assertNotNull(builder);
    }
    
    @Test
    void testAddOption() {
        // Create a scanner
        Scanner scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        
        // Create a menu builder
        MenuBuilder<String> builder = MenuBuilder.create(scanner, "Test Menu");
        
        // Add an option
        AtomicBoolean optionExecuted = new AtomicBoolean(false);
        builder.addOption("1", "Option 1", MenuOption.of(() -> {
            optionExecuted.set(true);
            return "option1";
        }));
        
        // Build the menu
        Menu<String> menu = builder.build();
        
        // Display the menu
        String result = menu.display();
        
        // Verify the menu was displayed correctly
        String output = outputStream.toString();
        assertTrue(output.contains("=== Test Menu ==="));
        assertTrue(output.contains("1 - 1"));
        
        // Verify the option was executed
        assertTrue(optionExecuted.get());
        
        // Verify the result is correct
        assertEquals("option1", result);
    }
    
    @Test
    void testAddMultipleOptions() {
        // Create a scanner with input "2"
        Scanner scanner = new Scanner(new ByteArrayInputStream("2\n".getBytes()));
        
        // Create a menu builder
        MenuBuilder<String> builder = MenuBuilder.create(scanner, "Test Menu");
        
        // Add multiple options
        AtomicBoolean option1Executed = new AtomicBoolean(false);
        AtomicBoolean option2Executed = new AtomicBoolean(false);
        
        builder.addOption("1", "Option 1", MenuOption.of(() -> {
            option1Executed.set(true);
            return "option1";
        }));
        
        builder.addOption("2", "Option 2", MenuOption.of(() -> {
            option2Executed.set(true);
            return "option2";
        }));
        
        // Build the menu
        Menu<String> menu = builder.build();
        
        // Display the menu
        String result = menu.display();
        
        // Verify the menu was displayed correctly
        String output = outputStream.toString();
        assertTrue(output.contains("=== Test Menu ==="));
        assertTrue(output.contains("1 - 1"));
        assertTrue(output.contains("2 - 2"));
        
        // Verify only option 2 was executed
        assertFalse(option1Executed.get());
        assertTrue(option2Executed.get());
        
        // Verify the result is correct
        assertEquals("option2", result);
    }
    
    @Test
    void testSetDefaultAction() {
        // Create a scanner with input "3" (not a valid option)
        Scanner scanner = new Scanner(new ByteArrayInputStream("3\n".getBytes()));
        
        // Create a menu builder
        MenuBuilder<String> builder = MenuBuilder.create(scanner, "Test Menu");
        
        // Add two options
        builder.addOption("1", "Option 1", MenuOption.of(() -> "option1"));
        builder.addOption("2", "Option 2", MenuOption.of(() -> "option2"));
        
        // Set a default action
        builder.setDefaultAction(choice -> "default-" + choice);
        
        // Build the menu
        Menu<String> menu = builder.build();
        
        // Display the menu
        String result = menu.display();
        
        // Verify the default action was executed
        assertEquals("default-3", result);
    }
    
    @Test
    void testMethodChaining() {
        // Create a scanner with input "1"
        Scanner scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        
        // Create a menu using method chaining
        Menu<String> menu = MenuBuilder.<String>create(scanner, "Test Menu")
            .addOption("1", "Option 1", MenuOption.of(() -> "option1"))
            .addOption("2", "Option 2", MenuOption.of(() -> "option2"))
            .setDefaultAction(choice -> "default-" + choice)
            .build();
        
        // Display the menu
        String result = menu.display();
        
        // Verify the result is correct
        assertEquals("option1", result);
    }
}
