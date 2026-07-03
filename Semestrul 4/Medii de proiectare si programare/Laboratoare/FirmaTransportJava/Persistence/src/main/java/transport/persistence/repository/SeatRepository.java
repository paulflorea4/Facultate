package transport.persistence.repository;

import transport.model.Seat;

import java.util.List;

public interface SeatRepository extends IRepository<Long, Seat> {
    List<Seat> findSeatsForTrip(Long tripId);
    List<Seat> findSeatsForReservation(Long reservationId);
    int countAvailableSeatsForTrip(Long tripId);
}
