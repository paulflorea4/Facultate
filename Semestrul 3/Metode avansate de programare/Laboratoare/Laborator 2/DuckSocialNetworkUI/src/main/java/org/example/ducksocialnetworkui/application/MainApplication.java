package org.example.ducksocialnetworkui.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.Main;
import org.example.ducksocialnetworkui.config.Configuration;
import org.example.ducksocialnetworkui.controller.AdminMenuController;
import org.example.ducksocialnetworkui.controller.MainMenuController;
import org.example.ducksocialnetworkui.domain.Message;
import org.example.ducksocialnetworkui.repository.*;
import org.example.ducksocialnetworkui.service.*;
import org.example.ducksocialnetworkui.validation.ValidatorDuck;
import org.example.ducksocialnetworkui.validation.ValidatorEvent;
import org.example.ducksocialnetworkui.validation.ValidatorPersoana;

import java.io.IOException;
import java.util.Properties;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties properties= Configuration.getConfig();
        String dbUrl=properties.getProperty("db.url");
        String dbUser=properties.getProperty("db.username");
        String dbPassword=properties.getProperty("db.password");

        UserRepository userRepository = new UserDBRepository(dbUrl, dbUser, dbPassword);
        UserService userService = new UserService(userRepository,new ValidatorPersoana(),new ValidatorDuck());

        FriendshipRepository friendshipRepository= new FriendshipDBRepository(dbUrl, dbUser, dbPassword);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);

        MessageRepository  messageRepository = new MessageDBRepository(dbUrl, dbUser, dbPassword);
        MessageService messageService = new MessageService(messageRepository);

        FriendRequestRepository friendRequestRepository = new FriendRequestDBRepository(dbUrl, dbUser, dbPassword);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository,friendshipService);

        EventRepository eventRepository = new EventDBRepository(dbUrl, dbUser, dbPassword);
        EventService eventService = new EventService(eventRepository,userRepository,new ValidatorEvent());

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/ducksocialnetworkui/main-menu-view.fxml")
        );


        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("DuckSocialNetwork");
        stage.setScene(scene);

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.init(userService,friendshipService,messageService,friendRequestService,eventService);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
