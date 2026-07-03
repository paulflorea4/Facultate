package transport.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.model.User;
import transport.services.ITransportService;
import transport.services.ServiceException;

public class LoginController {
    private ITransportService service;
    private Stage loginStage;
    private Stage officeStage;
    private OfficeController officeController;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void init(ITransportService service, Stage loginStage, Stage officeStage, OfficeController officeController) {
        this.service = service;
        this.loginStage = loginStage;
        this.officeStage = officeStage;
        this.officeController = officeController;
    }

    @FXML
    public void onLoginButton(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both username and password.");
            alert.showAndWait();
            return;
        }

        try {
            User loggedUser = new User(null, username, password);
            service.login(loggedUser, officeController);
            officeController.setCurrentUser(loggedUser);
            officeController.loadTrips();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, " + username + "!");
            alert.showAndWait();

            officeStage.show();
            loginStage.hide();
            passwordField.clear();
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
