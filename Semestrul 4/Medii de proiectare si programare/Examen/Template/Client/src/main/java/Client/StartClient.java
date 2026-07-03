package Client;

import Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartClient extends Application {
    private static final Logger logger = LogManager.getLogger();
    private String host = "localhost";
    private int port = 55555;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/client.properties")) {
            if (is != null) {
                props.load(is);
                host = props.getProperty("server.host", "localhost");
                port = Integer.parseInt(props.getProperty("server.port", "55555"));
                logger.info("Configuration loaded: HOST={}, PORT={}", host, port);
            } else {
                logger.warn("client.properties not found, using default configuration.");
            }
        } catch (IOException | NumberFormatException e) {
            logger.error("Error loading configuration: ", e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Pornire aplicație client...");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
        Parent root = loader.load();

        LoginController loginCtrl = loader.getController();
        loginCtrl.setConnectionInfo(host, port, primaryStage);

        primaryStage.setTitle("Joc Online - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        logger.info("Interfață client pornită.");
    }
}
