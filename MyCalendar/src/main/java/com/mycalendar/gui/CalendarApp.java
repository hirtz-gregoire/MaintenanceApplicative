package com.mycalendar.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe principale de l'application JavaFX.
 */
public class CalendarApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CalendarView.fxml"));
        Parent root = loader.load();
        
        CalendarController controller = loader.getController();
        
        primaryStage.setTitle("MyCalendar");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    
    /**
     * Point d'entrée principal de l'application.
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}
