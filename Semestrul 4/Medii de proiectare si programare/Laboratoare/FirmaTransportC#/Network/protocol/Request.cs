using FirmaTransportC_.Networking.dto;

namespace FirmaTransportC_.Networking.protocol
{
    [Serializable]
    public class Request
    {
        public RequestType Type { get; set; }
        public UserDTO? User { get; set; }
        public TripDTO? Trip { get; set; }
        public SearchDTO? SearchParams { get; set; }
        public ReservationDTO? Reservation { get; set; }

        public override string ToString() => $"Request{{Type={Type}, User={User}}}";
    }
}
