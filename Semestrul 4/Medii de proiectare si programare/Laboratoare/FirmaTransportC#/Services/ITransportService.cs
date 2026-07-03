using FirmaTransportC_.Model;

namespace FirmaTransportC_.Services
{
    public interface ITransportService
    {
        void Login(User user, IObserver client);
        void Logout(User user);
        List <Trip> GetAllTrips();
        List<Trip> SearchTrips(Trip trip);
        List<Seat> GetSeatsForTrip(Trip trip);
        void MakeReservation(Reservation reservation);
        void CancelReservation(Reservation reservation);
    }
}
