package org.example.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.template.domain.Placeholder;
import org.example.template.domain.User;
import org.example.template.service.Service;

import java.io.IOException;

public class ControllerLogin {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;

    private Service srv;

    public void setService(Service srv) {
        this.srv = srv;
    }

    @FXML
    public void onLogin() throws IOException {
        User u = srv.login(usernameField.getText(), passwordField.getText());
        if(u != null){
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            ControllerUser   controller = fxmlLoader.getController();
            controller.setService(srv,u);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML private void initialize() {

    }

}