package transport.server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import transport.networking.gRPC.TransportServiceGrpcImpl;
import transport.persistence.repository.ReservationRepository;
import transport.persistence.repository.SeatRepository;
import transport.persistence.repository.TripRepository;
import transport.persistence.repository.UserRepository;
import transport.persistence.repository.hibernate.TripHibernateRepository;
import transport.persistence.repository.hibernate.UserHibernateRepository;
import transport.persistence.repository.jdbc.ReservationDbRepository;
import transport.persistence.repository.jdbc.SeatDbRepository;
import transport.services.ITransportService;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;
    private static Logger logger = LogManager.getLogger(StartServer.class.getName());

    public static void main(String[] args) {
        Properties serverProps=new Properties();

            try {
                serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
                logger.info("Server properties set {}",serverProps);
            } catch (IOException e) {
                logger.error("Cannot find server.properties "+e);
                logger.debug("Looking for file in "+(new File(".")).getAbsolutePath());
                return;
            }

        UserRepository userRepo=new UserHibernateRepository();
        TripRepository tripRepo=new TripHibernateRepository();
        SeatRepository seatRepo=new SeatDbRepository(serverProps);
        ReservationRepository reservationRepo=new ReservationDbRepository(serverProps);

        ITransportService transportServerImpl =new TransportServiceImpl(userRepo, tripRepo, reservationRepo, seatRepo);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            logger.error("Wrong  Port Number"+nef.getMessage());
            logger.debug("Using default port "+ defaultPort);
        }
        logger.debug("Starting server on port: "+ serverPort);

        try {
            logger.info("Starting gRPC server on port: " + serverPort);

            Server server = ServerBuilder.forPort(serverPort)
                    .addService(new TransportServiceGrpcImpl(transportServerImpl))
                    .build();

            server.start();

            logger.info("gRPC Server started, listening on " + serverPort);

            server.awaitTermination();

        } catch (IOException e) {
            if (isBindConflict(e)) {
                logger.error("Cannot start gRPC server on port {} because the port is already in use.", serverPort);
                logger.error("Stop the other process using this port or change 'server.port' in server.properties.");
                logger.debug("Bind conflict details", e);
            } else {
                logger.error("Error starting the gRPC server: " + e.getMessage(), e);
            }
        } catch (InterruptedException e) {
            logger.error("Server interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static boolean isBindConflict(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof BindException) {
                return true;
            }
            String message = current.getMessage();
            if (message != null) {
                String lower = message.toLowerCase();
                if (lower.contains("failed to bind") || lower.contains("address already in use")) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }
}

