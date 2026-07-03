using FirmaTransportC_.Model;

namespace FirmaTransportC_.Networking.dto
{
    public static class DTOUtils
    {
        public static Trip GetFromDTO(TripDTO tripDTO)
        {
            return new Trip(
                tripDTO.Id,
                tripDTO.Destination,
                tripDTO.Date,
                tripDTO.Hour,
                tripDTO.AvailableSeats
            );
        }

        public static User GetFromDTO(UserDTO userDTO)
        {
            return new User(userDTO.Id, userDTO.Username, userDTO.Password);
        }

        public static Trip GetFromDTO(SearchDTO searchDTO)
        {
            return new Trip(0, searchDTO.Destination, searchDTO.Date, searchDTO.Hour, 0);
        }

        public static Reservation GetFromDTO(ReservationDTO reservationDTO)
        {
            return new Reservation(
                reservationDTO.Id,
                reservationDTO.ClientName,
                reservationDTO.TripId,
                reservationDTO.NumberOfSeats
            );
        }

        public static Seat GetFromDTO(SeatDTO seatDTO, long tripId)
        {
            return new Seat(
                seatDTO.Id,
                seatDTO.Number,
                seatDTO.ReservationId,
                tripId,
                seatDTO.ClientName
            );
        }

        public static SeatDTO GetDTO(Seat seat)
        {
            return new SeatDTO(seat.Id, seat.Number, seat.TripId, seat.ReservationId, seat.ClientName);
        }

        public static UserDTO GetDTO(User user)
        {
            return new UserDTO(user.Id, user.Username, user.Password);
        }

        public static SearchDTO GetSearchDTO(Trip trip)
        {
            return new SearchDTO(trip.Destination, trip.Date, trip.Hour);
        }

        public static ReservationDTO GetDTO(Reservation reservation)
        {
            return new ReservationDTO(
                reservation.Id,
                reservation.TripId,
                reservation.NumberOfSeats,
                reservation.ClientName
            );
        }

        public static TripDTO GetTripDTO(Trip trip)
        {
            return new TripDTO(
                trip.Id,
                trip.Destination,
                trip.Date,
                trip.Hour,
                trip.AvailableSeats
            );
        }

        public static List<TripDTO> GetDTOList(IEnumerable<Trip> trips)
        {
            return trips.Select(t => GetTripDTO(t)).ToList();
        }

        public static List<SeatDTO> GetDTOList(IEnumerable<Seat> seats)
        {
            return seats.Select(s => GetDTO(s)).ToList();
        }

        public static List<Trip> GetFromDTO(List<TripDTO> trips)
        {
            return trips.Select(t => GetFromDTO(t)).ToList();
        }

        public static List<Seat> GetFromDTO(List<SeatDTO> seats, long tripId)
        {
            return seats.Select(s => GetFromDTO(s, tripId)).ToList();
        }
    }
}
