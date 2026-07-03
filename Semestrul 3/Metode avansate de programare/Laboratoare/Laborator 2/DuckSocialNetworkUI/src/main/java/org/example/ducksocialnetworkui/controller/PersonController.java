package org.example.ducksocialnetworkui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ducksocialnetworkui.domain.Persoana;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.dto.UserDto;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.UserService;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PersonController implements Observer<EntityChangeEvent<User>> {
    private UserService userService;
    private ObservableList<User> model= FXCollections.observableArrayList();

    @FXML
    private TableView<User> tableView;

    @FXML private TableColumn<Persoana, Long> tableColumnId;
    @FXML private TableColumn<Persoana, String> tableColumnUsername;
    @FXML private TableColumn<Persoana, String> tableColumnLastName;
    @FXML private TableColumn<Persoana, String> tableColumnFirstName;
    @FXML private TableColumn<Persoana, LocalDate> tableColumnBirthDate;
    @FXML private TableColumn<Persoana, String> tableColumnOcupation;
    @FXML private TableColumn<Persoana, Double> tableColumnEmpathyLevel;


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

        var personsList = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(personsList);
        labelPage.setText("Page "+(currentPage+1)+ " of " + (maxPage+1));
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("dataNasterii"));
        tableColumnOcupation.setCellValueFactory(new PropertyValueFactory<>("ocupatie"));
        tableColumnEmpathyLevel.setCellValueFactory(new PropertyValueFactory<>("nivelEmpatie"));

        tableView.setItems(model);
        userFilter.setType("Persoana");
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
}
