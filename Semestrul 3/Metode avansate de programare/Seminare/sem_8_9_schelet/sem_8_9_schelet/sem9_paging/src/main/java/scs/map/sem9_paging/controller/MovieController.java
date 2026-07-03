package scs.map.sem9_paging.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scs.map.sem9_paging.MovieApplication;
import scs.map.sem9_paging.domain.Movie;

import scs.map.sem9_paging.observer.Observer;
import scs.map.sem9_paging.service.MovieService;
import scs.map.sem9_paging.util.event.EntityChangeEvent;
import scs.map.sem9_paging.util.event.EntityChangeEventType;
import scs.map.sem9_paging.util.paging.Page;
import scs.map.sem9_paging.util.paging.Pageable;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MovieController implements Observer<EntityChangeEvent> {
    private MovieService movieService;
    private ObservableList<Movie> model = FXCollections.observableArrayList();
    @FXML
    private TableView<Movie> tableView;
    @FXML
    private TableColumn<Movie, Long> tableColumnId;
    @FXML
    private TableColumn<Movie, String> tableColumnTitle;
    @FXML
    private TableColumn<Movie, String> tableColumnDirector;
    @FXML
    private TableColumn<Movie, Integer> tableColumnYear;
    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonPrevious;
    @FXML
    private Label labelPage;
    @FXML
    private ComboBox<Integer> comboBoxfilterYear;
    @FXML
    private TextField textFieldfilterYearAfter;
    @FXML
    private TextField textFieldfilterDirector;
    @FXML
    private TextField textFieldfilterTitle;
    
    private int pageSize = 5;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;



    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
        movieService.addObserver(this);
        initModel();
        initYearsCombo();
    }

    private void initYearsCombo() {
        List<Integer> years = movieService.getYears();
        years.addFirst(null);
        comboBoxfilterYear.getItems().setAll(years);
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumnDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        tableColumnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        tableView.setItems(model);
    }

    private void initModel() {
        var page=movieService.findAllOnPage(new Pageable(currentPage,pageSize));
        int maxPage=(int)Math.ceil(1.0*page.getTotalNumberOfElements()/pageSize)-1;
        if(maxPage==-1){
            maxPage=0;
        }
        if(currentPage>maxPage){
            currentPage=maxPage;
            page=movieService.findAllOnPage(new Pageable(currentPage,pageSize));
        }
        totalNumberOfElements=page.getTotalNumberOfElements();
        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage+1)*pageSize == totalNumberOfElements);
        var movieList=StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(movieList);
        labelPage.setText("Page "+(currentPage+1)+" of "+(maxPage+1));
    }

    public void onDelete(ActionEvent actionEvent) {
        Movie selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Optional<Movie> deleted = movieService.delete(selected.getId());
            if (deleted.isPresent()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Movie has been deleted");
                 initModel();
            }
        } else {
            MessageAlert.showErrorMessage(null, "Select a movie first!");
        }
    }

    public void onEdit(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieApplication.class.getResource("views/edit-movie-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setTitle("Add/edit movie");
        stage.setScene(scene);
        EditMovieController editMovieController = fxmlLoader.getController();
        Movie selected = tableView.getSelectionModel().getSelectedItem();
        editMovieController.init(movieService, stage, selected);
        stage.show();
    }

    @Override
    public void update(EntityChangeEvent event) {
        if (event.getType() == EntityChangeEventType.ADD || event.getType()==EntityChangeEventType.UPDATE) {
            currentPage = 0;
            initModel();
        }

    }

    public void onNextPage(ActionEvent actionEvent) {
        currentPage ++;
        initModel();
    }

    public void onPreviousPage(ActionEvent actionEvent) {
        currentPage --;
        initModel();
    }
}


