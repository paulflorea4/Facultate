package org.example.ducksocialnetworkui.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.config.Configuration;
import org.example.ducksocialnetworkui.controller.DuckController;
import org.example.ducksocialnetworkui.controller.PersonController;
import org.example.ducksocialnetworkui.repository.UserDBRepository;
import org.example.ducksocialnetworkui.repository.UserRepository;
import org.example.ducksocialnetworkui.service.UserService;
import org.example.ducksocialnetworkui.validation.ValidatorDuck;
import org.example.ducksocialnetworkui.validation.ValidatorPersoana;

import java.io.IOException;
import java.util.Properties;

public class PersonApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties properties= Configuration.getConfig();
        String dbUrl=properties.getProperty("db.url");
        String dbUser=properties.getProperty("db.username");
        String dbPassword=properties.getProperty("db.password");

        UserRepository userRepository = new UserDBRepository(dbUrl, dbUser, dbPassword);
        UserService userService = new UserService(userRepository,new ValidatorPersoana(),new ValidatorDuck());

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/ducksocialnetworkui/person-view.fxml")
        );


        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Persons");
        stage.setScene(scene);

        PersonController personController = fxmlLoader.getController();
        personController.init(userService);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
