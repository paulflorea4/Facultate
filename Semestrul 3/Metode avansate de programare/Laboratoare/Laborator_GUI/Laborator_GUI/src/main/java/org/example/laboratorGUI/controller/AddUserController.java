package org.example.laboratorGUI.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.laboratorGUI.domain.flock.Flock;
import org.example.laboratorGUI.service.FlockService;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.types.TipRata;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AddUserController {
    @FXML private GridPane personGrid;
    @FXML private GridPane duckGrid;

    @FXML private RadioButton changeToPersonBtn;
    @FXML private RadioButton changeToDuckBtn;
    @FXML private Button addUserBtn;

    @FXML private TextField usernameTxt;
    @FXML private TextField emailTxt;
    @FXML private TextField passwordTxt;

    @FXML private TextField surnameTxt;
    @FXML private TextField nameTxt;
    @FXML private TextField jobTxt;
    @FXML private DatePicker dobDatePicker;
    @FXML private Slider empathySlider;

    @FXML private Spinner<Double> speedSpinner;
    @FXML private Spinner<Double> resistanceSpinner;
    @FXML private ComboBox<TipRata> duckTypeCombobox;
    @FXML private ComboBox<String> flocksCombobox;

    private UserService userSrv;
    private FlockService flockSrv;
    private final ObservableList<String> flocks = FXCollections.observableArrayList();

    public void initializeNecessaryServices(UserService userSrv, FlockService flockSrv){
        this.userSrv = userSrv;
        this.flockSrv = flockSrv;
    }

    @FXML private void initialize(){
        changeToPersonBtn.setOnAction(event -> showPersonGrid());
        changeToDuckBtn.setOnAction(event -> showDuckGrid());

        SpinnerValueFactory.DoubleSpinnerValueFactory speedValueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0, 0.1);
        speedSpinner.setValueFactory(speedValueFactory);
        SpinnerValueFactory.DoubleSpinnerValueFactory resistanceValueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0, 0.1);
        resistanceSpinner.setValueFactory(resistanceValueFactory);

        duckTypeCombobox.setItems(FXCollections.observableArrayList(TipRata.values()));
        flocksCombobox.setItems(flocks);

        addUserBtn.setOnAction(event -> addUser());
    }

    private void showPersonGrid(){
        personGrid.setOpacity(1);
        personGrid.setDisable(false);

        duckGrid.setOpacity(0);
        duckGrid.setDisable(true);
        changeToDuckBtn.setSelected(false);
    }

    private void showDuckGrid(){
        duckGrid.setOpacity(1);
        duckGrid.setDisable(false);
        loadFlocks();

        personGrid.setOpacity(0);
        personGrid.setDisable(true);
        changeToPersonBtn.setSelected(false);
    }

    private void loadFlocks() {
        List<String> flocks = flockSrv.getAllFlocks()
                .stream()
                .map(Flock::getName)
                .toList();
        this.flocks.setAll(flocks);
    }

    private void addUser() {
        try {
            String username = usernameTxt.getText();
            String email = emailTxt.getText();
            String password = passwordTxt.getText();
            if (changeToPersonBtn.isSelected())
                addPerson(username, email, password);
            else
                addDuck(username, email, password);
            clearFields();
            MessageBox.showInformationMessage("Success!", "User added.");
        } catch (RuntimeException exception) {
            MessageBox.showErrorMessage("Could not add user:\n" + exception.getMessage());
        }
    }

    private void addPerson(String username, String email, String password) {
        String surname = surnameTxt.getText();
        String name = nameTxt.getText();
        String job = jobTxt.getText();
        LocalDate dob = dobDatePicker.getValue();
        double empathy = empathySlider.getValue();
        userSrv.addPerson(1L, username, email, password, surname, name, dob, job, empathy);
    }

    private void addDuck(String username, String email, String password) {
        double speed = speedSpinner.getValue();
        double resistance = resistanceSpinner.getValue();
        TipRata type = duckTypeCombobox.getValue();
        String flockName = flocksCombobox.getValue();
        Optional<Flock<?>> flock = flockSrv.getAllFlocks()
                .stream()
                .filter(flockTemp -> flockTemp.getName().equals(flockName))
                .findFirst();
        long flockID = flock.isPresent() ? flock.get().getFlockID() : 0;
        userSrv.addDuck(1L, username, email, password, type, speed, resistance, flockID);
    }

    private void clearFields() {
        usernameTxt.clear();
        emailTxt.clear();
        passwordTxt.clear();
        if (changeToPersonBtn.isSelected())
            clearPersonFields();
        else
            clearDuckFields();
    }

    private void clearPersonFields() {
        surnameTxt.clear();
        nameTxt.clear();
        jobTxt.clear();
        dobDatePicker.setValue(null);
        empathySlider.setValue(0.0);
        changeToPersonBtn.setSelected(false);
        personGrid.setOpacity(0);
    }

    private void clearDuckFields() {
        speedSpinner.getValueFactory().setValue(0.0);
        speedSpinner.getEditor().setText("");
        resistanceSpinner.getValueFactory().setValue(0.0);
        resistanceSpinner.getEditor().setText("");
        duckTypeCombobox.getSelectionModel().clearSelection();
        flocksCombobox.getSelectionModel().clearSelection();
        changeToPersonBtn.setSelected(false);
        duckGrid.setOpacity(0);
    }
}
