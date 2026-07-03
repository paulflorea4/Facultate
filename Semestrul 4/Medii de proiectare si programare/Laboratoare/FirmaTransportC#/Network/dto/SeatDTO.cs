using System.Text.Json.Serialization;

namespace FirmaTransportC_.Networking.dto
{
    [Serializable]
    public class SeatDTO
    {
        public long Id { get; set; }
        public int Number { get; set; }
        public long TripId { get; set; }
        public long? ReservationId { get; set; }
        public string? ClientName { get; set; }

        public SeatDTO(long id, int number, long tripId, long? reservationId, string? clientName)
        {
            Id = id;
            Number = number;
            TripId = tripId;
            ReservationId = reservationId;
            ClientName = clientName;
        }

        public SeatDTO()
        {
        }
    }
}
