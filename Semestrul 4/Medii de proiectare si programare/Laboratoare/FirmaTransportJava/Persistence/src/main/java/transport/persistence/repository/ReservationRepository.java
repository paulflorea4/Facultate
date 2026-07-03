package transport.persistence.repository;

import transport.model.Reservation;
import transport.model.Trip;

public interface ReservationRepository extends IRepository<Long, Reservation> {
    Iterable<Reservation> findReservationsByClientName(String clientName);
    Long makeReservation(Reservation reservation, Trip trip, SeatRepository seatRepository, TripRepository tripRepository);
    void cancelReservationTransaction(Reservation reservation, SeatRepository seatRepository, TripRepository tripRepository);
}
