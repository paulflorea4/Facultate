package Client.rpc;

import Domain.Configuratie;
import Domain.Joc;
import Domain.Jucator;
import Services.IJocObserver;
import Services.IJocServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IJocServices {
    private final String host;
    private final int port;
    private IJocObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private final BlockingQueue<Response> qresponses = new LinkedBlockingQueue<>();
    private Thread readerThread;

    private static final Logger logger = LogManager.getLogger();

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Jucator login(String alias, IJocObserver client) throws Exception {
        this.client = client;
        initializeConnection();
        sendRequest(new Request(RequestType.LOGIN, alias));
        Response response = qresponses.take();
        if (response.getType() == ResponseType.ERROR) {
            closeConnection();
            throw new Exception((String) response.getData());
               }
        if (response.getType() != ResponseType.OK) {
            closeConnection();
            throw new Exception("Răspuns invalid la login: " + response.getType());
        }
        return (Jucator) response.getData();
    }

    @Override
    public void logout(Jucator jucator, IJocObserver client) throws Exception {
        sendRequest(new Request(RequestType.LOGOUT, jucator));
        Response response = qresponses.take();
        closeConnection();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception((String) response.getData());
        }
    }

    @Override
    public Configuratie incepeJoc() throws Exception {
        sendRequest(new Request(RequestType.INCEPE_JOC, null));
        Response response = qresponses.take();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception((String) response.getData());
        }
        return (Configuratie) response.getData();
    }

    @Override
    public void salveazaJoc(Jucator jucator, Configuratie configuratie, int punctaj, int durataSauIncercari) throws Exception {
        Object[] data = new Object[]{jucator, configuratie, punctaj, durataSauIncercari};
        sendRequest(new Request(RequestType.SALVEAZA_JOC, data));
        Response response = qresponses.take();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception((String) response.getData());
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public Iterable<Joc> getClasament() throws Exception {
        sendRequest(new Request(RequestType.GET_CLASAMENT, null));
        Response response = qresponses.take();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception((String) response.getData());
        }
        return (Iterable<Joc>) response.getData();
    }

    @Override
    public void alegeVarianta(Configuratie configuratie) throws Exception {
        sendRequest(new Request(RequestType.ALEGE, configuratie));
        Response response = qresponses.take();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception((String) response.getData());
        }
    }

    @Override
    public void close() {
        closeConnection();
    }

    private void initializeConnection() throws IOException {
        connection = new Socket(host, port);
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        startReaderThread();
        logger.info("Conexiune stabilită cu {}:{}", host, port);
    }

    private void closeConnection() {
        try {
            if (readerThread != null) readerThread.interrupt();
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
            logger.info("Conexiune închisă.");
        } catch (IOException e) {
            logger.error("Eroare la închiderea conexiunii: {}", e.getMessage());
        }
    }

    private synchronized void sendRequest(Request request) throws IOException {
        output.writeObject(request);
        output.flush();
    }

    private void startReaderThread() {
        readerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Response response = (Response) input.readObject();
                    logger.info("Răspuns primit: {}", response);
                    if (isUpdate(response.getType())) {
                        handleUpdate(response);
                    } else {
                        qresponses.put(response);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Eroare citire răspuns: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        readerThread.setDaemon(true);
        readerThread.start();
    }

    private boolean isUpdate(ResponseType type) {
        return type == ResponseType.CLASAMENT_ACTUALIZAT
                || type == ResponseType.JOC_PORNIT
                || type == ResponseType.INCEPE_RUNDA
                || type == ResponseType.ALES;
    }

    @SuppressWarnings("unchecked")
    private void handleUpdate(Response response) {
        new Thread(() -> {
            try {
                switch (response.getType()) {
                    case CLASAMENT_ACTUALIZAT:
                        client.clasamentActualizat();
                        break;
                    case JOC_PORNIT:
                        Object[] gameData = (Object[]) response.getData();
                        List<Configuratie> configuratii = (List<Configuratie>) gameData[0];
                        String mesaj = (String) gameData[1];
                        client.jocPornit(configuratii, mesaj);
                        break;
                    case INCEPE_RUNDA:
                        client.incepeRunda((String) response.getData());
                        break;
                    case ALES:
                        client.variantaAleasa((Configuratie) response.getData());
                        break;
                    default:
                        logger.warn("Notificare necunoscută: {}", response.getType());
                        break;
                }
            } catch (Exception e) {
                logger.error("Eroare la procesarea notificării {}: {}", response.getType(), e.getMessage());
            }
        }).start();
    }
}
