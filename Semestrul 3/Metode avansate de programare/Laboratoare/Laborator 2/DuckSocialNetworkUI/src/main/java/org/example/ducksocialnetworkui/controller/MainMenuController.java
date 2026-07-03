package org.example.ducksocialnetworkui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.service.*;

import javafx.event.ActionEvent;
import java.io.IOException;

public class MainMenuController {
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private EventService eventService;

    public void init(UserService userService, FriendshipService friendshipService,MessageService messageService,FriendRequestService friendRequestService,EventService eventService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.eventService = eventService;
    }

    @FXML
    public void onAdmin(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/admin-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Admin Menu");
        stage.setScene(scene);
        AdminMenuController adminMenuController = fxmlLoader.getController();
        adminMenuController.init(userService,friendshipService,eventService);
        stage.show();
    }

    @FXML
    public void onLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Login Menu");
        stage.setScene(scene);
        LoginController loginController = fxmlLoader.getController();
        loginController.init(userService,friendshipService,messageService,friendRequestService,eventService,stage);
        stage.show();
    }
}
