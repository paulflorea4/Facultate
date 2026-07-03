using System.Text.Json.Serialization;

namespace FirmaTransportC_.Networking.dto
{
    [Serializable]
    public class ReservationDTO
    {
        public long Id { get; set; }
        public long TripId { get; set; }
        public int NumberOfSeats { get; set; }
        public string ClientName { get; set; }

        public ReservationDTO(long id, long tripId, int numberOfSeats, string clientName)
        {
            Id = id;
            TripId = tripId;
            NumberOfSeats = numberOfSeats;
            ClientName = clientName;
        }

        public ReservationDTO()
        {
        }
    }
}
