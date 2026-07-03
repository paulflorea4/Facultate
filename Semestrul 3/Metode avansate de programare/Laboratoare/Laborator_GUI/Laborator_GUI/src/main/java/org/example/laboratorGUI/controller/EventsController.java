package org.example.laboratorGUI.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.domain.user.duck.SwimmingDuck;
import org.example.laboratorGUI.events.Event;
import org.example.laboratorGUI.events.RaceEvent;
import org.example.laboratorGUI.observer.Observer;
import org.example.laboratorGUI.service.EventService;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;
import org.example.laboratorGUI.utils.types.TipEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsController implements Observer, CleanupController {
    @FXML private TableView<Event> tableView;

    @FXML private TableColumn<Event, Long> tableColumnID;
    @FXML private TableColumn<Event, String> tableColumnName;
    @FXML private TableColumn<Event, TipEvent> tableColumnType;
    @FXML private TableColumn<Event, String> tableColumnStatus;
    @FXML private TableColumn<Event, Void> tableColumnAction;

    @FXML private Button addEventBtn;
    @FXML private Button removeEventBtn;

    @FXML private ComboBox<String> filterBox;

    private EventService eventSrv;
    private User loggedUser;

    private final ObservableList<Event> events = FXCollections.observableArrayList();

    public void initializeNecessaryServices(EventService eventSrv, User user) {
        this.eventSrv = eventSrv;
        this.loggedUser = user;
        loadEvents();
        showButtonsOnlyToAdmin();

        eventSrv.addObserver(this);
    }

    private void loadEvents() {
        String status = filterBox.getValue();
        List<Event> eventsList = eventSrv.getEventsByStatus(status);
        events.setAll(eventsList);
    }

    private void showButtonsOnlyToAdmin() {
        if (loggedUser != null) {
            addEventBtn.setDisable(true);
            addEventBtn.setOpacity(0);

            removeEventBtn.setDisable(true);
            removeEventBtn.setOpacity(0);
        }
    }

    @FXML private void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("eventID"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        addActionButtonToTable();

        tableView.setItems(events);

        addEventBtn.setOnAction(event -> openAddRaceEventWindow());
        removeEventBtn.setOnAction(event -> removeRaceEvent());

        filterBox.getItems().add("PENDING");
        filterBox.getItems().add("FINISHED");
        filterBox.getSelectionModel().select(0);
        filterBox.setOnAction(event -> loadEvents());
    }

    private void addActionButtonToTable() {
        Callback<TableColumn<Event, Void>, TableCell<Event, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Event, Void> call(final TableColumn<Event, Void> param) {
                return new TableCell<>() {
                    private final Button actionBtn = new Button();

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty)
                            setGraphic(null);
                        else {
                            Event currentEvent = getTableView().getItems().get(getIndex());
                            setupActionButton(actionBtn, currentEvent);
                            setGraphic(actionBtn);
                        }
                    }
                };
            }
        };
        tableColumnAction.setCellFactory(cellFactory);
    }

    private void setupActionButton(Button actionBtn, Event selectedEvent) {
        String currentStatus = selectedEvent.getStatus();
        if (currentStatus.equals("FINISHED"))
            setupShowPastResultsButton(actionBtn, selectedEvent);
        else if (loggedUser == null)
            setupAdminButton(actionBtn, selectedEvent);
        else if (loggedUser instanceof SwimmingDuck)
            setupJoinButton(actionBtn, selectedEvent);
        else
            setupSubscribeButton(actionBtn, selectedEvent);
    }

    private void setupShowPastResultsButton(Button actionBtn, Event selectedEvent) {
        actionBtn.setText("View");
        actionBtn.setOnAction(event -> showPastResults(selectedEvent));
    }

    private void showPastResults(Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        String results = eventSrv.getRaceResults(eventID);
        MessageBox.showInformationMessage("Results", results);
    }

    private void setupAdminButton(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        actionBtn.setText("Start");
        actionBtn.setOnAction(event -> eventSrv.startRaceEvent(eventID));
    }

    private void setupJoinButton(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        Long loggedUserID = loggedUser.getUserID();

        boolean isJoined = eventSrv.getRaceEvent(eventID)
                .getDucks()
                .stream()
                .anyMatch(duck -> duck.getUserID().equals(loggedUserID));

        if (isJoined)
            configureButtonToLeave(actionBtn, selectedEvent);
        else
            configureButtonToJoin(actionBtn, selectedEvent);
    }

    private void configureButtonToJoin(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        Long loggedUserID = loggedUser.getUserID();

        actionBtn.setText("Join");
        actionBtn.setOnAction(event -> {
            try {
                eventSrv.addDuckToRaceEvent(eventID, loggedUserID);
                MessageBox.showInformationMessage("Success", "You have joined the race!");
                configureButtonToLeave(actionBtn, selectedEvent);
            } catch (RuntimeException exception) {
                MessageBox.showErrorMessage("Could not join:\n" + exception.getMessage());
            }
        });
    }

    private void configureButtonToLeave(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        Long loggedUserID = loggedUser.getUserID();

        actionBtn.setText("Leave");
        actionBtn.setOnAction(event -> {
            try {
                eventSrv.removeDuckFromRaceEvent(eventID, loggedUserID);
                MessageBox.showInformationMessage("Success", "You have left the race!");
                configureButtonToJoin(actionBtn, selectedEvent);
            } catch (RuntimeException exception) {
                MessageBox.showErrorMessage("Could not leave:\n" + exception.getMessage());
            }
        });
    }

    private void setupSubscribeButton(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        Long loggedUserID = loggedUser.getUserID();

        boolean isSubscribed = eventSrv.isUserSubscribed(eventID, loggedUserID);
        if (isSubscribed)
            configureButtonToUnsubscribe(actionBtn, selectedEvent);
        else
            configureButtonToSubscribe(actionBtn, selectedEvent);
    }

    private void configureButtonToSubscribe(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        Long loggedUserID = loggedUser.getUserID();

        actionBtn.setText("Subscribe");
        actionBtn.setOnAction(event -> {
            try {
                eventSrv.addSubscriber(eventID, loggedUserID);
                MessageBox.showInformationMessage("Success", "Subscribed to " + selectedEvent.getName());
                configureButtonToUnsubscribe(actionBtn, selectedEvent);
            } catch (RuntimeException exception) {
                MessageBox.showErrorMessage("Could not subscribe:\n" + exception.getMessage());
            }
        });
    }

    private void configureButtonToUnsubscribe(Button actionBtn, Event selectedEvent) {
        Long eventID = selectedEvent.getEventID();
        Long loggedUserID = loggedUser.getUserID();

        actionBtn.setText("Unsubscribe");
        actionBtn.setOnAction(event -> {
            try {
                eventSrv.removeSubscriber(eventID, loggedUserID);
                MessageBox.showInformationMessage("Success", "Unsubscribed from " + selectedEvent.getName());
                configureButtonToSubscribe(actionBtn, selectedEvent);
            } catch (RuntimeException exception) {
                MessageBox.showErrorMessage("Could not unsubscribe: " + exception.getMessage());
            }
        });
    }

    private void openAddRaceEventWindow() {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nameInput = new TextField();
        nameInput.setPromptText("Event Name");

        ObservableList<Long> distancesTemp = FXCollections.observableArrayList();
        ListView<Long> distListView = new ListView<>(distancesTemp);
        distListView.setPrefHeight(100);

        TextField distInput = new TextField();
        distInput.setPromptText("Distance");
        Button addDistBtn = new Button("Add Lane");
        addDistBtn.setOnAction(event -> handleDistanceInput(distInput, distancesTemp));

        Button saveBtn = new Button("Create Event");
        saveBtn.setOnAction(event -> {
            String name = nameInput.getText();
            List<Long> finalDistances = new ArrayList<>(distancesTemp);
            addRaceEvent(stage, name, TipEvent.Race, finalDistances);
        });

        layout.getChildren().addAll(new Label("Name:"), nameInput,
                new Label("Lanes:"), distListView,
                new HBox(5, distInput, addDistBtn),
                saveBtn);

        stage.setScene(new Scene(layout, 300, 400));
        stage.show();
    }

    private void handleDistanceInput(TextField distanceInput, ObservableList<Long> distancesTemp) {
        try {
            Long d = Long.parseLong(distanceInput.getText());
            distancesTemp.add(d);
            distanceInput.clear();
        } catch (NumberFormatException exception) {
            MessageBox.showErrorMessage("Please enter a valid distance.");
        }
    }

    private void addRaceEvent(Stage stage, String name, TipEvent type, List<Long> distances) {
        try {
            eventSrv.addRaceEvent(name, type, distances);
            stage.close();
            MessageBox.showInformationMessage("Success", "Event added.");
        } catch (RuntimeException exception) {
            MessageBox.showErrorMessage("Could not add race event:\n" + exception.getMessage());
        }
    }

    private void removeRaceEvent() {
        try {
            Long eventID = tableView.getSelectionModel().getSelectedItem().getEventID();
            eventSrv.removeRaceEvent(eventID);
            MessageBox.showInformationMessage("Success!", "Event removed.");
        } catch (RuntimeException exception) {
            MessageBox.showErrorMessage("Could not remove race event:\n" + exception.getMessage());
        }
    }

    private boolean isParticipant(RaceEvent event) {
        return (loggedUser instanceof SwimmingDuck) &&
                (event.getDucks()
                        .stream()
                        .anyMatch(duck -> duck.equals(loggedUser)));
    }

    private boolean isSubscriber(Long eventID) {
        return eventSrv.isUserSubscribed(eventID, loggedUser.getUserID());
    }

    private void handleNotificationForFinishedEvent(RaceEvent event) {
        Long eventID = event.getEventID();

        String title = "Race " + event.getName() + " finished!";
        String message;

        if (loggedUser == null || isSubscriber(eventID)) {
            message = event.showResults();
            MessageBox.showInformationMessage(title, message);
        }
        else if (isParticipant(event)) {
            Long loggedUserID = loggedUser.getUserID();
            message = event.showDuckResult(loggedUserID);
            MessageBox.showInformationMessage(title, message);
        }
    }

    @Override
    public void update(EntityChangeEvent event) {
        if (event.getData() instanceof RaceEvent race) {
            switch (event.getType()) {
                case EVENT_ADDED -> {
                    if (filterBox.getValue().equals("PENDING"))
                        tableView.getItems().add(race);
                }
                case EVENT_REMOVED -> tableView.getItems().remove(race);
                case EVENT_FINISHED -> {
                    handleNotificationForFinishedEvent(race);
                    if (filterBox.getValue().equals("PENDING"))
                        tableView.getItems().remove(race);
                    else
                        tableView.getItems().add(race);
                }
            }
        }
    }

    @Override
    public void cleanup() {
        eventSrv.removeObserver(this);
    }
}
