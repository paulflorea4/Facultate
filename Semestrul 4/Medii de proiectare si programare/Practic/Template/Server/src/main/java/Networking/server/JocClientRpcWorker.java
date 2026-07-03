package Networking.server;

import Domain.Configuratie;
import Domain.Cuvant;
import Domain.Joc;
import Domain.Jucator;
import Client.rpc.Request;
import Client.rpc.Response;
import Client.rpc.ResponseType;
import Services.IJocObserver;
import Services.IJocServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class JocClientRpcWorker implements Runnable,IJocObserver {
    private final IJocServices service;
    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static final Logger logger = LogManager.getLogger();

    public JocClientRpcWorker(IJocServices service, Socket connection) {
        this.service = service;
        this.connection = connection;
        this.connected = true;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            logger.error("Eroare la inițializarea stream-urilor: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Request request = (Request) input.readObject();
                logger.info("Request primit: {}", request);
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Eroare la procesarea request-ului: {}", e.getMessage());
                connected = false;
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
            logger.info("Conexiune client închisă.");
        } catch (IOException e) {
            logger.error("Eroare la închiderea conexiunii: {}", e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        switch (request.getType()) {
            case LOGIN: {
                String alias = (String) request.getData();
                try {
                    Jucator jucator = service.login(alias, this);
                    return new Response(ResponseType.OK, jucator);
                } catch (Exception e) {
                    return new Response(ResponseType.ERROR, e.getMessage());
                }
            }
            case LOGOUT: {
                Jucator jucator = (Jucator) request.getData();
                try {
                    service.logout(jucator, this);
                    connected = false;
                    return new Response(ResponseType.OK, null);
                } catch (Exception e) {
                    return new Response(ResponseType.ERROR, e.getMessage());
                }
            }
            case INCEPE_JOC: {
                try {
                    Configuratie configuratie = service.incepeJoc();
                    return new Response(ResponseType.OK, configuratie);
                } catch (Exception e) {
                    return new Response(ResponseType.ERROR, e.getMessage());
                }
            }
            case SALVEAZA_JOC: {
                // request.getData() conține acum: new Object[]{Jucator, Configuratie, Integer punctaj, Integer durataSauIncercari}
                Object[] data = (Object[]) request.getData();
                Jucator jucator = (Jucator) data[0];
                Configuratie configuratie = (Configuratie) data[1];
                int punctaj = (int) data[2];
                int durataSauIncercari = (int) data[3];
                try {
                    service.salveazaJoc(jucator, configuratie, punctaj, durataSauIncercari);
                    return new Response(ResponseType.OK, null);
                } catch (Exception e) {
                    return new Response(ResponseType.ERROR, e.getMessage());
                }
            }

            case GET_CLASAMENT: {
                try {
                    Iterable<Joc> clasament = service.getClasament();
                    return new Response(ResponseType.OK, clasament);
                } catch (Exception e) {
                    return new Response(ResponseType.ERROR, e.getMessage());
                }
            }
            case ALEGE:{
                Jucator jucator = (Jucator) request.getData();
                try {
                    service.alegeVarianta(jucator);
                    return new Response(ResponseType.OK, null);
                } catch (Exception e) {
                    return new Response(ResponseType.ERROR, e.getMessage());
                }
            }
            default:
                return new Response(ResponseType.ERROR, "Tip de request necunoscut.");
        }
    }

    @Override
    public void clasamentActualizat() throws Exception {
        logger.info("Notificare clasament -> trimitere CLASAMENT_ACTUALIZAT.");
        sendResponse(new Response(ResponseType.CLASAMENT_ACTUALIZAT, null));
    }

    @Override
    public void jocPornit(Cuvant cuvant, String mesaj) throws Exception {
        logger.info("Notificare joc -> trimitere JOC_PORNIT.");
        Object[] data = new Object[]{cuvant, mesaj};
        sendResponse(new Response(ResponseType.JOC_PORNIT, data));
    }

    @Override
    public void incepeRunda(String jucatorActiv) throws Exception {
        logger.info("Notificare jucator -> INCEPE_RUNDA pt: {}", jucatorActiv);
        sendResponse(new Response(ResponseType.INCEPE_RUNDA, jucatorActiv));
    }

    @Override
    public void variantaAleasa(Cuvant cuvant) throws Exception {
        logger.info("Notificare jucator -> CUVANT_SELECTAT: {}", cuvant);
        sendResponse(new Response(ResponseType.ALES, cuvant));
    }

    private synchronized void sendResponse(Response response) throws IOException {
        logger.traceEntry("sendResponse: {}", response);
        output.writeObject(response);
        output.flush();
        logger.traceExit();
    }
}
