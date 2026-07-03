package transport.model;

public class Reservation extends Entity<Long>{
    private String clientName;
    private Long tripId;
    private int numberOfSeats;

    public Reservation(Long id, String clientName, Long tripId, int numberOfSeats) {
        super(id);
        this.clientName = clientName;
        this.tripId = tripId;
        this.numberOfSeats = numberOfSeats;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
