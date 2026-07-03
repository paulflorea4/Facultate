package org.example.laboratorGUI.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.Page;
import org.example.laboratorGUI.utils.types.TipRata;

import java.util.List;

public class DuckController {
    @FXML private TableView<Duck> tableView;

    @FXML private TableColumn<Duck, Long> tableColumnID;
    @FXML private TableColumn<Duck, String> tableColumnUsername;
    @FXML private TableColumn<Duck, TipRata> tableColumnType;
    @FXML private TableColumn<Duck, Double> tableColumnSpeed;
    @FXML private TableColumn<Duck, Double> tableColumnResistance;
    @FXML private TableColumn<Duck, Long> tableColumnFlockID;

    @FXML private Button prevBtn;
    @FXML private Button nextBtn;
    @FXML private ComboBox<String> filterBox;
    private final String filterAll = "All";

    private UserService userSrv;

    private int currentPage = 0;

    private final ObservableList<Duck> ducks = FXCollections.observableArrayList();

    public void initializeNecessaryServices(UserService userSrv){
        this.userSrv = userSrv;
        loadDucks();
    }

    @FXML private void initialize(){
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        tableColumnResistance.setCellValueFactory(new PropertyValueFactory<>("resistance"));
        tableColumnFlockID.setCellValueFactory(new PropertyValueFactory<>("flockID"));

        tableView.setItems(ducks);

        prevBtn.setOnAction(event -> onPrevious());
        nextBtn.setOnAction(event -> onNext());

        filterBox.getItems().add(filterAll);
        for (TipRata t : TipRata.values())
            filterBox.getItems().add(t.name());
        filterBox.getSelectionModel().select(0);
        filterBox.setOnAction(event -> loadDucks());
    }

    private void loadDucks(){
        String selectedType = filterBox.getValue();
        if(selectedType.equals(filterAll))
            loadPage();
        else
            loadPageByDuckType(TipRata.valueOf(selectedType));
    }

    private void loadPage() {
        Page<Duck> page = userSrv.getDuckPage(currentPage);
        ducks.setAll((List<Duck>) page.getContent());

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable(currentPage + 1 >= page.getTotalPages());
    }

    private void loadPageByDuckType(TipRata type) {
        Page<Duck> page = userSrv.getDuckPageByType(currentPage, type);
        ducks.setAll((List<Duck>) page.getContent());

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable(currentPage + 1 >= page.getTotalPages());
    }

    private void onNext(){
        currentPage++;
        loadDucks();
    }

    private void onPrevious(){
        currentPage--;
        loadDucks();
    }
}