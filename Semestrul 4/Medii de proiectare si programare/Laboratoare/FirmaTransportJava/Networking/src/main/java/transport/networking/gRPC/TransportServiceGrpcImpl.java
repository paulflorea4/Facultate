package transport.networking.gRPC;

import io.grpc.stub.StreamObserver;
import transport.protocol.TransportProtobufs;
import transport.protocol.TransportServiceGrpc;
import transport.services.IObserver;
import transport.services.ITransportService;
import transport.protocol.TransportProtobufs.*;
import transport.services.ServiceException;

import java.util.List;

public class TransportServiceGrpcImpl extends TransportServiceGrpc.TransportServiceImplBase {
    private final ITransportService server;

    public TransportServiceGrpcImpl(ITransportService server) {
        this.server = server;
    }

    @Override
    public void login(User request, StreamObserver<TripsResponse> responseObserver) {
        transport.model.User user = new transport.model.User(0L, request.getUsername(), request.getPassword());

        IObserver observerAdapter = trips -> {
            try {
                responseObserver.onNext(ProtoUtils.toProtoTripsResponse(trips));
            } catch (Exception e) {
                System.err.println("Error notifying client: " + e.getMessage());
            }
        };

        try {
            server.login(user, observerAdapter);

            List<transport.model.Trip> trips = server.getAllTrips();
            responseObserver.onNext(ProtoUtils.toProtoTripsResponse(trips));


        } catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void logout(User request, StreamObserver<Empty> responseObserver) {
        try{
            transport.model.User user = new transport.model.User(request.getId(), request.getUsername(), "");
            server.logout(user);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        }  catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void getAllTrips(Empty request, StreamObserver<TripsResponse>  responseObserver) {
        try {
            List<transport.model.Trip> trips = server.getAllTrips();
            responseObserver.onNext(ProtoUtils.toProtoTripsResponse(trips));
            responseObserver.onCompleted();
        }  catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void searchTrips(Trip request, StreamObserver<TripsResponse> responseObserver) {
        try {
            transport.model.Trip searchCriteria = ProtoUtils.fromProto(request);
            List<transport.model.Trip> trips = server.searchTrips(searchCriteria);
            responseObserver.onNext(ProtoUtils.toProtoTripsResponse(trips));
            responseObserver.onCompleted();
        }  catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void getSeatsForTrip(Trip request, StreamObserver<SeatsResponse> responseObserver) {
        try {
            transport.model.Trip trip = ProtoUtils.fromProto(request);
            List<transport.model.Seat> seats = server.getSeatsForTrip(trip);
            responseObserver.onNext(ProtoUtils.toProtoSeatsResponse(seats));
            responseObserver.onCompleted();
        } catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void makeReservation(TransportProtobufs.Reservation request, StreamObserver<TransportProtobufs.Empty> responseObserver) {
        try {
            transport.model.Reservation res = new transport.model.Reservation(
                    request.getId(), request.getClientName(), request.getTripId(), request.getNumberOfSeats()
            );

            server.makeReservation(res);

            responseObserver.onNext(TransportProtobufs.Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void cancelReservation(TransportProtobufs.Reservation request, StreamObserver<TransportProtobufs.Empty> responseObserver) {
        try {
            transport.model.Reservation reservation = new transport.model.Reservation(
                    request.getId(),
                    request.getClientName(),
                    request.getTripId(),
                    request.getNumberOfSeats()
            );

            server.cancelReservation(reservation);
            responseObserver.onNext(TransportProtobufs.Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (ServiceException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }
}
