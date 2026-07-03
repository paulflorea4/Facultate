package scs.map.sem9_paging.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scs.map.sem9_paging.domain.Movie;
import scs.map.sem9_paging.service.MovieService;
import scs.map.sem9_paging.validator.ValidationException;

import java.util.Optional;

public class EditMovieController {
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextField textFieldDirector;
    @FXML
    private TextField textFieldYear;

    private MovieService movieService;
    private Stage dialogStage;
    private Movie movie;

    public void init(MovieService movieService, Stage stage, Movie movie) {
        this.movieService = movieService;
        this.dialogStage = stage;
        this.movie = movie;
        if (null != movie) {
            setFields(movie);
            textFieldId.setEditable(false);
        }
    }

    @FXML
    public void onSave() {
        Long id = textFieldId.getText() != null && !textFieldId.getText().isBlank()
                ? Long.valueOf(textFieldId.getText())
                : null;
        String title = textFieldTitle.getText();
        String director = textFieldDirector.getText();
        Integer year = Integer.valueOf(textFieldYear.getText());
        Movie movie = new Movie(title, director, year);
        movie.setId(id);
        if (null == this.movie)
            insertMovie(movie);
        else
            updateMovie(movie);
    }

    private void updateMovie(Movie movie) {
        try {
            Optional<Movie> savedMovie = this.movieService.update(movie);
            if (savedMovie.isPresent()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Updated Movie", "Movie has been updated");
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }


    private void insertMovie(Movie movie) {
        try {
            Optional<Movie> savedMovie = this.movieService.save(movie);
            if (savedMovie.isPresent()) {
                dialogStage.close();
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Inserted Movie", "Movie has been inserted");
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();

    }

    private void clearFields() {
        textFieldId.setText("");
        textFieldTitle.setText("");
        textFieldDirector.setText("");
        textFieldYear.setText("");
    }

    private void setFields(Movie movie) {
        textFieldId.setText(movie.getId().toString());
        textFieldTitle.setText(movie.getTitle());
        textFieldDirector.setText(movie.getDirector().toString());
        textFieldYear.setText(String.valueOf(movie.getYear()));
    }

    @FXML
    public void onCancel() {
        dialogStage.close();
    }
}

