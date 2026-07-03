using System.Text.Json.Serialization;

namespace FirmaTransportC_.Networking.dto
{
    [Serializable]
    public class TripDTO
    {
        public long Id { get; set; }
        public string Destination { get; set; }
        public string Date { get; set; }
        public string Hour { get; set; }
        public int AvailableSeats { get; set; }
        
        public TripDTO(long id, string destination, string date, string hour, int availableSeats)
        {
            Id = id;
            Destination = destination;
            Date = date;
            Hour = hour;
            AvailableSeats = availableSeats;
        }

        public TripDTO()
        {
        }
    }
}
