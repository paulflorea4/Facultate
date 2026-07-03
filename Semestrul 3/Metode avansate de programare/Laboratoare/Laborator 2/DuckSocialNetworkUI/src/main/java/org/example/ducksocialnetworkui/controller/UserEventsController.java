package org.example.ducksocialnetworkui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import org.example.ducksocialnetworkui.domain.*;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.Event;
import org.example.ducksocialnetworkui.event.RaceEvent;
import org.example.ducksocialnetworkui.event.TipEvent;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.EventService;

public class UserEventsController implements Observer<EntityChangeEvent<Event>> {

    private EventService eventService;
    private User currentUser;

    private final ObservableList<Event> model = FXCollections.observableArrayList();

    @FXML private TableView<Event> eventsTable;

    @FXML private TableColumn<Event, Long> tableColumnId;
    @FXML private TableColumn<Event, String> tableColumnName;
    @FXML private TableColumn<Event, TipEvent> tableColumnType;
    @FXML private TableColumn<Event, String> tableColumnStatus;
    @FXML private TableColumn<Event, Button> tableColumnAction;

    public void init(EventService eventService, User currentUser) {
        this.eventService = eventService;
        this.currentUser = currentUser;

        eventService.addObserver(this);
        initActionColumn();
        initModel();
    }


    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        eventsTable.setItems(model);
    }

    private void initModel() {
        model.setAll(eventService.getAllEvents());
    }

    private void initActionColumn() {
        tableColumnAction.setCellFactory(col -> new TableCell<>() {

            private final Button actionButton = new Button();

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Event event = getTableRow().getItem();

                if (currentUser instanceof SwimmingDuck) {
                    actionButton.setText("Join");
                    actionButton.setOnAction(e -> joinRace(event));
                } else {
                    actionButton.setText("Subscribe");
                    actionButton.setOnAction(e -> subscribe(event));
                }

                actionButton.setDisable("FINISHED".equals(event.getStatus()));

                setGraphic(actionButton);
            }
        });
    }

    private void joinRace(Event event) {
        try {
            eventService.addDuckToRace(event.getEventID(), currentUser.getId());
            MessageAlert.showMessage(
                    null,
                    Alert.AlertType.INFORMATION,
                    "Joined",
                    "You joined the race successfully."
            );
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "You already joined the race!");
        }
    }

    private void subscribe(Event event) {
        try {
            eventService.subscribe(event.getEventID(), currentUser.getId());
            MessageAlert.showMessage(
                    null,
                    Alert.AlertType.INFORMATION,
                    "Subscribed",
                    "You subscribed to the event successfully."
            );
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private boolean isParticipant(RaceEvent event) {
        return (currentUser instanceof SwimmingDuck) &&
                (event.getDucks()
                        .stream()
                        .anyMatch(duck -> duck.equals(currentUser)));
    }

    private boolean isSubscriber(Long eventID) {
        return eventService.isUserSubscribed(eventID, currentUser.getId());
    }

    private void handleNotificationForFinishedEvent(RaceEvent event) {
        Long eventID = event.getEventID();

        String title = "Notification for " + currentUser.getUsername() +" Race " + event.getName() + " finished!";
        String message;

        if (currentUser == null || isSubscriber(eventID)) {
            message = event.getResults();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,title ,message);
        }
        else if (isParticipant(event)) {
            Long currentUserId = currentUser.getId();
            message = event.showDuckResult(currentUserId);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,title ,message);
        }
    }

    @Override
    public void update(EntityChangeEvent<Event> event) {
        switch (event.getChangeType()) {
            case ADD, DELETE, UPDATE -> initModel();

            case EVENT_FINISHED -> {
                initModel();
                handleNotificationForFinishedEvent((RaceEvent) event.getData());
            }
        }
    }

    public void onClose() {
        eventService.removeObserver(this);
    }
}
