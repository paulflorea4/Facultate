using FirmaTransportC_.Model;
using FirmaTransportC_.Networking.dto;
using FirmaTransportC_.Services;
using log4net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;

namespace FirmaTransportC_.Networking.protocol
{
    public class TransportServicesProxy : ITransportService
    {
        private string host;
        private int port;

        private IObserver client;
        private NetworkStream stream;
        private TcpClient connection;
        private Queue<Response> responses;
        private volatile bool finished;
        private EventWaitHandle _waitHandle;
        private static readonly ILog log = LogManager.GetLogger(typeof(TransportServicesProxy));

        public TransportServicesProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            responses = new Queue<Response>();
        }

        public void Login(User user, IObserver client)
        {
            initializeConnection();
            sendRequest(JsonProtocolUtils.CreateLoginRequest(user));
            Response response = readResponse();
            if(response.Type == ResponseType.OK)
            {
                this.client = client;
                log.Info("Login successful");
            }

            if(response.Type == ResponseType.ERROR)
            {
                string errorMessage = response.ErrorMessage ?? "Unknown error";
                closeConnection();
                throw new ServiceException("Login failed: " + errorMessage);
            }
        }

        public void Logout(User user)
        {
            sendRequest(JsonProtocolUtils.CreateLogoutRequest(user));
            Response response = readResponse();
            closeConnection();
            if (response.Type == ResponseType.ERROR)
            {
                throw new ServiceException(response.ErrorMessage);
            }
        }

        public List<Trip> GetAllTrips()
        {
            sendRequest(JsonProtocolUtils.CreateGetTripsRequest());
            Response response = readResponse();
            if(response.Type != ResponseType.TRIPS_LIST)
            {
                throw new ServiceException("Unexpected response type: " + response.Type);
            }

            return DTOUtils.GetFromDTO(response.Trips);
        }

        public List<Seat> GetSeatsForTrip(Trip trip)
        {
            sendRequest(JsonProtocolUtils.CreateGetSeatsRequest(trip));
            Response response = readResponse();
            if(response.Type != ResponseType.SEATS_LIST)
            {
                throw new ServiceException("Unexpected response type: " + response.Type);
            }

            return DTOUtils.GetFromDTO(response.Seats, trip.Id);
        }

        public List<Trip> SearchTrips(Trip trip)
        {
            sendRequest(JsonProtocolUtils.CreateSearchTripsRequest(DTOUtils.GetSearchDTO(trip)));
            Response response = readResponse();
            if(response.Type != ResponseType.SEARCH_RESULT)
            {
                throw new ServiceException("Unexpected response type: " + response.Type);
            }

            return DTOUtils.GetFromDTO(response.Trips);
        }

        public void MakeReservation(Reservation reservation)
        {
            sendRequest(JsonProtocolUtils.CreateMakeReservationRequest(reservation));
            Response response = readResponse();
            if(response.Type != ResponseType.OK)
            {
                throw new ServiceException("Unexpected response type: " + response.Type);
            }
        }

        public void CancelReservation(Reservation reservation)
        {
            sendRequest(JsonProtocolUtils.CreateCancelReservationRequest(reservation));
            Response response = readResponse();
            if(response.Type != ResponseType.OK)
            {
                throw new ServiceException("Unexpected response type: " + response.Type);
            }
        }

        private void initializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                stream = connection.GetStream();
                finished = false;
                _waitHandle = new AutoResetEvent(false);
                startReader();
            }
            catch (Exception e)
            {
                log.Error(e);
                throw new ServiceException("Error connecting to server: " + e.Message);
            }
        }

        private void closeConnection()
        {
            finished = true;
            try
            {
                stream.Close();
                connection.Close();
                _waitHandle.Close();
                client = null;
            }
            catch (Exception e)
            {
                log.Error(e);
            }
            finally
            {
                stream = null;
                connection = null;
                _waitHandle = null;
                client = null;
            }
        }

        private void sendRequest(Request request)
        {
            try
            {
                lock (stream)
                {

                    string jsonRequest = JsonSerializer.Serialize(request);
                    log.DebugFormat("Sending request {0}", jsonRequest);
                    byte[] data = Encoding.UTF8.GetBytes(jsonRequest + "\n");
                    stream.Write(data, 0, data.Length);
                    stream.Flush();

                }
            }
            catch (Exception e)
            {
                throw new ServiceException("Error sending request " + e.Message);
            }

        }

        private Response readResponse()
        {
            Response response = null;
            try
            {
                _waitHandle.WaitOne();
                lock (responses)
                {
                    response = responses.Dequeue();

                }

                if(response.ErrorMessage != null)
                {
                    throw new ServiceException("Error response: " + response.ErrorMessage);
                }

                return response;
            }
            catch (Exception e)
            {
                throw new ServiceException("Error reading response " + e.Message);
            }
        }

        private void startReader()
        {
            Thread tw = new Thread(run);
            tw.IsBackground = true;
            tw.Start();
        }

        private void handleUpdate(Response response)
        {
            log.Debug("handling update " + response);
            if (response.Type != ResponseType.UPDATE || client == null)
            {
                return;
            }

            try
            {
                List<Trip> trips = DTOUtils.GetFromDTO(response.Trips);
                client.tripsUpdated(trips);
            }
            catch (Exception e)
            {
                log.Error("Error handling update " + e);
            }
        }   

        private bool isUpdate(Response response)
        {
            return response.Type == ResponseType.UPDATE;
        }

        public virtual void run()
        {
            using StreamReader reader = new StreamReader(stream, Encoding.UTF8);
            while (!finished)
            {
                try
                {
                    string responseJson = reader.ReadLine();
                    if (string.IsNullOrEmpty(responseJson))
                        continue;

                    Response response = JsonSerializer.Deserialize<Response>(responseJson);
                    log.Debug("response received " + response);

                    if (isUpdate(response))
                    {
                        handleUpdate(response);
                    }
                    else
                    {
                        lock (responses)
                        {
                            responses.Enqueue(response);
                        }
                        _waitHandle.Set();
                    }
                }
                catch (IOException) when (finished)
                {
                    break;
                }
                catch (ObjectDisposedException) when (finished)
                {
                    break;
                }
                catch (Exception e)
                {
                    if (!finished)
                    {
                        log.Error("Reading error " + e);
                    }
                    break;
                }
            }
        }

    }
}
