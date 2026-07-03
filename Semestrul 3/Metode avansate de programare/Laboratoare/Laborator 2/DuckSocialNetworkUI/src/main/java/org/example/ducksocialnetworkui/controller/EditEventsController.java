package org.example.ducksocialnetworkui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.Event;
import org.example.ducksocialnetworkui.event.TipEvent;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.EventService;
import org.example.ducksocialnetworkui.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.example.ducksocialnetworkui.event.EntityChangeEventType.*;

public class EditEventsController implements Observer<EntityChangeEvent<Event>> {
    private EventService eventService;
    private ObservableList<Event> model= FXCollections.observableArrayList();

    @FXML private TableView<Event> eventsTable;

    @FXML private TableColumn<Event, Long> tableColumnId;
    @FXML private TableColumn<Event, String> tableColumnName;
    @FXML private TableColumn<Event, TipEvent> tableColumnType;
    @FXML private TableColumn<Event, String> tableColumnStatus;

    @FXML private ComboBox<String> statusComboBox;

    public void init(EventService eventService) {
        this.eventService = eventService;
        eventService.addObserver(this);
        initModel();
    }

    private void initModel() {
        var eventList=eventService.getAllEvents();
        model.setAll(eventList);
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        eventsTable.setItems(model);

        statusComboBox.getItems().addAll("PENDING", "FINISHED");
        statusComboBox.valueProperty().addListener((obs, old, status) -> {
            filterByStatus(status);
        });
    }

    @FXML
    public void onAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Race Event");
        dialog.setHeaderText("Create a new race event");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Event name");

        TextField distancesField = new TextField();
        distancesField.setPromptText("Distances (e.g. 100,200,300)");

        VBox content = new VBox(10,
                new Label("Event name:"),
                nameField,
                new Label("Distances:"),
                distancesField
        );
        content.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(content);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        nameField.textProperty().addListener((obs, old, val) ->
                addButton.setDisable(val.trim().isEmpty())
        );

        dialog.showAndWait().ifPresent(result -> {
            if (result == addButtonType) {
                try {
                    String name = nameField.getText().trim();
                    List<Long> distances = parseDistances(distancesField.getText());

                    eventService.addRaceEvent(name, distances);
                } catch (Exception e) {
                    MessageAlert.showErrorMessage(null,e.getMessage());
                }
            }
        });
    }

    private List<Long> parseDistances(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Distances cannot be empty.");
        }

        List<Long> distances = new ArrayList<>();
        String[] parts = input.split(",");

        for (String p : parts) {
            try {
                long value = Long.parseLong(p.trim());
                if (value <= 0) {
                    throw new NumberFormatException();
                }
                distances.add(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid distance: " + p);
            }
        }

        return distances;
    }


    @FXML
    public void onDelete() {
        Event selected = eventsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            MessageAlert.showErrorMessage(null,"Please select an event to start.");
            return;
        }

        try {
            eventService.removeRaceEvent(selected.getEventID());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    public void onStart() {
        Event selected = eventsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            MessageAlert.showErrorMessage(null, "Please select an event to start.");
            return;
        }
        if (!"PENDING".equals(selected.getStatus())) {
            MessageAlert.showErrorMessage(null, "Only PENDING events can be started.");
            return;
        }
        try {
            eventService.startRaceEvent(selected.getEventID());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }


    private void filterByStatus(String status) {
        if (status == null || status.isEmpty()) {
            model.setAll(eventService.getAllEvents());
        } else {
            model.setAll(eventService.getEventsByStatus(status));
        }
    }

    @Override
    public void update(EntityChangeEvent<Event> event) {
        switch (event.getChangeType()) {
            case ADD, DELETE, UPDATE -> initModel();

            case EVENT_FINISHED -> {
                initModel();

                Event finished = event.getData();
                if (finished != null && finished.getEventID() != null) {
                    String results = eventService.getRaceResults(finished.getEventID());


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Race finished");
                    alert.setHeaderText("Results for: " + finished.getName());
                    alert.setContentText(results);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.showAndWait();
                }
            }
        }
    }
}
