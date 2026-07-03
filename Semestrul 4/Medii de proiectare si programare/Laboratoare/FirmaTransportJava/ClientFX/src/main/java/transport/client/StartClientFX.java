package transport.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.client.gui.LoginController;
import transport.client.gui.OfficeController;
import transport.client.gui.TripController;
import transport.networking.protocol.TransportServicesProxy;
import transport.services.ITransportService;

import java.util.Properties;

public class StartClientFX extends Application {
    private static final int defaultPort = 55555;
    private static final String defaultHost = "localhost";
    private static final Logger logger = LogManager.getLogger(StartClientFX.class);

    @Override
    public void start(Stage loginStage) throws Exception {
        logger.debug("Starting ClientFX");

        Properties clientProps = new Properties();
        try {
            clientProps.load(this.getClass().getClassLoader().getResourceAsStream("client.properties"));
            logger.info("Client properties set: {}", clientProps);
        } catch (Exception e) {
            logger.error("Cannot find client.properties {}", e.getMessage());
            logger.debug("Looking for file in {}", (new java.io.File(".")).getAbsolutePath());
            return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultHost);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port", String.valueOf(defaultPort)));
        } catch (NumberFormatException e) {
            logger.error("Wrong port number {}", e.getMessage());
            logger.debug("Using default port {}", defaultPort);
        }

        logger.info("Using server IP {} and server port {}", serverIP, serverPort);

        ITransportService service = new TransportServicesProxy(serverIP, serverPort);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
        FXMLLoader officeLoader = new FXMLLoader(getClass().getResource("/office-view.fxml"));
        FXMLLoader tripLoader = new FXMLLoader(getClass().getResource("/trip-view.fxml"));

        Parent loginRoot = loginLoader.load();
        Parent officeRoot = officeLoader.load();
        Parent tripRoot = tripLoader.load();

        Scene loginScene = new Scene(loginRoot);
        Scene officeScene = new Scene(officeRoot);
        Scene tripScene = new Scene(tripRoot);

        Stage officeStage = new Stage();
        officeStage.setTitle("Office View");
        officeStage.setScene(officeScene);

        Stage tripStage = new Stage();
        tripStage.setTitle("Trip Seats");
        tripStage.setScene(tripScene);

        LoginController loginController = loginLoader.getController();
        OfficeController officeController = officeLoader.getController();
        TripController tripController = tripLoader.getController();

        officeController.init(service, loginStage, officeStage, tripStage, tripController);
        loginController.init(service, loginStage, officeStage, officeController);

        loginStage.setTitle("Login");
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
