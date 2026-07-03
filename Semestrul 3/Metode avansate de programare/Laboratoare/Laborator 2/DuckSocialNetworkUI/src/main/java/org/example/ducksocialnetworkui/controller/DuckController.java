package org.example.ducksocialnetworkui.controller;

import org.example.ducksocialnetworkui.domain.*;
import org.example.ducksocialnetworkui.dto.UserDto;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.UserService;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DuckController implements Observer<EntityChangeEvent<User>> {
    private UserService userService;
    private ObservableList<User> model= FXCollections.observableArrayList();

    @FXML private TableView<User> tableView;

    @FXML private TableColumn<Duck, Long> tableColumnId;
    @FXML private TableColumn<Duck, String> tableColumnName;
    @FXML private TableColumn<Duck, TipRata> tableColumnTip;
    @FXML private TableColumn<Duck, Double> tableColumnViteza;
    @FXML private TableColumn<Duck, Double> tableColumnRezistenta;
    @FXML private TableColumn<Duck, Long> tableColumnCardId;


    @FXML private ComboBox<String> comboBoxDuckType;

    @FXML private Button buttonNext;

    @FXML private Button buttonPrevious;

    @FXML private Label labelPage;

    private int pageSize = 8;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;
    private final UserDto userFilter=new UserDto();

    public void init(UserService userService) {
        this.userService = userService;
        userService.addObserver(this);
        initModel();
    }

    private void initModel() {
        var page = userService.getAllPage(new Pageable(currentPage,pageSize),userFilter);
        int maxPage = (int)Math.ceil(1.0*page.getTotalNumberOfElements()/pageSize)-1;
        if(maxPage==-1){
            maxPage=0;
        }
        if(currentPage > maxPage){
            currentPage = maxPage;
            page=userService.getAllPage(new Pageable(currentPage,pageSize),userFilter);
        }

        totalNumberOfElements=page.getTotalNumberOfElements();
        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage+1)*pageSize == totalNumberOfElements);

        var ducksList = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                        .collect(Collectors.toList());

        model.setAll(ducksList);
        labelPage.setText("Page "+(currentPage+1)+ " of " + (maxPage+1));
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnViteza.setCellValueFactory(new PropertyValueFactory<>("viteza"));
        tableColumnRezistenta.setCellValueFactory(new PropertyValueFactory<>("rezistenta"));
        tableColumnCardId.setCellValueFactory(new PropertyValueFactory<>("cardId"));

        tableView.setItems(model);
        userFilter.setType("Duck");

        comboBoxDuckType.getItems().addAll("Inotator", "Zburator", "Hibrid", "Toate");
        comboBoxDuckType.valueProperty().addListener((obs, old, type) -> {
            filterByDuckType(type);
        });
    }


    @Override
    public void update(EntityChangeEvent<User> event) {
        if(event.getChangeType() == EntityChangeEventType.ADD || event.getChangeType() == EntityChangeEventType.UPDATE || event.getChangeType() == EntityChangeEventType.DELETE){
            currentPage=0;
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

    private void filterByDuckType(String type){
            switch (type) {
                case "Inotator" -> userFilter.setDuckType("SWIMMING");
                case "Zburator" -> userFilter.setDuckType("FLYING");
                case "Hibrid" -> userFilter.setDuckType("FLYING_AND_SWIMMING");
                case "Toate" -> userFilter.setDuckType(null);
            }
        initModel();
    }
}
