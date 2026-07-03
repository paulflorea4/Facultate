namespace FirmaTransportC_.Model
{
    public class Reservation : Entity<long>
    {
        private string clientName;
        private long tripId;
        private int numberOfSeats;
        public Reservation(long id, string clientName, long tripId, int numberOfSeats) : base(id)
        {
            this.clientName = clientName;
            this.tripId = tripId;
            this.numberOfSeats = numberOfSeats;
        }
        public string ClientName
        {
            get { return clientName; }
            set { clientName = value; }
        }
        public long TripId
        {
            get { return tripId; }
            set { tripId = value; }
        }

        public int NumberOfSeats
        {
            get { return numberOfSeats; }
            set { numberOfSeats = value; }
        }

        public override string ToString()
        {
            return "Reservation{" +
                "id=" + Id +
                ", clientName=" + clientName +
                ", tripId=" + tripId +
                ", numberOfSeats=" + numberOfSeats +
                '}';
        }
    }
}
