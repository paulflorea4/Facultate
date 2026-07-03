package Controller;

import Domain.Jucator;
import Client.rpc.ServicesRpcProxy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField aliasField;

    private String serverHost;
    private int serverPort;
    private Stage loginStage;

    public void setConnectionInfo(String host, int port, Stage stage) {
        this.serverHost = host;
        this.serverPort = port;
        this.loginStage = stage;
    }

    @FXML
    private void handleLogin() {
        String alias = aliasField.getText().trim();
        if (alias.isEmpty()) {
            MessageAlert.showMessage(loginStage, Alert.AlertType.WARNING,
                    "Avertisment", "Introduceți un alias!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/main-view.fxml"));
            Parent root = loader.load();
            MainController mainController = loader.getController();

            ServicesRpcProxy service = new ServicesRpcProxy(serverHost, serverPort);

            Jucator loggedJucator = service.login(alias, mainController);

            Stage mainStage = new Stage();
            mainStage.setTitle("Joc Online - " + loggedJucator.getAlias());
            mainController.initData(service, loggedJucator, mainStage, loginStage);

            mainStage.setScene(new Scene(root));
            mainStage.show();
            loginStage.hide();

        } catch (Exception e) {
            MessageAlert.showMessage(loginStage, Alert.AlertType.ERROR,
                    "Eroare autentificare", e.getMessage());
        }
    }
}
