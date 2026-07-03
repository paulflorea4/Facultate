package scs.map.sem9_paging;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import scs.map.sem9_paging.config.Config;
import scs.map.sem9_paging.controller.MovieController;
import scs.map.sem9_paging.repository.MovieDBRepository;
import scs.map.sem9_paging.repository.MovieRepository;
import scs.map.sem9_paging.service.MovieService;
import scs.map.sem9_paging.validator.MovieValidator;

import java.io.IOException;

public class MovieApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String url = Config.getProperties().getProperty("db.url");
        String username = Config.getProperties().getProperty("db.username");
        String pasword = Config.getProperties().getProperty("db.password");
//        String username="postgres";
//        String pasword="postgres";
//        String url="jdbc:postgresql://localhost:5436/cinema";

        MovieRepository movieRepository =
                new MovieDBRepository(url,username, pasword);


        MovieService movieService = new MovieService(movieRepository, new MovieValidator());



        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/movies-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movies");
        stage.setScene(scene);

        MovieController movieController = fxmlLoader.getController();
        movieController.setMovieService(movieService);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}