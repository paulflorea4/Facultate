using FirmaTransportC_.Networking.protocol;
using FirmaTransportC_.Services;
using log4net;
using System.Net.Sockets;
using System.Threading;

namespace FirmaTransportC_.Networking.utils
{
    public class JsonTransportServer : ConcurrentServer
    {
        private ITransportService server;
        private TransportClientWorker worker;
        private static readonly ILog log = LogManager.GetLogger(typeof(JsonTransportServer));
        public JsonTransportServer(string host, int port, ITransportService server) : base(host, port)
        {
            this.server = server;
            log.Debug("Creating JsonTransportServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new TransportClientWorker(server, client);
            return new Thread(worker.run);
        }
    }
}
