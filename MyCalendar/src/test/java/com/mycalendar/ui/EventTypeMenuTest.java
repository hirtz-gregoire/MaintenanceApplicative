package com.mycalendar.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycalendar.events.TypeEvent;
import com.mycalendar.user.User;

class EventTypeMenuTest {

    private Scanner scanner;
    private EventTypeMenu eventTypeMenu;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private User testUser;
    private TestEventInputHelper testEventInputHelper;
    
    @BeforeEach
    void setUp() {
        // Save the original System.out
        originalOut = System.out;
        
        // Redirect System.out to capture output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        // Create a scanner with predefined input
        scanner = new Scanner(new ByteArrayInputStream("5\n".getBytes()));
        
        // Create a test event input helper
        testEventInputHelper = new TestEventInputHelper();
        
        // Create the menu
        eventTypeMenu = new EventTypeMenu(scanner, testEventInputHelper);
        
        // Create a test user
        testUser = new User("testuser", "password");
    }
    
    @AfterEach
    void tearDown() {
        // Restore the original System.out
        System.setOut(originalOut);
    }
    
    @Test
    void testGetDescription() {
        assertEquals("rendez-vous personnel", eventTypeMenu.getDescription(TypeEvent.RDV_PERSONNEL));
        assertEquals("réunion", eventTypeMenu.getDescription(TypeEvent.REUNION));
        assertEquals("événement périodique", eventTypeMenu.getDescription(TypeEvent.PERIODIQUE));
        assertEquals("tâche", eventTypeMenu.getDescription(TypeEvent.TASK));
    }
    
    @Test
    void testDisplayMenu() {
        // Test with input "5" (return option)
        boolean result = eventTypeMenu.displayMenu(testUser);
        
        // Print the output for debugging
        System.err.println("Actual output: " + outputStream.toString());
        
        // Verify the result is false (return option)
        assertFalse(result);
    }
    
    @Test
    void testExecuteActionWithTaskEvent() {
        // Reset the test helper
        testEventInputHelper.reset();
        
        // Execute the action
        eventTypeMenu.executeAction(TypeEvent.TASK, testUser);
        
        // Verify the correct method was called
        assertTrue(testEventInputHelper.isTaskEventCalled());
        assertEquals(testUser, testEventInputHelper.getLastUser());
    }
    
    @Test
    void testExecuteActionWithPersonalEvent() {
        // Reset the test helper
        testEventInputHelper.reset();
        
        // Execute the action
        eventTypeMenu.executeAction(TypeEvent.RDV_PERSONNEL, testUser);
        
        // Verify the correct method was called
        assertTrue(testEventInputHelper.isPersonalEventCalled());
        assertEquals(testUser, testEventInputHelper.getLastUser());
    }
    
    @Test
    void testExecuteActionWithMeetingEvent() {
        // Reset the test helper
        testEventInputHelper.reset();
        
        // Execute the action
        eventTypeMenu.executeAction(TypeEvent.REUNION, testUser);
        
        // Verify the correct method was called
        assertTrue(testEventInputHelper.isMeetingEventCalled());
        assertEquals(testUser, testEventInputHelper.getLastUser());
    }
    
    @Test
    void testExecuteActionWithPeriodicEvent() {
        // Reset the test helper
        testEventInputHelper.reset();
        
        // Execute the action
        eventTypeMenu.executeAction(TypeEvent.PERIODIQUE, testUser);
        
        // Verify the correct method was called
        assertTrue(testEventInputHelper.isPeriodicEventCalled());
        assertEquals(testUser, testEventInputHelper.getLastUser());
    }
    
    /**
     * Test implementation of EventInputHelper that tracks method calls.
     */
    private static class TestEventInputHelper extends EventInputHelper {
        private boolean taskEventCalled;
        private boolean personalEventCalled;
        private boolean meetingEventCalled;
        private boolean periodicEventCalled;
        private User lastUser;
        
        public TestEventInputHelper() {
            super(null, null); // Pass null for scanner and calendarManager
        }
        
        public void reset() {
            taskEventCalled = false;
            personalEventCalled = false;
            meetingEventCalled = false;
            periodicEventCalled = false;
            lastUser = null;
        }
        
        @Override
        public void inputTaskEvent(User user) {
            taskEventCalled = true;
            lastUser = user;
        }
        
        @Override
        public void inputPersonalEvent(User user) {
            personalEventCalled = true;
            lastUser = user;
        }
        
        @Override
        public void inputMeetingEvent(User user) {
            meetingEventCalled = true;
            lastUser = user;
        }
        
        @Override
        public void inputPeriodicEvent(User user) {
            periodicEventCalled = true;
            lastUser = user;
        }
        
        public boolean isTaskEventCalled() {
            return taskEventCalled;
        }
        
        public boolean isPersonalEventCalled() {
            return personalEventCalled;
        }
        
        public boolean isMeetingEventCalled() {
            return meetingEventCalled;
        }
        
        public boolean isPeriodicEventCalled() {
            return periodicEventCalled;
        }
        
        public User getLastUser() {
            return lastUser;
        }
    }
}
