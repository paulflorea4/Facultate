package transport.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.model.Reservation;
import transport.model.Seat;
import transport.model.Trip;
import transport.model.User;
import transport.persistence.repository.ReservationRepository;
import transport.persistence.repository.SeatRepository;
import transport.persistence.repository.TripRepository;
import transport.persistence.repository.UserRepository;
import transport.persistence.repository.utils.PasswordUtils;
import transport.services.IObserver;
import transport.services.ITransportService;
import transport.services.ServiceException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;

public class TransportServiceImpl implements ITransportService {
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final Map<Long, IObserver> loggedClients;
    private final static Logger logger = LogManager.getLogger(TransportServiceImpl.class);

    public TransportServiceImpl(UserRepository userRepository, TripRepository tripRepository, ReservationRepository reservationRepository, SeatRepository seatRepository) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, IObserver client) throws ServiceException {
        User foundUser = userRepository.findByUsername(user.getUsername());
        logger.info("Found user ID: " + foundUser.getId());
        if (foundUser != null && PasswordUtils.verify(user.getPassword(), foundUser.getPassword())) {
            if(loggedClients.get(foundUser.getId()) != null){
                throw new ServiceException("User already logged in.");
            }

            loggedClients.put(foundUser.getId(), client);
            logger.info("User {} logged in", user.getUsername());
        }else {
            throw new ServiceException("Authentication failed!");
        }
    }

    @Override
    public synchronized void logout(User user) throws ServiceException {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null) {
            throw new ServiceException("User not found");
        }

        IObserver loggedClient = loggedClients.remove(foundUser.getId());
        if (loggedClient == null) {
            throw new ServiceException("User not logged in.");
        }
        logger.info("User {} logged out", user.getUsername());
    }

    @Override
    public synchronized List<Trip> getAllTrips() throws ServiceException {
        try {
            List<Trip> trips = tripRepository.findAll();
            logger.info("Retrieved {} trips", trips.size());
            return trips;
        } catch (Exception e) {
            logger.error("Error retrieving all trips", e);
            throw new ServiceException("Error retrieving trips: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<Trip> searchTrips(Trip trip) throws ServiceException {
        try {
            List<Trip> foundTrips = tripRepository.findTripsByDestinationAndDepartureDate(
                    trip.getDestination(),
                    trip.getDate(),
                    trip.getHour()
            );
            logger.info("Found {} trips matching destination {} and departure {}", foundTrips.size(), trip.getDestination(), trip.getDate() + " " + trip.getHour());
            return foundTrips;
        } catch (Exception e) {
            logger.error("Error searching trips", e);
            throw new ServiceException("Error searching trips: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<Seat> getSeatsForTrip(Trip trip) throws ServiceException {
        try {
            List<Seat> seats = seatRepository.findSeatsForTrip(trip.getId());
            logger.info("Retrieved {} seats for trip {}", seats.size(), trip.getId());
            return seats;
        } catch (Exception e) {
            logger.error("Error retrieving seats for trip {}", trip.getId(), e);
            throw new ServiceException("Error retrieving seats: " + e.getMessage());
        }
    }

    @Override
    public synchronized void makeReservation(Reservation reservation) throws ServiceException {
        try {
            Trip trip = tripRepository.findById(reservation.getTripId());
            if (trip == null) {
                throw new ServiceException("Trip not found with id: " + reservation.getTripId());
            }

            reservationRepository.makeReservation(reservation, trip, seatRepository, tripRepository);

            notifyClientsUpdate();
            logger.info("Reservation created for user {} on trip {}", reservation.getClientName(), reservation.getTripId());
        } catch (Exception e) {
            logger.error("Error making reservation", e);
            throw new ServiceException("Error making reservation: " + e.getMessage());
        }
    }

    @Override
    public synchronized void cancelReservation(Reservation reservation) throws ServiceException {
        try {
            reservationRepository.cancelReservationTransaction(reservation, seatRepository, tripRepository);

            notifyClientsUpdate();
            logger.info("Reservation cancelled for trip {}", reservation.getTripId());
        } catch (Exception e) {
            logger.error("Error cancelling reservation", e);
            throw new ServiceException("Error cancelling reservation: " + e.getMessage());
        }
    }

    private void notifyClientsUpdate() {
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<Trip> updatedTrips = getAllTrips();

            for (IObserver observer : loggedClients.values()) {
                executor.submit(() -> {
                    try {
                        observer.tripsUpdated(updatedTrips);
                        logger.debug("Notified observer of trip updates");
                    } catch (Exception e) {
                        logger.error("Error notifying observer", e);
                    }
                });
            }
        } catch (Exception e) {
            logger.error("Error fetching trips for notification", e);
        }
    }
}

