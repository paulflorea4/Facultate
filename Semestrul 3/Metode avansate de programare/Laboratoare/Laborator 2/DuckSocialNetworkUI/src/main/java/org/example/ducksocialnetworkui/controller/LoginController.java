package org.example.ducksocialnetworkui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.service.*;

import javafx.scene.control.*;
import org.example.ducksocialnetworkui.utils.PasswordUtils;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML
    private Stage stage;

    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private FriendRequestService  friendRequestService;
    private EventService eventService;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void init(UserService userService, FriendshipService friendshipService, MessageService messageService,
                     FriendRequestService friendRequestService, EventService eventService, Stage stage){
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.eventService=eventService;
        this.stage = stage;
    }

    @FXML
    public void loginButtonAction(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username!=null || password!=null){
            try{
                Optional<User> user=userService.verifyCredentials(username);
                if(user.isEmpty()){
                    MessageAlert.showErrorMessage(null,"User-ul "+username+" nu exista!");
                    return;
                }else if(!PasswordUtils.verify(passwordField.getText(),user.get().getPassword())){
                    MessageAlert.showErrorMessage(null,"Parola incorecta!");
                    return;
                }

                openUserPage(user.get());
                stage.close();
            }catch(Exception e){
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        }
    }


    private void openUserPage(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/home-page-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Home");
        stage.setScene(scene);
        HomePageController homePageController = fxmlLoader.getController();
        homePageController.init(userService,friendshipService,messageService,friendRequestService,eventService,user);
        stage.show();
    }
}
