package org.example.laboratorGUI.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.laboratorGUI.controller.MainController;
import org.example.laboratorGUI.database.DBConnection;
import org.example.laboratorGUI.repository.database.*;
import org.example.laboratorGUI.service.FlockService;
import org.example.laboratorGUI.service.EventService;
import org.example.laboratorGUI.service.MessageService;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.GUI.ViewLoader;

import java.io.IOException;
import java.sql.SQLException;

public class MainApplication extends Application {
    private DBFlockRepository flockRepo;
    private DBUserRepository userRepo;
    private DBFriendshipRepository friendshipRepo;
    private DBMessageRepository msgRepo;
    private DBEventRepository eventRepo;

    private EventService eventSrv;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        flockRepo = new DBFlockRepository(DBConnection.getConnection());
        userRepo = new DBUserRepository(DBConnection.getConnection());
        friendshipRepo = new DBFriendshipRepository(DBConnection.getConnection());
        msgRepo = new DBMessageRepository(DBConnection.getConnection());
        eventRepo = new DBEventRepository(DBConnection.getConnection());

        UserService userSrv = new UserService(userRepo, friendshipRepo, flockRepo);
        FlockService flockSrv = new FlockService(userRepo, flockRepo);
        MessageService msgSrv = new MessageService(msgRepo);
        eventSrv = new EventService(userRepo, eventRepo);

        String path = "/org/example/laboratorGUI/main-window.fxml";
        String title = "Choose...";
        ViewLoader.<MainController>showStage(stage, path, title, controller ->
                controller.initializeNecessaryServices(userSrv, flockSrv, msgSrv, eventSrv));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        eventSrv.shutdown();
        closeConnections();
    }

    private void closeConnections() throws SQLException {
        flockRepo.closeConnection();
        userRepo.closeConnection();
        friendshipRepo.closeConnection();
        msgRepo.closeConnection();
        eventRepo.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}
