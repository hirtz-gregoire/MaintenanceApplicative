package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

class MenuOptionTest {

    @Test
    void testExecute() {
        // Create a MenuOption that returns a string
        MenuOption<String> option = MenuOption.of(() -> "result");
        
        // Test that execute returns the expected result
        assertEquals("result", option.execute());
    }
    
    @Test
    void testOfWithSupplier() {
        // Create a supplier that returns a string
        Supplier<String> supplier = () -> "supplier result";
        
        // Create a MenuOption from the supplier
        MenuOption<String> option = MenuOption.of(supplier);
        
        // Test that execute returns the expected result
        assertEquals("supplier result", option.execute());
    }
    
    @Test
    void testOfWithLambda() {
        // Create a MenuOption from a lambda
        MenuOption<Integer> option = MenuOption.of(() -> 42);
        
        // Test that execute returns the expected result
        assertEquals(42, option.execute());
    }
    
    @Test
    void testSideEffects() {
        // Create a flag to track if the option was executed
        AtomicBoolean executed = new AtomicBoolean(false);
        
        // Create a MenuOption that sets the flag
        MenuOption<Void> option = MenuOption.of(() -> {
            executed.set(true);
            return null;
        });
        
        // Test that the flag is not set before execution
        assertFalse(executed.get());
        
        // Execute the option
        option.execute();
        
        // Test that the flag is set after execution
        assertTrue(executed.get());
    }
}
