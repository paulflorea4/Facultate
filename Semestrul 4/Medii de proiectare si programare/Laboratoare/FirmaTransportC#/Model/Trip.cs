namespace FirmaTransportC_.Model
{
    public class Trip : Entity<long>
    {
        private string destination;
        private string date;
        private string hour;
        private int availableSeats;
        public Trip(long id, string destination, string date, string hour, int availableSeats) : base(id)
        {
            this.destination = destination;
            this.date = date;
            this.hour = hour;
            this.availableSeats = availableSeats;
        }

        public string Date
        {
            get { return date; }
            set { date = value; }
        }

        public string Hour
        {
            get { return hour; }
            set { hour = value; }
        }

        public string Destination
        {
            get { return destination; }
            set { destination = value; }
        }

        public int AvailableSeats
        {
            get { return availableSeats; }
            set { availableSeats = value; }
        }

        public override string ToString()
        {
            return "Trip{" +
                "id=" + Id +
                ", departure='" + date + " " + hour + '\'' +
                ", destination='" + destination +
                '}';
        }
    }
}