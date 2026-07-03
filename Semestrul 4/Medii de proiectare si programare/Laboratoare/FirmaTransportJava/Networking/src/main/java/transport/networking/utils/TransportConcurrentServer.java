package transport.networking.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import transport.networking.protocol.TransportClientWorker;
import transport.services.ITransportService;

import java.net.Socket;

public class TransportConcurrentServer extends AbstractConcurrentServer{
    private ITransportService server;
    private static Logger logger = LogManager.getLogger(AbstractServer.class);

    public TransportConcurrentServer(int port, ITransportService server) {
        super(port);
        this.server = server;
        logger.debug("Transport Concurrent Server created");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TransportClientWorker worker = new TransportClientWorker(server, client);

        return new Thread(worker);
    }

    @Override
    public void stop() throws ServerException {
        super.stop();
        logger.debug("Transport Concurrent Server stopped");
    }
}
