package transport.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import transport.model.Reservation;
import transport.model.Seat;
import transport.model.Trip;
import transport.services.ITransportService;
import transport.services.ServiceException;

import java.util.List;

public class TripController {

    private ITransportService service;
    private Trip trip;

    private final ObservableList<Seat> model = FXCollections.observableArrayList();

    @FXML private TableView<Seat> seatsTable;
    @FXML private TableColumn<Seat, Integer> seatNumberColumn;
    @FXML private TableColumn<Seat, String> clientNameColumn;

    @FXML private TextField numberOfSeatsField;
    @FXML private TextField clientNameField;

    @FXML private Label tripLabel;

    @FXML
    private void initialize() {
        seatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        seatsTable.setItems(model);
    }

    public void init(ITransportService service, Trip trip) {
        this.service = service;
        this.trip = trip;
        initModel();
    }

    public void onTripsUpdated(List<Trip> trips) {
        if (trip == null || service == null || trips == null) {
            return;
        }

        Trip updatedTrip = trips.stream()
                .filter(t -> t.getId() != null && t.getId().equals(trip.getId()))
                .findFirst()
                .orElse(null);

        if (updatedTrip != null) {
            this.trip = updatedTrip;
            initModel();
        }
    }

    private void initModel() {
        try {
            List<Seat> seats = service.getSeatsForTrip(trip);
            model.setAll(seats);
            tripLabel.setText(String.format("Trip to %s on %s at %s", trip.getDestination(), trip.getDate(), trip.getHour()));
        } catch (ServiceException e) {
            showError("Could not load seats", e.getMessage());
        }
    }

    public void onAddReservation() {
        try {
            String clientName = clientNameField.getText();
            int numberOfSeats = Integer.parseInt(numberOfSeatsField.getText());

            if (clientName == null || clientName.isBlank() || numberOfSeats <= 0) {
                showError("Invalid input", "Please provide a valid client name and a positive number of seats.");
                return;
            }

            service.makeReservation(new Reservation(null, clientName.trim(), trip.getId(), numberOfSeats));
            clientNameField.clear();
            numberOfSeatsField.clear();
            initModel();
        } catch (NumberFormatException e) {
            showError("Invalid input", "Number of seats must be a positive integer.");
        } catch (ServiceException e) {
            showError("Could not add reservation", e.getMessage());
        }
    }

    public void onCancelReservation() {
        Seat selectedSeat = seatsTable.getSelectionModel().getSelectedItem();
        if (selectedSeat == null) {
            showError("No Selection", "Please select a reserved seat to cancel.");
            return;
        }

        if (selectedSeat.getReservationId() == null) {
            showError("Invalid Selection", "The selected seat is not reserved.");
            return;
        }

        try {
            service.cancelReservation(new Reservation(selectedSeat.getReservationId(), selectedSeat.getClientName(), selectedSeat.getTripId(), 1));
            initModel();
        } catch (ServiceException e) {
            showError("Could not cancel reservation", e.getMessage());
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
