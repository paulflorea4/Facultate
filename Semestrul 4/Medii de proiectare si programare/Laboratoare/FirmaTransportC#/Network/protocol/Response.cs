using FirmaTransportC_.Networking.dto;

namespace FirmaTransportC_.Networking.protocol
{
    public class Response
    {
        public ResponseType Type { get; set; }
        public string? ErrorMessage { get; set; }

        public UserDTO? User { get; set; }
        public List<TripDTO>? Trips { get; set; }
        public List<SeatDTO>? Seats { get; set; }

        public override string ToString() => $"Response{{Type={Type}, Error='{ErrorMessage}'}}";
    }
}
