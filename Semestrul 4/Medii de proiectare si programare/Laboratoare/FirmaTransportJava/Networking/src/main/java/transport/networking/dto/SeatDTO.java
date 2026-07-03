package transport.networking.dto;

import java.io.Serializable;

public class SeatDTO implements Serializable {
    private int number;
    private String clientName;

    public SeatDTO(int number, String clientName) {
        this.number = number;
        this.clientName = clientName;
    }

    public int getNumber() {
        return number;
    }

    public String getClientName() {
        return clientName;
    }
}