package Controller;

import Domain.Configuratie;
import Domain.Joc;
import Domain.Jucator;
import Services.IJocObserver;
import Services.IJocServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainController implements IJocObserver {

    @FXML private ComboBox<Configuratie> configuratiiComboBox;
    @FXML private Label labelAlias;
    @FXML private Label labelInfo1;
    @FXML private Label labelInfo2;
    @FXML private Label labelInfo3;
    @FXML private Label gameBoard;
    @FXML private Button btnAlegeVarianta;

    @FXML private TableView<Joc> tableClasament;
    @FXML private TableColumn<Joc, String> colAlias;
    @FXML private TableColumn<Joc, String> colPunctaj;
    @FXML private TableColumn<Joc, String> colDurata;

    private IJocServices service;
    private Jucator jucator;
    private Stage currentStage;
    private Stage loginStage;

    private Configuratie config;
    private int punctaj = 0;
    private int incercari = 0;

    @FXML
    private void initialize() {
        colAlias.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getJucator().getAlias()));
        colPunctaj.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getPunctaj())));
        colDurata.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getDurataSauIncercari())));
    }

    public void initData(IJocServices service, Jucator jucator,
                         Stage currentStage, Stage loginStage) {
        this.service      = service;
        this.jucator      = jucator;
        this.currentStage = currentStage;
        this.loginStage   = loginStage;

        punctaj = 0;

        labelAlias.setText("Jucător: " + jucator.getAlias());
        labelInfo1.setText("Punctaj: 0");
        labelInfo2.setText("Stare: Jucatori insuficienti...");

        gameBoard.setText("");
        loadClasament();
    }

    @FXML
    public void handleLogout() {
        try {
            service.logout(jucator, this);
        } catch (Exception e) {
            MessageAlert.showMessage(currentStage, Alert.AlertType.ERROR,
                    "Eroare logout", e.getMessage());
        }
        currentStage.close();
        loginStage.show();
    }

    @Override
    public void clasamentActualizat() throws Exception {
        Platform.runLater(() -> {
            try {
                Iterable<Joc> clasament = service.getClasament();
                List<Joc> list = new ArrayList<>();
                clasament.forEach(list::add);
                tableClasament.getItems().setAll(list);
            } catch (Exception e) {
                System.err.println("Eroare reîncărcare clasament: " + e.getMessage());
            }
        });
    }

    @Override
    public void jocPornit(List<Configuratie> configuratii, String mesaj) throws Exception {
        Platform.runLater(() -> {
            setStatusMessage(mesaj);
            labelInfo1.setText("Punctaj: 0");
            labelInfo2.setText("Stare: Se alege configuratie");
            gameBoard.setText("");

            configuratiiComboBox.getItems().clear();
            configuratiiComboBox.setItems(FXCollections.observableArrayList(configuratii));
        });
    }

    @Override
    public void incepeRunda(String jucatorActiv) throws Exception {
        Platform.runLater(() -> {
            if (this.jucator == null) {
                setStatusMessage("Jocul pornește...");
                if (btnAlegeVarianta != null) btnAlegeVarianta.setDisable(true);
                return;
            }
            if (this.jucator.getAlias().equals(jucatorActiv)) {
                setStatusMessage("E rândul tău să alegi!");
                if (btnAlegeVarianta != null) btnAlegeVarianta.setDisable(false);
            } else {
                setStatusMessage("Așteptăm ca " + jucatorActiv + " să aleagă...");
                if (btnAlegeVarianta != null) btnAlegeVarianta.setDisable(true);
            }
        });
    }

    @Override
    public void variantaAleasa(Configuratie configuratie) throws Exception {
        Platform.runLater(() -> {
            if (gameBoard != null) {gameBoard.setText("Configuratie aleasa: " + configuratie.getValori()); this.config = configuratie; }
            setStatusMessage("Configuratie aleasă!");
            if (btnAlegeVarianta != null) btnAlegeVarianta.setDisable(true);

            configuratiiComboBox.getItems().clear();
            configuratiiComboBox.setDisable(true);
            configuratiiComboBox.setVisible(false);
        });
    }

    @FXML
    public void handleAlegeVarianta() {
        try {
            Configuratie configuratie = configuratiiComboBox.getSelectionModel().getSelectedItem();
            if (configuratie != null) {
                service.alegeVarianta(configuratie);
            }

        } catch (Exception e) {
            MessageAlert.showMessage(currentStage, Alert.AlertType.ERROR,
                    "Eroare generare literă", e.getMessage());
        }
    }

    public void setStatusMessage(String message) {
        labelInfo3.setText("Stare: " + message);
    }

    private void loadClasament() {
        try {
            Iterable<Joc> clasament = service.getClasament();
            List<Joc> list = new ArrayList<>();
            clasament.forEach(list::add);
            tableClasament.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            MessageAlert.showMessage(currentStage, Alert.AlertType.ERROR,
                    "Eroare clasament", e.getMessage());
        }
    }
}
