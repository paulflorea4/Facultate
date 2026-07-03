using FirmaTransportC_.Network.gRPC;
using FirmaTransportC_.Networking.protocol;
using FirmaTransportC_.Services;
using log4net;
using log4net.Config;
using System.Configuration;
using System.Reflection;
using Transport.Network.Protobuf;

namespace FirmaTransportC_.Client
{
    public class StartClient
    {
        private static int DEFAULT_PORT = 55556;
        private static String DEFAULT_IP = "127.0.0.1";
        private static readonly ILog log = LogManager.GetLogger(typeof(StartClient));

        public static void Main(string[] args)
        {
            var entryAssembly = Assembly.GetEntryAssembly();
            if (entryAssembly == null)
            {
                MessageBox.Show("Failed to get entry assembly.", "Error");
                return;
            }

            var logRepository = LogManager.GetRepository(entryAssembly);
            var configPath = Path.Combine(AppContext.BaseDirectory, "log4net.config");
            XmlConfigurator.Configure(logRepository, new FileInfo(configPath));

            ApplicationConfiguration.Initialize();
            log.Debug("Reading properties from app.config ...");
            int port = DEFAULT_PORT;
            String ip = DEFAULT_IP;
            String? portS = ConfigurationManager.AppSettings["port"];
            if (portS == null)
            {
                log.DebugFormat("Port property not set. Using default value {0}", DEFAULT_PORT);
            }
            else
            {
                bool result = Int32.TryParse(portS, out port);
                if (!result)
                {
                    log.DebugFormat("Port property not a number. Using default value {0}", DEFAULT_PORT);
                    port = DEFAULT_PORT;
                    log.DebugFormat("Portul {0}", port);
                }
            }
            String? ipS = ConfigurationManager.AppSettings["ip"];

            if (ipS == null)
            {
                log.DebugFormat("IP property not set. Using default value {0}", DEFAULT_IP);
            }

            log.InfoFormat("Using gRPC server on IP {0} and port {1}", ip, port);
            ITransportService server = new TransportServicesGrpcProxy(ip, port);

            LoginForm loginForm = new LoginForm(server);
            TripForm tripForm = new TripForm(server);
            OfficeForm officeForm = new OfficeForm(server, tripForm, loginForm);
            loginForm.SetOfficeForm(officeForm);

            Application.Run(loginForm);
        }
    }
}
