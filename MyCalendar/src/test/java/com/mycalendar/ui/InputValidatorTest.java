package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.mycalendar.ui.InputValidator.ValidationResult;

class InputValidatorTest {

    @Test
    void testCreateValidator() {
        // Create a validator with an identity transformer
        Function<String, String> transformer = Function.identity();
        InputValidator<String, String> validator = InputValidator.create(transformer);
        
        // Verify the validator is not null
        assertNotNull(validator);
    }
    
    @Test
    void testAddRule() {
        // Create a validator with an identity transformer
        InputValidator<String, String> validator = InputValidator.create(Function.identity());
        
        // Add a rule
        Predicate<String> notEmpty = s -> s != null && !s.isEmpty();
        validator.addRule(notEmpty, "Input cannot be empty");
        
        // Verify the rule was added by testing validation
        ValidationResult<String> result = validator.validate("");
        assertFalse(result.isValid());
        assertEquals("Input cannot be empty", result.getErrorMessage().get());
    }
    
    @Test
    void testValidateWithValidInput() {
        // Create a validator with a transformer that converts to uppercase
        Function<String, String> toUpperCase = String::toUpperCase;
        InputValidator<String, String> validator = InputValidator.create(toUpperCase);
        
        // Add a rule
        Predicate<String> notEmpty = s -> s != null && !s.isEmpty();
        validator.addRule(notEmpty, "Input cannot be empty");
        
        // Validate a valid input
        ValidationResult<String> result = validator.validate("hello");
        
        // Verify the result
        assertTrue(result.isValid());
        assertEquals("HELLO", result.getValue().get());
        assertFalse(result.getErrorMessage().isPresent());
    }
    
    @Test
    void testValidateWithInvalidInput() {
        // Create a validator with a transformer that converts to uppercase
        Function<String, String> toUpperCase = String::toUpperCase;
        InputValidator<String, String> validator = InputValidator.create(toUpperCase);
        
        // Add a rule
        Predicate<String> notEmpty = s -> s != null && !s.isEmpty();
        validator.addRule(notEmpty, "Input cannot be empty");
        
        // Validate an invalid input
        ValidationResult<String> result = validator.validate("");
        
        // Verify the result
        assertFalse(result.isValid());
        assertFalse(result.getValue().isPresent());
        assertEquals("Input cannot be empty", result.getErrorMessage().get());
    }
    
    @Test
    void testValidateWithMultipleRules() {
        // Create a validator with a transformer that parses an integer
        Function<String, Integer> parseInt = Integer::parseInt;
        InputValidator<String, Integer> validator = InputValidator.create(parseInt);
        
        // Add multiple rules
        validator.addRule(s -> s != null && !s.isEmpty(), "Input cannot be empty");
        validator.addRule(s -> {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }, "Input must be a valid integer");
        validator.addRule(s -> Integer.parseInt(s) > 0, "Input must be positive");
        
        // Validate a valid input
        ValidationResult<Integer> result1 = validator.validate("42");
        assertTrue(result1.isValid());
        assertEquals(42, result1.getValue().get());
        
        // Validate an empty input (first rule fails)
        ValidationResult<Integer> result2 = validator.validate("");
        assertFalse(result2.isValid());
        assertEquals("Input cannot be empty", result2.getErrorMessage().get());
        
        // Validate a non-integer input (second rule fails)
        ValidationResult<Integer> result3 = validator.validate("abc");
        assertFalse(result3.isValid());
        assertEquals("Input must be a valid integer", result3.getErrorMessage().get());
        
        // Validate a non-positive integer (third rule fails)
        ValidationResult<Integer> result4 = validator.validate("-5");
        assertFalse(result4.isValid());
        assertEquals("Input must be positive", result4.getErrorMessage().get());
    }
    
    @Test
    void testValidationResultValid() {
        // Create a valid result
        ValidationResult<String> result = ValidationResult.valid("value");
        
        // Verify the result
        assertTrue(result.isValid());
        assertEquals("value", result.getValue().get());
        assertFalse(result.getErrorMessage().isPresent());
    }
    
    @Test
    void testValidationResultInvalid() {
        // Create an invalid result
        ValidationResult<String> result = ValidationResult.invalid("error message");
        
        // Verify the result
        assertFalse(result.isValid());
        assertFalse(result.getValue().isPresent());
        assertEquals("error message", result.getErrorMessage().get());
    }
    
    @Test
    void testValidationResultGetValueWithOptional() {
        // Create a valid result
        ValidationResult<String> validResult = ValidationResult.valid("value");
        
        // Get the value as an Optional
        Optional<String> value = validResult.getValue();
        
        // Verify the value
        assertTrue(value.isPresent());
        assertEquals("value", value.get());
        
        // Create an invalid result
        ValidationResult<String> invalidResult = ValidationResult.invalid("error message");
        
        // Get the value as an Optional
        Optional<String> emptyValue = invalidResult.getValue();
        
        // Verify the value is empty
        assertFalse(emptyValue.isPresent());
    }
    
    @Test
    void testValidationResultGetErrorMessageWithOptional() {
        // Create a valid result
        ValidationResult<String> validResult = ValidationResult.valid("value");
        
        // Get the error message as an Optional
        Optional<String> errorMessage = validResult.getErrorMessage();
        
        // Verify the error message is empty
        assertFalse(errorMessage.isPresent());
        
        // Create an invalid result
        ValidationResult<String> invalidResult = ValidationResult.invalid("error message");
        
        // Get the error message as an Optional
        Optional<String> presentErrorMessage = invalidResult.getErrorMessage();
        
        // Verify the error message
        assertTrue(presentErrorMessage.isPresent());
        assertEquals("error message", presentErrorMessage.get());
    }
}
