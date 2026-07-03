package org.example.laboratorGUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.laboratorGUI.service.FlockService;
import org.example.laboratorGUI.service.EventService;
import org.example.laboratorGUI.service.MessageService;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.GUI.ViewLoader;
import org.example.laboratorGUI.utils.MessageBox;

import java.io.IOException;

public class MainController {
    @FXML private Button toAdminView;
    @FXML private Button toUserLogin;

    private UserService userSrv;
    private FlockService flockSrv;
    private MessageService msgSrv;
    private EventService eventSrv;

    public void initializeNecessaryServices(UserService userSrv, FlockService flockSrv, MessageService msgSrv, EventService eventSrv) {
        this.userSrv = userSrv;
        this.flockSrv = flockSrv;
        this.msgSrv = msgSrv;
        this.eventSrv = eventSrv;
    }

    @FXML private void initialize() {
        toAdminView.setOnAction(event -> openAdminWindow());
        toUserLogin.setOnAction(event -> openUserLoginWindow());
    }

    void openAdminWindow() {
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/admin-view.fxml";
            String title = "Duck Social Network";
            ViewLoader.<AdminController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(userSrv, flockSrv, eventSrv));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open admin-view:\n" + exception.getMessage());
        }
    }

    void openUserLoginWindow() {
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/login-window.fxml";
            String title = "Login";
            ViewLoader.<LoginController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(userSrv, msgSrv, eventSrv));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open login-window:\n" + exception.getMessage());
        }
    }
}
