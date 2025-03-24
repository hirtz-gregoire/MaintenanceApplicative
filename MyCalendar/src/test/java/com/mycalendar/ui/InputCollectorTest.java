package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputCollectorTest {

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
        
        // Create a collector
        Function<String, String> parser = Function.identity();
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("oui");
        InputCollector<String> collector = InputCollector.create(scanner, "Entrez une valeur : ", parser, continueCondition);
        
        // Verify the collector is not null
        assertNotNull(collector);
    }
    
    @Test
    void testCollectSingleItem() {
        // Create input for the scanner: one item followed by "non" to stop
        String input = "item1\nnon\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create a collector
        Function<String, String> parser = Function.identity();
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("oui");
        InputCollector<String> collector = InputCollector.create(scanner, "Entrez une valeur : ", parser, continueCondition);
        
        // Collect items
        List<String> items = collector.collect();
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Entrez une valeur : "));
        assertTrue(output.contains("Ajouter un autre élément ? (oui / non)"));
        
        // Verify the collected items
        assertEquals(1, items.size());
        assertEquals("item1", items.get(0));
    }
    
    @Test
    void testCollectMultipleItems() {
        // Create input for the scanner: three items with "oui" to continue, then "non" to stop
        String input = "item1\noui\nitem2\noui\nitem3\nnon\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create a collector
        Function<String, String> parser = Function.identity();
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("oui");
        InputCollector<String> collector = InputCollector.create(scanner, "Entrez une valeur : ", parser, continueCondition);
        
        // Collect items
        List<String> items = collector.collect();
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Entrez une valeur : "));
        assertTrue(output.contains("Ajouter un autre élément ? (oui / non)"));
        
        // Verify the collected items
        assertEquals(3, items.size());
        assertEquals("item1", items.get(0));
        assertEquals("item2", items.get(1));
        assertEquals("item3", items.get(2));
    }
    
    @Test
    void testCollectWithInitialItems() {
        // Create input for the scanner: one item followed by "non" to stop
        String input = "item3\nnon\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create a collector
        Function<String, String> parser = Function.identity();
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("oui");
        InputCollector<String> collector = InputCollector.create(scanner, "Entrez une valeur : ", parser, continueCondition);
        
        // Collect items with initial items
        List<String> initialItems = Arrays.asList("item1", "item2");
        List<String> items = collector.collect(initialItems);
        
        // Verify the output prompts
        String output = outputStream.toString();
        assertTrue(output.contains("Entrez une valeur : "));
        assertTrue(output.contains("Ajouter un autre élément ? (oui / non)"));
        
        // Verify the collected items
        assertEquals(3, items.size());
        assertEquals("item1", items.get(0));
        assertEquals("item2", items.get(1));
        assertEquals("item3", items.get(2));
    }
    
    @Test
    void testCollectWithParser() {
        // Create input for the scanner: two integer values
        String input = "42\noui\n99\nnon\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create a collector with a parser that converts to integers
        Function<String, Integer> parser = Integer::parseInt;
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("oui");
        InputCollector<Integer> collector = InputCollector.create(scanner, "Entrez un nombre : ", parser, continueCondition);
        
        // Collect items
        List<Integer> items = collector.collect();
        
        // Verify the collected items
        assertEquals(2, items.size());
        assertEquals(42, items.get(0));
        assertEquals(99, items.get(1));
    }
    
    @Test
    void testCollectWithCustomContinueCondition() {
        // Create input for the scanner: two items with "y" to continue, then "n" to stop
        String input = "item1\ny\nitem2\nn\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create a collector with a custom continue condition
        Function<String, String> parser = Function.identity();
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("y");
        InputCollector<String> collector = InputCollector.create(scanner, "Enter a value: ", parser, continueCondition);
        
        // Collect items
        List<String> items = collector.collect();
        
        // Verify the collected items
        assertEquals(2, items.size());
        assertEquals("item1", items.get(0));
        assertEquals("item2", items.get(1));
    }
    
    @Test
    void testCollectWithExceptionInParser() {
        // Create input for the scanner: a valid integer, then an invalid one, then "non" to stop
        String input = "42\noui\nnot-a-number\nnon\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        // Create a collector with a parser that may throw an exception
        Function<String, Integer> parser = Integer::parseInt;
        Predicate<String> continueCondition = s -> s.equalsIgnoreCase("oui");
        InputCollector<Integer> collector = InputCollector.create(scanner, "Entrez un nombre : ", parser, continueCondition);
        
        // Collect items
        List<Integer> items = collector.collect();
        
        // Verify the collected items (only the valid one should be collected)
        assertEquals(1, items.size());
        assertEquals(42, items.get(0));
    }
}
