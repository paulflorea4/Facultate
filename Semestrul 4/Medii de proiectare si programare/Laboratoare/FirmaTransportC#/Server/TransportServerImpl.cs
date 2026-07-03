using FirmaTransportC_.Model;
using FirmaTransportC_.Persistence;
using FirmaTransportC_.Persistence.utils;
using FirmaTransportC_.Services;
using log4net;
using System.Runtime.InteropServices;

namespace FirmaTransportC_.Server
{
    public class TransportServerImpl : ITransportService
    {
        private IUserRepository userRepository;
        private ITripRepository tripRepository;
        private IReservationRepository reservationRepository;
        private ISeatRepository seatRepository;
        private readonly IDictionary<long, IObserver> loggedClients;
        private static readonly ILog log = LogManager.GetLogger(typeof(TransportServerImpl));

        public TransportServerImpl(IUserRepository userRepository, ITripRepository tripRepository, IReservationRepository reservationRepository, ISeatRepository seatRepository)
        {
            this.userRepository = userRepository;
            this.tripRepository = tripRepository;
            this.reservationRepository = reservationRepository;
            this.seatRepository = seatRepository;
            loggedClients = new Dictionary<long, IObserver>();
        }

        public void Login(User user, IObserver client)
        {
            User foundUser = userRepository.FindByUsername(user.Username);
            if (foundUser != null && PasswordUtils.Verify(user.Password, foundUser.Password))
            {
                if (loggedClients.ContainsKey(foundUser.Id))
                {
                    throw new ServiceException("User already logged in.");
                }
                loggedClients[foundUser.Id] = client;
                log.InfoFormat("User {0} logged in.", user.Username);
            }
            else
            {
                throw new ServiceException("Invalid username or password.");
            }
        }

        public void Logout(User user)
        {
            User foundUser = userRepository.FindByUsername(user.Username);
            if (foundUser != null && loggedClients.ContainsKey(foundUser.Id))
            {
                if (!loggedClients.Remove(foundUser.Id))
                {
                    throw new ServiceException("Error during logout.");
                }
                log.InfoFormat("User {0} logged out.", user.Username);
            }
            else
            {
                throw new ServiceException("User not logged in.");
            }
        }

        public List<Trip> GetAllTrips()
        {
            try
            {
                List<Trip> trips = tripRepository.FindAll();
                log.Info("Fetched all trips. Count: " + trips.Count);
                return trips;
            }
            catch (Exception ex)
            {
                log.Error("Error fetching trips: " + ex.Message);
                throw new ServiceException("Error fetching trips.");
            }
        }

        public List<Trip> SearchTrips(Trip trip)
        {
            try
            {
                List<Trip> trips = tripRepository.FindTripsByDestinationAndDepartureDate(trip.Destination, trip.Date, trip.Hour);
                log.InfoFormat("Found {0} trips with destination '{1}' and date '{2}'. Found: {3}", trips.Count, trip.Destination, trip.Date, trips.Count);
                return trips;

            }
            catch (Exception ex)
            {
                log.Error("Error searching trips: " + ex.Message);
                throw new ServiceException("Error searching trips.");
            }
        }

        public List<Seat> GetSeatsForTrip(Trip trip)
        {
            try
            {
                List<Seat> seats = seatRepository.FindSeatsForTrip(trip.Id);
                log.InfoFormat("Fetched {0} seats for trip with ID {1}.", seats.Count, trip.Id);
                return seats;

            }
            catch (Exception ex)
            {
                log.Error("Error fetching seats for trip: " + ex.Message);
                throw new ServiceException("Error fetching seats for trip.");
            }
        }

        public void MakeReservation(Reservation reservation)
        {
            try
            {
                Trip trip = tripRepository.FindOne(reservation.TripId);
                if(trip == null)
                {
                    throw new ServiceException("Trip not found.");
                }

                reservationRepository.MakeReservationTransaction(reservation, trip, seatRepository, tripRepository);
                NotifyClients();
                log.InfoFormat("Reservation made for trip ID {0} by client '{1}' for {2} seats.", reservation.TripId, reservation.ClientName, reservation.NumberOfSeats);
            }
            catch (Exception ex)
            {
                log.Error("Error making reservation: " + ex.Message);
                throw new ServiceException("Error making reservation.");
            }
        }

        public void CancelReservation(Reservation reservation)
        {
            try
            {
                reservationRepository.CancelReservationTransaction(reservation, seatRepository, tripRepository);
                NotifyClients();
                log.InfoFormat("Reservation cancelled for trip ID {0} by client '{1}' for {2} seats.", reservation.TripId, reservation.ClientName, reservation.NumberOfSeats);

            }
            catch (Exception ex)
            {
                log.Error("Error cancelling reservation: " + ex.Message);
                throw new ServiceException("Error cancelling reservation.");
            }
        }

        private void NotifyClients()
        {
            List<Trip> trips = tripRepository.FindAll();
            foreach (var client in loggedClients.Values)
            {
                Task.Run(() => { 
                    try
                    {
                        client.tripsUpdated(trips);
                    }
                    catch (Exception ex)
                    {
                        log.Error("Error notifying client: " + ex.Message);
                    }
                });
            }
        }
    }
}
