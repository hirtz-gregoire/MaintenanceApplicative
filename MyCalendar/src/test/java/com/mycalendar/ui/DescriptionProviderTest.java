package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

class DescriptionProviderTest {

    @Test
    void testCreateWithDefaultProvider() {
        // Create a description provider with a default provider
        Function<String, String> defaultProvider = s -> "default-" + s;
        DescriptionProvider<String> provider = DescriptionProvider.create(defaultProvider);
        
        // Test that the default provider is used for unknown options
        assertEquals("default-test", provider.getDescription("test"));
    }
    
    @Test
    void testAddDescription() {
        // Create a description provider with a default provider
        Function<String, String> defaultProvider = s -> "default-" + s;
        DescriptionProvider<String> provider = DescriptionProvider.create(defaultProvider);
        
        // Add a description
        provider.addDescription("option1", "description1");
        
        // Test that the added description is returned
        assertEquals("description1", provider.getDescription("option1"));
    }
    
    @Test
    void testAddMultipleDescriptions() {
        // Create a description provider with a default provider
        Function<String, String> defaultProvider = s -> "default-" + s;
        DescriptionProvider<String> provider = DescriptionProvider.create(defaultProvider);
        
        // Add multiple descriptions using method chaining
        provider
            .addDescription("option1", "description1")
            .addDescription("option2", "description2")
            .addDescription("option3", "description3");
        
        // Test that all descriptions are returned correctly
        assertEquals("description1", provider.getDescription("option1"));
        assertEquals("description2", provider.getDescription("option2"));
        assertEquals("description3", provider.getDescription("option3"));
        
        // Test that the default provider is used for unknown options
        assertEquals("default-unknown", provider.getDescription("unknown"));
    }
    
    @Test
    void testWithEnumValues() {
        // Create a description provider for an enum
        DescriptionProvider<TestEnum> provider = DescriptionProvider.<TestEnum>create(e -> e.name().toLowerCase())
            .addDescription(TestEnum.VALUE1, "First Value")
            .addDescription(TestEnum.VALUE2, "Second Value");
        
        // Test that the descriptions are returned correctly
        assertEquals("First Value", provider.getDescription(TestEnum.VALUE1));
        assertEquals("Second Value", provider.getDescription(TestEnum.VALUE2));
        
        // Test that the default provider is used for values without a specific description
        assertEquals("value3", provider.getDescription(TestEnum.VALUE3));
    }
    
    // Test enum for the testWithEnumValues test
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}
