package org.example.ducksocialnetworkui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ducksocialnetworkui.domain.FriendRequest;
import org.example.ducksocialnetworkui.domain.FriendRequestStatus;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.FriendRequestService;

import java.util.Objects;

public class NotificationsController implements Observer<EntityChangeEvent<FriendRequest>> {

    private FriendRequestService service;
    private User currentUser;

    private final ObservableList<FriendRequest> model =
            FXCollections.observableArrayList();

    @FXML
    private TableView<FriendRequest> tableRequests;
    @FXML
    private TableColumn<FriendRequest, Long> colFrom;
    @FXML
    private TableColumn<FriendRequest, String> colStatus;

    @FXML private Button acceptButton;
    @FXML private Button rejectButton;


    public void init(FriendRequestService service, User currentUser) {
        this.service = service;
        this.currentUser = currentUser;

        service.addObserver(this);
        loadModel();
    }


    @FXML
    public void initialize() {
        colFrom.setCellValueFactory(
                new PropertyValueFactory<>("fromId")
        );

        colStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getStatus().name()
                )
        );

        tableRequests.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(FriendRequest fr, boolean empty) {
                super.updateItem(fr, empty);

                if (fr == null || empty) {
                    setStyle("");
                    return;
                }

                if (isSelected()) {
                    setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white;");
                    return;
                }

                switch (fr.getStatus()) {
                    case PENDING ->
                            setStyle("-fx-background-color: #F5C542; -fx-text-fill: black;"); // galben închis
                    case APPROVED ->
                            setStyle("-fx-background-color: #7ED321; -fx-text-fill: black;"); // verde închis
                    case REJECTED ->
                            setStyle("-fx-background-color: #D0021B; -fx-text-fill: white;"); // roșu închis
                }
            }
        });



        tableRequests.setItems(model);
    }

    private void loadModel() {
        model.setAll(service.getAllForUser(currentUser.getId()));
    }



    @FXML
    public void onAccept() {
        FriendRequest selected =
                tableRequests.getSelectionModel().getSelectedItem();

        if (selected == null) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"","Selecteaza o cerere");
            return;
        }

        if (selected.getStatus() != FriendRequestStatus.PENDING) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,
                    "", "Poti raspunde doar la cereri PENDING");
            return;
        }

        service.respond(selected.getId(), true);
    }

    @FXML
    public void onReject() {
        FriendRequest selected =
                tableRequests.getSelectionModel().getSelectedItem();

        if (selected == null) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"","Selecteaza o cerere");
            return;
        }

        if (selected.getStatus() != FriendRequestStatus.PENDING) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,
                    "", "Poti raspunde doar la cereri PENDING");
            return;
        }

        service.respond(selected.getId(), false);
    }


    @Override
    public void update(EntityChangeEvent<FriendRequest> event) {

        if (Objects.isNull(event.getData()))
            return;

        FriendRequest fr = event.getData();

        if (!fr.getToId().equals(currentUser.getId()))
            return;

        if (event.getChangeType() == EntityChangeEventType.FRIEND_REQUEST_RECEIVED ||
                event.getChangeType() == EntityChangeEventType.FRIEND_REQUEST_UPDATED) {

            loadModel();
        }
    }
}
