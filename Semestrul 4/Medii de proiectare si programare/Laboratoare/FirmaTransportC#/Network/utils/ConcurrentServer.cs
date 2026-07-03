using log4net;
using System.Net.Sockets;
using System.Threading;

namespace FirmaTransportC_.Networking.utils
{
    public abstract class ConcurrentServer : AbstractServer
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(ConcurrentServer));

        public ConcurrentServer(string host, int port) : base(host, port)
        { }

        public override void processRequest(TcpClient client)
        {

            Thread t = createWorker(client);
            t.Start();

        }

        protected abstract Thread createWorker(TcpClient client);
    }
}
