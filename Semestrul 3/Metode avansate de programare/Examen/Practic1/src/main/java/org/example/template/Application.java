package org.example.template;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.repository.CarDBRepository;
import org.example.template.repository.UserDBRepository;
import org.example.template.service.Service;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserDBRepository userDBRepository = new UserDBRepository();
        CarDBRepository carDBRepository = new CarDBRepository();
        Service service = new Service(userDBRepository,carDBRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ControllerLogin controller = fxmlLoader.getController();
        controller.setService(service);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}