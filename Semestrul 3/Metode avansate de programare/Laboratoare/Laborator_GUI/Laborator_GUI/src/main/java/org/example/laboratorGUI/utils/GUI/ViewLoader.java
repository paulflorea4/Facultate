package org.example.laboratorGUI.utils.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.laboratorGUI.application.MainApplication;
import org.example.laboratorGUI.controller.CleanupController;

import java.io.IOException;
import java.util.function.Consumer;

public class ViewLoader {
    public static <T> void showStage(Stage stage, String path, String title, Consumer<T> setupController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(path));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(title);
        stage.setScene(scene);

        T controller = fxmlLoader.getController();
        if (setupController != null)
            setupController.accept(controller);

        stage.setOnCloseRequest(event -> {
            if (controller instanceof CleanupController cleanupController)
                cleanupController.cleanup();
        });

        stage.show();
    }
}
