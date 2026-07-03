using FirmaTransportC_.Model;
using System;
using System.Collections.Generic;
using System.Text;

namespace FirmaTransportC_.Persistence
{
    public interface IReservationRepository : IRepository<long, Reservation>
    {
        List<Reservation> FindReservationsByClientName(string clientName);
        void MakeReservationTransaction(Reservation reservation, Trip trip, ISeatRepository seatRepository, ITripRepository tripRepository);
        void CancelReservationTransaction(Reservation reservation, ISeatRepository seatRepository, ITripRepository tripRepository);
    }
}
