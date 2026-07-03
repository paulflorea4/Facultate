package transport.persistence.repository;

import transport.model.Trip;

import java.util.List;

public interface TripRepository extends IRepository<Long, Trip> {
    List<Trip> findTripsByDestinationAndDepartureDate(String destination, String date, String hour);
}
