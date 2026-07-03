using FirmaTransportC_.Model;
using System;
using System.Collections.Generic;
using System.Text;

namespace FirmaTransportC_.Persistence
{
    public interface ISeatRepository : IRepository<long, Seat>
    {
        List<Seat> FindSeatsForTrip(long tripId);
        List<Seat> FindSeatsForReservation(long reservationId);
    }
}
