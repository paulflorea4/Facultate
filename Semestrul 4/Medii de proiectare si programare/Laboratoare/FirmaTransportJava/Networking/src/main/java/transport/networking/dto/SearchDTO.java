package transport.networking.dto;

import java.io.Serializable;

public class SearchDTO implements Serializable {
    private String destination;
    private String date;
    private String hour;

    public SearchDTO(String destination, String date, String hour) {
        this.destination = destination;
        this.date = date;
        this.hour = hour;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }
}