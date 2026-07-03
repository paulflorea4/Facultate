package transport.services;

import transport.model.Reservation;
import transport.model.Seat;
import transport.model.Trip;
import transport.model.User;

import java.util.List;

public interface ITransportService {

    void login(User user, IObserver client) throws ServiceException;

    void logout(User user) throws ServiceException;

    List<Trip> getAllTrips() throws ServiceException;

    List<Trip> searchTrips(Trip trip) throws ServiceException;

    List<Seat> getSeatsForTrip(Trip trip) throws ServiceException;

    void makeReservation(Reservation reservation) throws ServiceException;

    void cancelReservation(Reservation reservation) throws ServiceException;
}
