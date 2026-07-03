using FirmaTransportC_.Model;
using System;
using System.Collections.Generic;
using System.Text;

namespace FirmaTransportC_.Persistence
{
    public interface ITripRepository : IRepository<long, Trip>
    {
        List<Trip> FindTripsByDestinationAndDepartureDate(string destination, string date, string hour);
    }
}
