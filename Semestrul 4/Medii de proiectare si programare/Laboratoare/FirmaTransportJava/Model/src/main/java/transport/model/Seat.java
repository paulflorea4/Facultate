package transport.model;

public class Seat extends Entity<Long>{
    private int number;
    private Long reservationId;
    private Long tripId;
    private String clientName;

    public Seat(Long id, int number, Long reservationId, Long tripId, String clientName) {
        super(id);
        this.number = number;
        this.reservationId = reservationId;
        this.tripId = tripId;
        this.clientName = clientName;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getReservationId() {
        return  reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + getId() +
                ", number=" + number +
                ", reservationId=" + reservationId +
                ", tripId=" + tripId +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}