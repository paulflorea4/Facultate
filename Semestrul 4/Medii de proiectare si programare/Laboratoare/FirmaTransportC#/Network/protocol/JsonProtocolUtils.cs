using FirmaTransportC_.Model;
using FirmaTransportC_.Networking.dto;

namespace FirmaTransportC_.Networking.protocol
{
    public class JsonProtocolUtils
    {

        public static Response CreateOkResponse()
        {
            return new Response { Type = ResponseType.OK };
        }

        public static Response CreateLoginResponse(User user)
        {
            return new Response { Type = ResponseType.OK, User = DTOUtils.GetDTO(user) };
        }

        public static Response CreateErrorResponse(string errorMessage)
        {
            return new Response { Type = ResponseType.ERROR, ErrorMessage = errorMessage };
        }

        public static Response CreateGetTripsResponse(List<Trip> trips)
        {
            return new Response
            {
                Type = ResponseType.TRIPS_LIST,
                Trips = DTOUtils.GetDTOList(trips)
            };
        }

        public static Response CreateGetSeatsResponse(List<Seat> seats)
        {
            return new Response
            {
                Type = ResponseType.SEATS_LIST,
                Seats = DTOUtils.GetDTOList(seats)
            };
        }

        public static Response CreateUpdateResponse(List<Trip> trips)
        {
            return new Response
            {
                Type = ResponseType.UPDATE,
                Trips = DTOUtils.GetDTOList(trips)
            };
        }

        public static Response CreateSearchTripsResponse(List<Trip> trips)
        {
            return new Response
            {
                Type = ResponseType.SEARCH_RESULT,
                Trips = DTOUtils.GetDTOList(trips)
            };
        }

        public static Response CreateTripsUpdatedResponse(List<Trip> trips)
        {
            return new Response
            {
                Type = ResponseType.UPDATE,
                Trips = DTOUtils.GetDTOList(trips)
            };
        }

        public static Request CreateLoginRequest(User user)
        {
            return new Request { Type = RequestType.LOGIN, User = DTOUtils.GetDTO(user) };
        }

        public static Request CreateLogoutRequest(User user)
        {
            return new Request { Type = RequestType.LOGOUT, User = DTOUtils.GetDTO(user) };
        }

        public static Request CreateGetTripsRequest()
        {
            return new Request { Type = RequestType.GET_TRIPS };
        }

        public static Request CreateSearchTripsRequest(SearchDTO searchParams)
        {
            return new Request { Type = RequestType.SEARCH_TRIPS, SearchParams = searchParams };
        }

        public static Request CreateGetSeatsRequest(Trip trip)
        {
            return new Request { Type = RequestType.GET_SEATS_FOR_TRIP, Trip = DTOUtils.GetTripDTO(trip) };
        }

        public static Request CreateMakeReservationRequest(Reservation reservation)
        {
            return new Request
            {
                Type = RequestType.MAKE_RESERVATION,
                Reservation = DTOUtils.GetDTO(reservation)
            };
        }

        public static Request CreateCancelReservationRequest(Reservation reservation)
        {
            return new Request
            {
                Type = RequestType.CANCEL_RESERVATION,
                Reservation = DTOUtils.GetDTO(reservation)
            };
        }
    }
}
