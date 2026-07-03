package transport.networking.protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.model.Seat;
import transport.model.Trip;
import transport.networking.dto.DTOUtils;
import transport.networking.dto.ReservationDTO;
import transport.networking.dto.SearchDTO;
import transport.networking.dto.TripDTO;
import transport.networking.dto.UserDTO;
import transport.services.IObserver;
import transport.services.ITransportService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class TransportClientWorker implements Runnable, IObserver {

    private final ITransportService server;
    private final Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private volatile boolean connected;

    private static final Logger logger = LogManager.getLogger(TransportClientWorker.class);

    public TransportClientWorker(ITransportService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            logger.error("Error initializing client worker", e);
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (Exception e) {
                logger.error("Error while handling request", e);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Worker interrupted", e);
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error closing client worker resources", e);
        }
    }

    private Response handleRequest(Request request) {
        try {
            switch (request.getType()) {
                case LOGIN:
                    server.login(DTOUtils.getFromDTO((UserDTO) request.getData()), this);
                    return new Response(ResponseType.OK, null);

                case LOGOUT:
                    server.logout(DTOUtils.getFromDTO((UserDTO) request.getData()));
                    connected = false;
                    return new Response(ResponseType.OK, null);

                case GET_TRIPS:
                    List<Trip> trips = server.getAllTrips();
                    return new Response(ResponseType.TRIPS_LIST, trips);

                case SEARCH_TRIPS:
                    List<Trip> filteredTrips = server.searchTrips(DTOUtils.getFromDTO((SearchDTO) request.getData()));
                    return new Response(ResponseType.SEARCH_RESULT, filteredTrips);

                case GET_SEATS_FOR_TRIP:
                    List<Seat> seats = server.getSeatsForTrip(DTOUtils.getFromDTO((TripDTO) request.getData()));
                    return new Response(ResponseType.SEATS_LIST, seats);

                case MAKE_RESERVATION:
                    server.makeReservation(DTOUtils.getFromDTO((ReservationDTO) request.getData()));
                    return new Response(ResponseType.OK, null);

                case CANCEL_RESERVATION:
                    server.cancelReservation(DTOUtils.getFromDTO((ReservationDTO) request.getData()));
                    return new Response(ResponseType.OK, null);

                default:
                    return new Response(ResponseType.ERROR, "Unknown request type");
            }
        } catch (Exception e) {
            return new Response(ResponseType.ERROR, e.getMessage());
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.debug("Sending response {}", response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void tripsUpdated(List<Trip> trips) {
        try {
            sendResponse(new Response(ResponseType.UPDATE, trips));
        } catch (IOException e) {
            logger.error("Error sending update", e);
        }
    }
}