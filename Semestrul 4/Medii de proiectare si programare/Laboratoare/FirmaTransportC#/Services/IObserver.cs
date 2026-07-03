using FirmaTransportC_.Model;

namespace FirmaTransportC_.Services
{
    public interface IObserver
    {
        void tripsUpdated(List<Trip> trips);
    }
}
