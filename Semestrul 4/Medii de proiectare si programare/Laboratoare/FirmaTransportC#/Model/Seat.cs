namespace FirmaTransportC_.Model
{
    public class Seat : Entity<long>
    {
        private int number;
        private long? reservationId;
        private long tripId;
        private string? clientName;
        public Seat(long id, int number, long? reservationId, long tripId, string? clientName) : base(id)
        {
            this.number = number;
            this.reservationId = reservationId;
            this.tripId = tripId;
            this.clientName = clientName;
        }

        public int Number
        {
            get { return number; }
            set { number = value; }
        }

        public long? ReservationId
        {
            get { return reservationId; }
            set { reservationId = value; }
        }

        public long TripId
        {
            get { return tripId; }
            set { tripId = value; }
        }

        public string? ClientName
        {
            get { return clientName; }
            set { clientName = value; }
        }

        public override string ToString()
        {
            return "Seat{" +
                "id=" + Id +
                ", number=" + number +
                ", reservationId=" + reservationId +
                ", tripId=" + tripId +
                '}';
        }
    }
}