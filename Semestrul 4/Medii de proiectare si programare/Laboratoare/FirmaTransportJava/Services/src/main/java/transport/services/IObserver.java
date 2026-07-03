package transport.services;

import transport.model.Trip;

import java.util.List;

public interface IObserver {
    void tripsUpdated(List<Trip> trips) throws ServiceException;
}


