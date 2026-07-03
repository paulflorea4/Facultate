package Server;

import Networking.server.JocRpcConcurrentServer;
import Services.IJocServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartServer {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        int port = 55555;
        Properties props = new Properties();

        try (InputStream is = StartServer.class.getResourceAsStream("/server.properties")) {
            if (is != null) {
                props.load(is);
                port = Integer.parseInt(props.getProperty("server.port", "55555"));
                logger.info("Configuration loaded: PORT={}", port);
            } else {
                logger.warn("server.properties not found, using default configuration.");
            }
        } catch (IOException | NumberFormatException e) {
            logger.error("Error loading configuration: ", e);
        }

        IJocServices service = JocServiceFactory.getInstance();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Oprire server - eliberare resurse...");
            service.close();
        }));

        JocRpcConcurrentServer server = new JocRpcConcurrentServer(port, service);
        logger.info("Pornire server pe portul {}", port);
        server.start();
    }
}
