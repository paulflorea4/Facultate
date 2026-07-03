using FirmaTransportC_.Services;
using Grpc.Core;
using Grpc.Net.Client;
using Transport.Network.Protobuf;

namespace FirmaTransportC_.Network.gRPC
{
    public class TransportServicesGrpcProxy : ITransportService
    {
        private readonly string host;
        private readonly int port;
        private TransportService.TransportServiceClient client;
        private GrpcChannel channel;
        private IObserver observer;
        private CancellationTokenSource cts;

        public TransportServicesGrpcProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
        }

        public void Login(Model.User user, IObserver client)
        {
            channel = GrpcChannel.ForAddress($"http://{host}:{port}");
            this.client = new TransportService.TransportServiceClient(channel);

            var request = new User
            {
                Username = user.Username,
                Password = user.Password
            };

            try
            {
                var stream = this.client.login(request);

                var reader = stream.ResponseStream;
                if (reader.MoveNext(CancellationToken.None).Result)
                {
                    this.observer = client;
                    cts = new CancellationTokenSource();
                    this.observer.tripsUpdated(MapProtoToModel(reader.Current.Trips));

                    Task.Run(() => ListenForUpdates(stream, cts.Token));
                }
            }
            catch (Exception e)
            {
                throw new ServiceException("Login failed: " + e.Message);
            }
        }

        public void Logout(Model.User user)
        {
            try
            {
                if (cts != null)
                {
                    cts.Cancel();
                    cts.Dispose();
                    cts = null;
                }

                var request = new User
                {
                    Id = user.Id,
                    Username = user.Username
                };
                client.logout(request);

                if (channel != null)
                {
                    channel.ShutdownAsync().Wait();
                    channel = null;
                }

                this.observer = null;
            }
            catch (Exception e)
            {
                throw new ServiceException("Logout failed: " + e.Message);
            }
        }

        public List<Model.Trip> GetAllTrips()
        {
            try
            {
                var response = client.getAllTrips(new Empty());
                return MapProtoToModel(response.Trips);
            }
            catch (Exception e)
            {
                throw new ServiceException("Error getting trips: " + e.Message, e);
            }
        }

        public List<Model.Trip> SearchTrips(Model.Trip filter)
        {
            try
            {
                var request = new Trip
                {
                    Destination = filter.Destination,
                    Date = filter.Date,
                    Hour = filter.Hour
                };
                var response = client.searchTrips(request);
                return MapProtoToModel(response.Trips);
            }
            catch (Exception e)
            {
                throw new ServiceException("Error searching trips", e);
            }
        }

        public void MakeReservation(Model.Reservation res)
        {
            try
            {
                var request = new Reservation
                {
                    ClientName = res.ClientName,
                    TripId = res.TripId,
                    NumberOfSeats = res.NumberOfSeats
                };
                client.makeReservation(request);
            }
            catch (Exception e)
            {
                throw new ServiceException("Reservation failed", e);
            }
        }

        public void CancelReservation(Model.Reservation res)
        {
            try
            {
                var request = new Reservation
                {
                    Id = res.Id,
                    TripId = res.TripId,
                    ClientName = res.ClientName,
                    NumberOfSeats = res.NumberOfSeats

                };
                client.cancelReservation(request);
            }
            catch (Exception e)
            {
                throw new ServiceException("Cancel failed: " + res.ToString(), e);
            }
        }

        public List<Model.Seat> GetSeatsForTrip(Model.Trip trip)
        {
            try
            {
                var request = new Trip { Id = trip.Id };
                var response = client.getSeatsForTrip(request);
                return MapProtoToSeats(response.Seats);
            }
            catch (Exception e)
            {
                throw new ServiceException("Error getting seats", e);
            }
        }

        private async Task ListenForUpdates(AsyncServerStreamingCall<TripsResponse> stream, CancellationToken token)
        {
            try
            {
                await foreach (var response in stream.ResponseStream.ReadAllAsync(token))
                {
                    var trips = MapProtoToModel(response.Trips);

                    observer.tripsUpdated(trips);
                }
            }
            catch (RpcException ex) when (ex.StatusCode == StatusCode.Cancelled)
            {
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error in update stream: " + ex.Message);
            }
        }

        private List<Model.Trip> MapProtoToModel(IEnumerable<Trip> protoTrips)
        {
            var list = new List<Model.Trip>();
            foreach (var t in protoTrips)
            {
                list.Add(new Model.Trip(
                    t.Id,
                    t.Destination,
                    t.Date,
                    t.Hour,
                    t.AvailableSeats
                ));
            }
            return list;
        }

        private List<Model.Seat> MapProtoToSeats(IEnumerable<Seat> protoSeats)
        {
            var list = new List<Model.Seat>();
            foreach (var s in protoSeats)
            {
                list.Add(new Model.Seat(
                    s.Id,
                    s.Number,
                    s.ReservationId == 0 ? null : (long?)s.ReservationId,
                    s.TripId,
                    s.ClientName  
                ));
            }
            return list;
        }
    }
}
