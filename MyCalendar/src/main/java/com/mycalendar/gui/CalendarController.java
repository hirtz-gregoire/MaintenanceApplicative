package com.mycalendar.gui;

import com.mycalendar.CalendarManager;
import com.mycalendar.events.*;
import com.mycalendar.json.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour l'interface utilisateur du calendrier.
 */
public class CalendarController {
    
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, String> typeColumn;
    @FXML private TableColumn<Event, String> dateColumn;
    @FXML private TableColumn<Event, String> ownerColumn;
    
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> eventTypeComboBox;
    @FXML private TextField titleField;
    @FXML private TextField ownerField;
    @FXML private TextField durationField;
    @FXML private TextField placeField;
    @FXML private TextField participantsField;
    @FXML private TextField messageField;
    @FXML private TextField frequencyField;
    
    @FXML private Label placeLabel;
    @FXML private Label participantsLabel;
    @FXML private Label messageLabel;
    @FXML private Label frequencyLabel;
    @FXML private Label durationLabel;
    
    private CalendarManager calendarManager;
    private ObservableList<Event> eventList;
    
    /**
     * Initialise le contrôleur.
     */
    public void initialize() {
        calendarManager = new CalendarManager();
        eventList = FXCollections.observableArrayList();
        
        // Configuration de la table
        titleColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(event.getTitle().getValue());
        });
        
        typeColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(event.getType().toString());
        });
        
        dateColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(event.getStartDate().toString());
        });
        
        ownerColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(event.getOwner().getValue());
        });
        
        eventTable.setItems(eventList);
        
        // Configuration du ComboBox des types d'événements
        eventTypeComboBox.getItems().addAll(
            "RDV_PERSONNEL",
            "REUNION",
            "PERIODIQUE",
            "TASK",
            "RAPPEL"
        );
        eventTypeComboBox.getSelectionModel().selectFirst();
        
        // Configuration du DatePicker
        datePicker.setValue(LocalDate.now());
        
        // Ajout d'un listener pour le changement de type d'événement
        eventTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateFieldsVisibility(newValue);
        });
        
        // Initialisation de la visibilité des champs
        updateFieldsVisibility(eventTypeComboBox.getSelectionModel().getSelectedItem());
    }
    
    /**
     * Met à jour la visibilité des champs en fonction du type d'événement sélectionné.
     * @param eventType Le type d'événement sélectionné
     */
    private void updateFieldsVisibility(String eventType) {
        // Réinitialisation de tous les champs
        placeField.setVisible(false);
        placeLabel.setVisible(false);
        participantsField.setVisible(false);
        participantsLabel.setVisible(false);
        messageField.setVisible(false);
        messageLabel.setVisible(false);
        frequencyField.setVisible(false);
        frequencyLabel.setVisible(false);
        durationField.setVisible(true);
        durationLabel.setVisible(true);
        
        // Configuration en fonction du type d'événement
        switch (eventType) {
            case "RDV_PERSONNEL":
                // Rien à faire, les champs par défaut sont corrects
                break;
            case "REUNION":
                placeField.setVisible(true);
                placeLabel.setVisible(true);
                participantsField.setVisible(true);
                participantsLabel.setVisible(true);
                break;
            case "PERIODIQUE":
                frequencyField.setVisible(true);
                frequencyLabel.setVisible(true);
                durationField.setVisible(false);
                durationLabel.setVisible(false);
                break;
            case "TASK":
                placeField.setVisible(true);
                placeLabel.setText("Priorité:");
                placeLabel.setVisible(true);
                durationField.setVisible(false);
                durationLabel.setVisible(false);
                break;
            case "RAPPEL":
                messageField.setVisible(true);
                messageLabel.setVisible(true);
                durationField.setVisible(false);
                durationLabel.setVisible(false);
                break;
        }
    }
    
    /**
     * Gère l'ajout d'un événement.
     * @param event L'événement déclencheur
     */
    @FXML
    private void handleAddEvent(ActionEvent event) {
        String type = eventTypeComboBox.getValue();
        String title = titleField.getText();
        String owner = ownerField.getText();
        LocalDate date = datePicker.getValue();
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(8, 0)); // Par défaut à 8h
        
        if (title.isEmpty() || owner.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }
        
        try {
            int duration = 0;
            if (durationField.isVisible()) {
                duration = Integer.parseInt(durationField.getText());
            }
            
            String place = placeField.isVisible() ? placeField.getText() : "";
            String participants = participantsField.isVisible() ? participantsField.getText() : "";
            String message = messageField.isVisible() ? messageField.getText() : "";
            
            int frequency = 0;
            if (frequencyField.isVisible()) {
                frequency = Integer.parseInt(frequencyField.getText());
            }
            
            // Création de l'événement
            calendarManager.ajouterEvent(type, title, owner, dateTime, duration, place, participants, frequency);
            
            // Mise à jour de la liste des événements
            refreshEventList();
            
            // Réinitialisation des champs
            clearFields();
            
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour la durée et la fréquence.");
        }
    }
    
    /**
     * Gère la suppression d'un événement.
     * @param event L'événement déclencheur
     */
    @FXML
    private void handleDeleteEvent(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            calendarManager.supprimerEvent(selectedEvent.getId());
            refreshEventList();
        } else {
            showAlert("Information", "Veuillez sélectionner un événement à supprimer.");
        }
    }
    
    /**
     * Gère l'exportation des événements au format JSON.
     * @param event L'événement déclencheur
     */
    @FXML
    private void handleExportToJson(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les événements");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
        );
        File file = fileChooser.showSaveDialog(null);
        
        if (file != null) {
            try {
                List<Event> events = calendarManager.getAllEvents();
                JsonUtils.saveToFile(events, file);
                showAlert("Succès", "Les événements ont été exportés avec succès.");
            } catch (IOException e) {
                showAlert("Erreur", "Erreur lors de l'exportation des événements: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gère l'importation des événements depuis un fichier JSON.
     * @param event L'événement déclencheur
     */
    @FXML
    private void handleImportFromJson(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des événements");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
        );
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
            try {
                // Note: Cette partie est simplifiée car l'importation réelle nécessiterait
                // de gérer les différents types d'événements et leur désérialisation
                showAlert("Information", "Fonctionnalité d'importation à implémenter.");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de l'importation des événements: " + e.getMessage());
            }
        }
    }
    
    /**
     * Rafraîchit la liste des événements.
     */
    private void refreshEventList() {
        eventList.clear();
        eventList.addAll(calendarManager.getAllEvents());
    }
    
    /**
     * Réinitialise les champs de saisie.
     */
    private void clearFields() {
        titleField.clear();
        durationField.clear();
        placeField.clear();
        participantsField.clear();
        messageField.clear();
        frequencyField.clear();
    }
    
    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param title Le titre de l'alerte
     * @param message Le message de l'alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
