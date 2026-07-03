package transport.networking.protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.model.Reservation;
import transport.model.Seat;
import transport.model.Trip;
import transport.model.User;
import transport.networking.dto.DTOUtils;
import transport.networking.dto.ReservationDTO;
import transport.networking.dto.SearchDTO;
import transport.networking.dto.TripDTO;
import transport.networking.dto.UserDTO;
import transport.services.IObserver;
import transport.services.ITransportService;
import transport.services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransportServicesProxy implements ITransportService {

    private final String host;
    private final int port;
    private static final Logger logger = LogManager.getLogger(TransportServicesProxy.class);

    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TransportServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingQueue<>();
    }

    private void initializeConnection() throws ServiceException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new ServiceException("Error initializing connection: " + e.getMessage());
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            logger.error("Error closing connection", e);
        } finally {
            client = null;
            input = null;
            output = null;
            connection = null;
        }
    }

    private void sendRequest(Request request) throws ServiceException {
        if (output == null) {
            throw new ServiceException("Not connected to server");
        }
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending request: " + e.getMessage());
        }
    }

    private Response readResponse() throws ServiceException {
        try {
            Response response = qresponses.take();
            if (response.getType() == ResponseType.ERROR) {
                throw new ServiceException(String.valueOf(response.getData()));
            }
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException("Interrupted while waiting for server response");
        }
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.setDaemon(true);
        thread.start();
    }

    @SuppressWarnings("unchecked")
    private void handleUpdate(Response response) {
        if (response.getType() != ResponseType.UPDATE || client == null) {
            return;
        }

        try {
            List<Trip> trips = (List<Trip>) response.getData();
            client.tripsUpdated(trips);
        } catch (Exception e) {
            logger.error("Error dispatching update to observer", e);
        }
    }

    @Override
    public void login(User user, IObserver client) throws ServiceException {
        initializeConnection();

        UserDTO userDTO = DTOUtils.getDTO(user);
        sendRequest(new Request(RequestType.LOGIN, userDTO));

        Response response = readResponse();
        if (response.getType() != ResponseType.OK) {
            closeConnection();
            throw new ServiceException("Unexpected response type on login: " + response.getType());
        }

        this.client = client;
    }

    @Override
    public void logout(User user) throws ServiceException {
        try {
            UserDTO userDTO = DTOUtils.getDTO(user);
            sendRequest(new Request(RequestType.LOGOUT, userDTO));
            readResponse();
        } finally {
            closeConnection();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Trip> getAllTrips() throws ServiceException {
        sendRequest(new Request(RequestType.GET_TRIPS, null));
        Response response = readResponse();

        if (response.getType() != ResponseType.TRIPS_LIST) {
            throw new ServiceException("Unexpected response type for trips: " + response.getType());
        }

        return (List<Trip>) response.getData();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Trip> searchTrips(Trip trip) throws ServiceException {
        SearchDTO search = DTOUtils.getSearchDTO(trip);
        sendRequest(new Request(RequestType.SEARCH_TRIPS, search));
        Response response = readResponse();

        if (response.getType() != ResponseType.SEARCH_RESULT) {
            throw new ServiceException("Unexpected response type for search: " + response.getType());
        }

        return (List<Trip>) response.getData();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Seat> getSeatsForTrip(Trip trip) throws ServiceException {
        TripDTO tripDTO = DTOUtils.getTripDTO(trip);
        sendRequest(new Request(RequestType.GET_SEATS_FOR_TRIP, tripDTO));
        Response response = readResponse();

        if (response.getType() != ResponseType.SEATS_LIST) {
            throw new ServiceException("Unexpected response type for seats: " + response.getType());
        }

        return (List<Seat>) response.getData();
    }

    @Override
    public void makeReservation(Reservation reservation) throws ServiceException {
        ReservationDTO reservationDTO = DTOUtils.getDTO(reservation);
        sendRequest(new Request(RequestType.MAKE_RESERVATION, reservationDTO));

        Response response = readResponse();
        if (response.getType() != ResponseType.OK) {
            throw new ServiceException("Unexpected response type for reservation: " + response.getType());
        }
    }

    @Override
    public void cancelReservation(Reservation reservation) throws ServiceException {
        ReservationDTO reservationDTO = DTOUtils.getDTO(reservation);
        sendRequest(new Request(RequestType.CANCEL_RESERVATION, reservationDTO));

        Response response = readResponse();
        if (response.getType() != ResponseType.OK) {
            throw new ServiceException("Unexpected response type for cancel: " + response.getType());
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.UPDATE;
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    Response castResponse = (Response) response;
                    if (isUpdate(castResponse)) {
                        handleUpdate(castResponse);
                    } else {
                        qresponses.put(castResponse);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (!finished) {
                        logger.error("Reader error", e);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Reader interrupted", e);
                    return;
                }
            }
        }
    }
}