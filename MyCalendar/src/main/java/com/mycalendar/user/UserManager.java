package com.mycalendar.user;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users;
    
    public UserManager() {
        this.users = new HashMap<>();
        addDefaultUsers();
    }
    
    private void addDefaultUsers() {
        registerUser("Roger", "Chat");
        registerUser("Pierre", "KiRouhl");
    }
    

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        
        User user = new User(username, password);
        users.put(username, user);
        return true;
    }

    public User authenticateUser(String username, String password) {
        User user = users.get(username);
        
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        
        return null;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    public int getUserCount() {
        return users.size();
    }
}
