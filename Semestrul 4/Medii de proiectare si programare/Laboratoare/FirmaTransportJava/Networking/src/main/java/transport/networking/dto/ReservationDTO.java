package transport.networking.dto;

import java.io.Serializable;

public class ReservationDTO implements Serializable {
    private Long id;
    private String clientName;
    private Long tripId;
    private int numberOfSeats;

    public ReservationDTO(Long id, String clientName, Long tripId, int numberOfSeats) {
        this.id = id;
        this.clientName = clientName;
        this.tripId = tripId;
        this.numberOfSeats = numberOfSeats;
    }

    public Long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public Long getTripId() {
        return tripId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}