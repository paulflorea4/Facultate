package transport.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import transport.model.Trip;
import transport.model.User;
import transport.services.IObserver;
import transport.services.ITransportService;
import transport.services.ServiceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OfficeController implements IObserver {
    private ITransportService service;
    private Stage loginStage;
    private Stage officeStage;
    private Stage tripStage;
    private TripController tripController;
    private User currentUser;
    private final ObservableList<Trip> model = FXCollections.observableArrayList();

    @FXML private TableView<Trip> tripsTable;
    @FXML private TableColumn<Trip, String> tripsDestinationColumn;
    @FXML private TableColumn<Trip, String> tripsDateColumn;
    @FXML private TableColumn<Trip, String> tripsHourColumn;
    @FXML private TableColumn<Trip, Integer> tripsAvailableSeatsColumn;
    @FXML private DatePicker tripDatePicker;
    @FXML private TextField tripHourField;
    @FXML private TextField destinationField;

    @FXML
    public void initialize() {
        tripsDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        tripsDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tripsHourColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
        tripsAvailableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        tripsTable.setItems(model);
        tripsTable.setRowFactory(tableView -> {
            TableRow<Trip> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    openTripView(row.getItem());
                }
            });
            return row;
        });
    }

    public void init(ITransportService service, Stage loginStage, Stage officeStage, Stage tripStage, TripController tripController) {
        this.service = service;
        this.loginStage = loginStage;
        this.officeStage = officeStage;
        this.tripStage = tripStage;
        this.tripController = tripController;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void loadTrips() throws ServiceException {
        model.setAll(service.getAllTrips());
    }

    private void initModel() {
        try {
            loadTrips();
        } catch (ServiceException e) {
            showError("Could not load trips", e.getMessage());
        }
    }

    @FXML
    public void onSearchButton() {
        LocalDate selectedDate = tripDatePicker.getValue();
        String hourInput = tripHourField.getText();
        String destination = destinationField.getText();

        if (selectedDate == null || destination == null || destination.isBlank() || hourInput == null || hourInput.isBlank()) {
            showError("Invalid input", "Please select a date, destination and hour.");
            return;
        }

        try {
            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String normalizedHour = LocalTime.parse(hourInput.trim(), hourFormatter).format(hourFormatter);
            String normalizedDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

            Trip filter = new Trip(null, destination.trim(), normalizedDate, normalizedHour, 0);
            model.setAll(service.searchTrips(filter));
        } catch (Exception e) {
            showError("Invalid input", "Hour must be in HH:mm format.");
        }
    }

    @FXML
    public void onResetButton() {
        tripDatePicker.setValue(null);
        tripHourField.clear();
        destinationField.clear();
        initModel();
    }

    @FXML
    public void onLogoutButton() {
        if (currentUser == null) {
            showError("Logout Failed", "No logged in user found.");
            return;
        }

        try {
            service.logout(currentUser);
            currentUser = null;

            if (tripStage != null) {
                tripStage.hide();
            }
            if (officeStage != null) {
                officeStage.hide();
            }
            if (loginStage != null) {
                loginStage.show();
            }

            model.clear();
            tripDatePicker.setValue(null);
            tripHourField.clear();
            destinationField.clear();
        } catch (ServiceException e) {
            showError("Could not logout", e.getMessage());
        }
    }

    @Override
    public void tripsUpdated(List<Trip> trips) throws ServiceException {
        Platform.runLater(() -> {
            model.setAll(trips);
            if (tripController != null && tripStage != null && tripStage.isShowing()) {
                tripController.onTripsUpdated(trips);
            }
        });
    }

    private void openTripView(Trip selectedTrip) {
        if (tripController == null || tripStage == null) {
            showError("Navigation Error", "Trip window is not configured.");
            return;
        }

        tripController.init(service, selectedTrip);
        if (!tripStage.isShowing()) {
            tripStage.show();
        } else {
            tripStage.toFront();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
