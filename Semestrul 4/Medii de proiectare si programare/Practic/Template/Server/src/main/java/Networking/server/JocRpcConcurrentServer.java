package Networking.server;

import Services.IJocServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JocRpcConcurrentServer {
    private final int port;
    private final IJocServices service;
    private static final int THREAD_POOL_SIZE = 10;
    private static final Logger logger = LogManager.getLogger();

    public JocRpcConcurrentServer(int port, IJocServices service) {
        this.port = port;
        this.service = service;
    }

    public void start() {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server RPC pornit pe portul {}", port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client nou conectat: {}", clientSocket.getInetAddress());
                JocClientRpcWorker worker = new JocClientRpcWorker(service, clientSocket);
                threadPool.execute(worker);
            }
        } catch (IOException e) {
            logger.error("Eroare server: {}", e.getMessage());
        } finally {
            threadPool.shutdown();
            logger.info("Thread pool oprit.");
        }
    }
}
