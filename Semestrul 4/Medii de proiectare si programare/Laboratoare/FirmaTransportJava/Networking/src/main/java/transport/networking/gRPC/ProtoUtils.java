package transport.networking.gRPC;

import transport.protocol.TransportProtobufs;
import java.util.List;

public class ProtoUtils {

    public static TransportProtobufs.TripsResponse toProtoTripsResponse(List<transport.model.Trip> trips) {
        TransportProtobufs.TripsResponse.Builder responseBuilder = TransportProtobufs.TripsResponse.newBuilder();

        if (trips != null) {
            for (transport.model.Trip t : trips) {
                TransportProtobufs.Trip tripProto = TransportProtobufs.Trip.newBuilder()
                        .setId(t.getId())
                        .setDestination(t.getDestination() != null ? t.getDestination() : "")
                        .setDate(t.getDate() != null ? t.getDate() : "")
                        .setHour(t.getHour() != null ? t.getHour() : "")
                        .setAvailableSeats(t.getAvailableSeats())
                        .build();

                responseBuilder.addTrips(tripProto);
            }
        }

        return responseBuilder.build();
    }

    public static transport.model.Trip fromProto(TransportProtobufs.Trip tripProto) {
        return new transport.model.Trip(
                tripProto.getId(),
                tripProto.getDestination(),
                tripProto.getDate(),
                tripProto.getHour(),
                tripProto.getAvailableSeats()
        );
    }

    public static TransportProtobufs.SeatsResponse toProtoSeatsResponse(List<transport.model.Seat> seats) {
        TransportProtobufs.SeatsResponse.Builder responseBuilder = TransportProtobufs.SeatsResponse.newBuilder();

        if (seats != null) {
            for (transport.model.Seat s : seats) {
                responseBuilder.addSeats(TransportProtobufs.Seat.newBuilder()
                        .setId(s.getId())
                        .setNumber(s.getNumber())
                        .setTripId(s.getTripId())
                        .setClientName(s.getClientName() != null ? s.getClientName() : "")
                        .setReservationId(s.getReservationId() != null ? s.getReservationId() : 0L)
                        .build());
            }
        }

        return responseBuilder.build();
    }
}