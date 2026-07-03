using FirmaTransportC_.Model;
using FirmaTransportC_.Networking.dto;
using FirmaTransportC_.Services;
using log4net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;

namespace FirmaTransportC_.Networking.protocol
{
    public class TransportClientWorker : IObserver
    {
        private ITransportService server;
        private TcpClient connection;

        private NetworkStream stream;

        private volatile bool connected;

        private static readonly ILog log = LogManager.GetLogger("TransportClientWorker");

        public TransportClientWorker(ITransportService server, TcpClient connection)
        {
            this.server = server;
            this.connection = connection;
            try
            {
                stream = connection.GetStream();
                connected = true;
            }
            catch (Exception e)
            {
                log.Error("Error getting stream: " + e.Message);
            }
        }

        public virtual void run()
        {
            using StreamReader reader = new StreamReader(stream, Encoding.UTF8);
            while (connected)
            {
                try
                {
                    string requestJson = reader.ReadLine();

                    if (string.IsNullOrEmpty(requestJson))
                        continue;

                    log.DebugFormat("Received json request {0}", requestJson);

                    Request request = JsonSerializer.Deserialize<Request>(requestJson);

                    log.DebugFormat("Deserializaed Request {0} ", request);

                    Response response = handleRequest(request);

                    if (response != null)
                    {
                        sendResponse(response);
                    }
                }
                catch (Exception e)
                {
                    log.ErrorFormat("run eroare {0}", e.Message);
                    if (e.InnerException != null)
                        log.ErrorFormat("run inner error {0}", e.InnerException.Message);
                    log.Error(e.StackTrace);
                }

                try
                {
                    Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    log.Error(e.StackTrace);
                }
            }
            try
            {
                stream.Close();
                connection.Close();
            }
            catch (Exception e)
            {
                log.Error("Error " + e);
            }
        }

        private Response handleRequest(Request request)
        {
            try
            {
                switch (request.Type)
                {
                    case RequestType.LOGIN:
                        {
                            log.Debug("Login request received");
                            User user = DTOUtils.GetFromDTO(request.User);
                            lock (server)
                            {
                                server.Login(user, this);
                            }
                            return JsonProtocolUtils.CreateLoginResponse(user);
                        }
                    case RequestType.LOGOUT:
                        {
                            log.Debug("Logout request received");
                            User user = DTOUtils.GetFromDTO(request.User);
                            lock (server)
                            {
                                server.Logout(user);
                            }
                            connected = false;
                            return JsonProtocolUtils.CreateOkResponse();
                        }
                    case RequestType.GET_TRIPS:
                        {
                            log.Debug("Get trips request received");
                            List<Trip> trips;
                            lock (server)
                            {
                                trips = server.GetAllTrips();
                            }
                            return JsonProtocolUtils.CreateGetTripsResponse(trips);
                        }
                    case RequestType.SEARCH_TRIPS:
                        {
                            log.Debug("Search trips request received");
                            Trip search = DTOUtils.GetFromDTO(request.SearchParams);
                            List<Trip> trips;
                            lock (server)
                            {
                                trips = server.SearchTrips(search);
                            }

                            return JsonProtocolUtils.CreateSearchTripsResponse(trips);
                        }
                    case RequestType.GET_SEATS_FOR_TRIP:
                        {
                            log.Debug("Get seats for trip request received");
                            Trip trip = DTOUtils.GetFromDTO(request.Trip);
                            List<Seat> seats;
                            lock (server)
                            {
                                seats = server.GetSeatsForTrip(trip);
                            }

                            return JsonProtocolUtils.CreateGetSeatsResponse(seats);
                        }
                    case RequestType.MAKE_RESERVATION:
                        {
                            log.Debug("Make reservation request received");
                            Reservation reservation = DTOUtils.GetFromDTO(request.Reservation);
                            lock (server)
                            {
                                server.MakeReservation(reservation);
                            }
                            return JsonProtocolUtils.CreateOkResponse();
                        }
                    case RequestType.CANCEL_RESERVATION:
                        {
                            log.Debug("Cancel reservation request received");
                            Reservation reservation = DTOUtils.GetFromDTO(request.Reservation);
                            lock (server)
                            {
                                server.CancelReservation(reservation);
                            }
                            return new Response { Type = ResponseType.OK };
                        }
                }
                return new Response { Type = ResponseType.ERROR, ErrorMessage = "Unknown request type" };

            }
            catch (Exception e)
            {
                log.Error("Error handling request: " + e.Message);
                return new Response { Type = ResponseType.ERROR, ErrorMessage = e.Message };
            }
        }

        private void sendResponse(Response response)
        {
            try
            {
                string jsonString = JsonSerializer.Serialize(response);
                log.DebugFormat("Sending response {0}", jsonString);
                lock (stream)
                {
                    byte[] data = Encoding.UTF8.GetBytes(jsonString + "\n");
                    stream.Write(data, 0, data.Length);
                    stream.Flush();
                }
            }
            catch (Exception e)
            {
                log.Error("Error sending response: " + e.Message);
            }
        }

        public void tripsUpdated(List<Trip> trips)
        {
            try
            {
                sendResponse(JsonProtocolUtils.CreateTripsUpdatedResponse(trips));
            }
            catch (Exception e)
            {
                {
                    log.Error("Error sending trips updated response: " + e.Message);
                }
            }
        }
    }
}
