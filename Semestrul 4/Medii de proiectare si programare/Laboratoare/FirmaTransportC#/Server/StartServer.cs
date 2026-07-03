using FirmaTransportC_.Networking.utils;
using FirmaTransportC_.Persistence;
using FirmaTransportC_.Persistence.ado;
using log4net;
using log4net.Config;
using System.Configuration;
using System.Net.Sockets;
using System.Reflection;

namespace FirmaTransportC_.Server
{
    class StartServer

    {
        private static int DEFAULT_PORT = 55556;
        private static String DEFAULT_IP = "127.0.0.1";
        private static readonly ILog log = LogManager.GetLogger(typeof(StartServer));

        static void Main(string[] args)
        {

            var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
            var configPath = Path.Combine(AppContext.BaseDirectory, "log4net.config");
            XmlConfigurator.Configure(logRepository, new FileInfo(configPath));

            log.Info("Starting transport server");
            log.Info("Reading properties from app.config ...");
            int port = DEFAULT_PORT;
            String ip = DEFAULT_IP;
            String portS = ConfigurationManager.AppSettings["port"];
            if (portS == null)
            {
                log.Debug("Port property not set. Using default value " + DEFAULT_PORT);
            }
            else
            {
                bool result = Int32.TryParse(portS, out port);
                if (!result)
                {
                    log.Debug("Port property not a number. Using default value " + DEFAULT_PORT);
                    port = DEFAULT_PORT;
                    log.Debug("Port " + port);
                }
            }
            String ipS = ConfigurationManager.AppSettings["ip"];

            if (ipS == null)
            {
                log.Info("Port property not set. Using default value " + DEFAULT_IP);
            }

            log.InfoFormat("Configuration Settings for database {0}", GetConnectionString());

            var props = new Dictionary<string, string>
            {
                { "ConnectionString", GetConnectionString() }
            };

            IUserRepository userRepository = new UserDbRepository(props);
            ITripRepository tripRepository = new TripDbRepository(props);
            ISeatRepository seatRepository = new SeatDbRepository(props);
            IReservationRepository reservationRepository = new ReservationDbRepository(props);

            TransportServerImpl serviceImpl = new TransportServerImpl(userRepository, tripRepository, reservationRepository, seatRepository);

            log.DebugFormat("Starting server on IP {0} and port {1}", ip, port);
            JsonTransportServer server = new JsonTransportServer(ip, port, serviceImpl);
            server.Start();
            log.Debug("Server started ...");
            Console.ReadLine();

        }

        static string GetConnectionString()
        {
            return ConfigurationManager.AppSettings["ConnectionString"];
        }
    }
}
