package transport.networking.dto;

import java.io.Serializable;

public class TripDTO implements Serializable {
    private Long id;
    private String destination;
    private String date;
    private String hour;
    private int availableSeats;

    public TripDTO(Long id, String destination, String date, String hour, int availableSeats) {
        this.id = id;
        this.destination = destination;
        this.date = date;
        this.hour = hour;
        this.availableSeats = availableSeats;
    }

    public Long getId() { return id; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public String getHour() { return hour; }
    public int getAvailableSeats() { return availableSeats; }
}