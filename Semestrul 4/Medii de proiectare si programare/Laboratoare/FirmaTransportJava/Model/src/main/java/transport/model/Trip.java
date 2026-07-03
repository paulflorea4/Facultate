package transport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trips")
public class Trip extends transport.model.Entity<Long>{
    @Column(name = "destination", nullable = false)
    private String destination;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "hour")
    private String hour;
    @Column(name = "available_seats")
    private int availableSeats;

    public Trip(Long id, String destination, String date, String hour, int availableSeats) {
        super(id);
        this.destination = destination;
        this.date = date;
        this.hour = hour;
        this.availableSeats = availableSeats;
    }

    public Trip() {
        super(null);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + getId() +
                ", destination='" + destination + '\'' +
                ", departure=" + date + " : " + hour +
                '}';
    }
}
