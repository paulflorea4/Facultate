package org.example.laboratorGUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.service.EventService;
import org.example.laboratorGUI.service.MessageService;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.GUI.ViewLoader;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.PasswordUtils;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameTxt;
    @FXML private PasswordField passwordTxt;
    @FXML private Button loginBtn;

    private UserService userSrv;
    private MessageService msgSrv;
    private EventService eventSrv;

    public void initializeNecessaryServices(UserService userSrv, MessageService msgSrv, EventService eventSrv){
        this.userSrv = userSrv;
        this.msgSrv = msgSrv;
        this.eventSrv = eventSrv;
    }

    @FXML private void initialize(){
        loginBtn.setOnAction(event -> tryToLoginUser());
    }

    private void openChatWindow(User loggedUser) {
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/message-view.fxml";
            String title = loggedUser.getUsername();
            ViewLoader.<MessageController>showStage(stage, path, title, controller -> controller.setMsgService(userSrv, msgSrv, eventSrv, loggedUser));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open message-view:\n" + exception.getMessage());
        }
    }

    private void tryToLoginUser() {
        try {
            String usernameInput = usernameTxt.getText();
            User existingUser = userSrv.getUserByUsername(usernameInput);
            if (existingUser != null) {
                String passwordInput = passwordTxt.getText();
                if (PasswordUtils.verify(passwordInput, existingUser.getPassword())) {
                    openChatWindow(existingUser);

                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.close();
                }
                else {
                    throw new RuntimeException("The password is incorrect!");
                }
            }
            else {
                throw new RuntimeException("The user doesn't exist!");
            }
        } catch (RuntimeException ex) {
            MessageBox.showErrorMessage("Could not login:\n" + ex.getMessage());
        }

    }
}
