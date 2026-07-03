using System.Text.Json.Serialization;

namespace FirmaTransportC_.Networking.dto
{
    [Serializable]
    public class SearchDTO
    {
        public string Destination { get; set; }
        public string Date { get; set; }
        public string Hour { get; set; }

        public SearchDTO(string destination, string date, string hour)
        {
            Destination = destination;
            Date = date;
            Hour = hour;
        }

        public SearchDTO()
        {
        }
    }
}
