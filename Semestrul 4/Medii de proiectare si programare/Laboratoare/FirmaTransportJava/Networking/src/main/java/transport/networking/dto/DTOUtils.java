package transport.networking.dto;

import transport.model.Reservation;
import transport.model.Seat;
import transport.model.Trip;
import transport.model.User;

public class DTOUtils {

    public static Trip getFromDTO(TripDTO tripDTO) {
        return new Trip(
                tripDTO.getId(),
                tripDTO.getDestination(),
                tripDTO.getDate(),
                tripDTO.getHour(),
                tripDTO.getAvailableSeats()
        );
    }

    public static User getFromDTO(UserDTO userDTO) {
        return new User(null, userDTO.getUsername(), userDTO.getPassword());
    }

    public static Trip getFromDTO(SearchDTO searchDTO) {
        return new Trip(null, searchDTO.getDestination(), searchDTO.getDate(), searchDTO.getHour(), 0);
    }

    public static Reservation getFromDTO(ReservationDTO reservationDTO) {
        return new Reservation(reservationDTO.getId(), reservationDTO.getClientName(), reservationDTO.getTripId(), reservationDTO.getNumberOfSeats());
    }

    public static Seat getFromDTO(SeatDTO seatDTO, Long tripId) {
        return new Seat(null, seatDTO.getNumber(), null, tripId, seatDTO.getClientName());
    }

    public static SeatDTO getDTO(Seat seat) {
        return new SeatDTO(seat.getNumber(), seat.getClientName());
    }

    public static UserDTO getDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }

    public static SearchDTO getSearchDTO(Trip trip) {
        return new SearchDTO(trip.getDestination(), trip.getDate(), trip.getHour());
    }

    public static ReservationDTO getDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getId(), reservation.getClientName(), reservation.getTripId(), reservation.getNumberOfSeats());
    }

    public static TripDTO getTripDTO(Trip trip) {
        return new TripDTO(trip.getId(), trip.getDestination(), trip.getDate(), trip.getHour(), trip.getAvailableSeats());
    }
}