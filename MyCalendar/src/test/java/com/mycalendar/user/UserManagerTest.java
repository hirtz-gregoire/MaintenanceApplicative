package com.mycalendar.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserManagerTest {
    
    private UserManager userManager;
    
    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }
    
    @Test
    void testConstructorInitializesDefaultUsers() {
        // Vérifie que les utilisateurs par défaut sont créés
        assertTrue(userManager.userExists("Roger"));
        assertTrue(userManager.userExists("Pierre"));
        
        // Vérifie que l'authentification fonctionne pour les utilisateurs par défaut
        assertNotNull(userManager.authenticateUser("Roger", "Chat"));
        assertNotNull(userManager.authenticateUser("Pierre", "KiRouhl"));
        
        // Vérifie que l'authentification échoue avec un mot de passe incorrect
        assertNull(userManager.authenticateUser("Roger", "WrongPassword"));
        assertNull(userManager.authenticateUser("Pierre", "WrongPassword"));
    }
    
    @Test
    void testRegisterUser() {
        // Enregistre un nouvel utilisateur
        boolean result = userManager.registerUser("john.doe", "password123");
        
        // Vérifie que l'enregistrement a réussi
        assertTrue(result);
        assertTrue(userManager.userExists("john.doe"));
        assertNotNull(userManager.authenticateUser("john.doe", "password123"));
    }
    
    @Test
    void testRegisterUserWithExistingUsername() {
        // Enregistre un utilisateur
        userManager.registerUser("john.doe", "password123");
        
        // Tente d'enregistrer un utilisateur avec le même nom d'utilisateur
        boolean result = userManager.registerUser("john.doe", "differentpassword");
        
        // Vérifie que l'enregistrement a échoué
        assertFalse(result);
        
        // Vérifie que le mot de passe n'a pas été modifié
        assertNotNull(userManager.authenticateUser("john.doe", "password123"));
        assertNull(userManager.authenticateUser("john.doe", "differentpassword"));
    }
    
    @Test
    void testAuthenticateUserWithNonExistentUsername() {
        // Tente d'authentifier un utilisateur qui n'existe pas
        User user = userManager.authenticateUser("nonexistent", "password123");
        
        // Vérifie que l'authentification a échoué
        assertNull(user);
    }
    
    @Test
    void testAuthenticateUserWithIncorrectPassword() {
        // Enregistre un utilisateur
        userManager.registerUser("john.doe", "password123");
        
        // Tente d'authentifier l'utilisateur avec un mot de passe incorrect
        User user = userManager.authenticateUser("john.doe", "wrongpassword");
        
        // Vérifie que l'authentification a échoué
        assertNull(user);
    }
    
    @Test
    void testUserExists() {
        // Vérifie qu'un utilisateur qui n'existe pas retourne false
        assertFalse(userManager.userExists("nonexistent"));
        
        // Enregistre un utilisateur
        userManager.registerUser("john.doe", "password123");
        
        // Vérifie que l'utilisateur existe maintenant
        assertTrue(userManager.userExists("john.doe"));
    }
    
    @Test
    void testGetUserCount() {
        // Vérifie que le nombre d'utilisateurs initial est 2 (Roger et Pierre)
        assertEquals(2, userManager.getUserCount());
        
        // Enregistre un nouvel utilisateur
        userManager.registerUser("john.doe", "password123");
        
        // Vérifie que le nombre d'utilisateurs a augmenté
        assertEquals(3, userManager.getUserCount());
    }
}
