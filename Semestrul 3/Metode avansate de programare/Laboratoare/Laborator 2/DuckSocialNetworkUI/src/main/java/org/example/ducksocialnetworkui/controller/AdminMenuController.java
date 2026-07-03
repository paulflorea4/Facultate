package org.example.ducksocialnetworkui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.application.DuckApplication;
import org.example.ducksocialnetworkui.application.PersonApplication;
import org.example.ducksocialnetworkui.repository.EventRepository;
import org.example.ducksocialnetworkui.service.EventService;
import org.example.ducksocialnetworkui.service.FriendshipService;
import org.example.ducksocialnetworkui.service.UserService;

import java.io.IOException;

public class AdminMenuController {
    private UserService userService;
    private FriendshipService friendshipService;
    private EventService eventService;

    @FXML private TabPane menuTabPane;

    public void init(UserService userService, FriendshipService friendshipService, EventService eventService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.eventService = eventService;
    }

    @FXML
    public void showPersons(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(PersonApplication.class.getResource("/org/example/ducksocialnetworkui/person-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("PersonView");
        stage.setScene(scene);
        PersonController personController = fxmlLoader.getController();
        personController.init(userService);
        stage.show();
    }

    @FXML
    public void showDucks(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DuckApplication.class.getResource("/org/example/ducksocialnetworkui/duck-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("DuckView");
        stage.setScene(scene);
        DuckController duckController = fxmlLoader.getController();
        duckController.init(userService);
        stage.show();
    }

    @FXML
    public void onEditUsers(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/edit-users-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Add/Delete User");
        stage.setScene(scene);
        EditUsersController editUsersController = fxmlLoader.getController();
        editUsersController.init(userService);
        stage.show();
    }

    @FXML
    public void onEditFriendships(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/edit-friendships-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Add/Delete Friendship");
        stage.setScene(scene);
        EditFriendshipsController editFriendshipsController = fxmlLoader.getController();
        editFriendshipsController.init(friendshipService);
        stage.show();
    }

    @FXML
    public void onEditEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/edit-events-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Add/Delete Events");
        stage.setScene(scene);
        EditEventsController editEventsController = fxmlLoader.getController();
        editEventsController.init(eventService);
        stage.show();
    }

    @FXML
    public void switchToUsersTab(ActionEvent actionEvent) {
        menuTabPane.getSelectionModel().select(1);
    }

    @FXML
    public void switchToComunityTab(ActionEvent actionEvent) {
        menuTabPane.getSelectionModel().select(2);
    }

    @FXML
    public void switchToEventsTab(ActionEvent actionEvent) {menuTabPane.getSelectionModel().select(3);}

    @FXML
    public void numberOfComunities(){
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Numarul de comunitati: ",friendshipService.getNumberOfCommunities()+" comunitati existente");
    }

    @FXML
    public void mostSociableComunity(){
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Cea mai sociabila comunitate: ","Id-uri: "+friendshipService.getMostSociableCommunity().toString());
    }
}
