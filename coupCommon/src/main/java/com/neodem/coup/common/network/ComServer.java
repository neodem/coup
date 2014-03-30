package com.neodem.coup.common.network;

import com.neodem.coup.common.network.ComBaseClient.Dest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ComServer implements Runnable {

    private static final Logger log = LogManager.getLogger(ComServer.class.getName());
    private final ComMessageTranslator mt = new DefaultComMessageTranslator();
    private final Map<Dest, ClientConnector> clientMap = new HashMap<>();
    private ServerSocket server = null;
    private volatile Thread serverThread = null;
    private int clientCount = 0;
    private int port = 6969;

    public void startComServer() {
        try {
            log.info("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            log.info("Server started: " + server);
            startServerThread();
        } catch (IOException ioe) {
            log.error("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (serverThread == thisThread) {
            try {
                log.info("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                log.error("Server accept error: " + ioe);
                stopServerThread();
            }
        }
    }

    private void startServerThread() {
        if (serverThread == null) {
            serverThread = new Thread(this);
            serverThread.setName("ComServer-main");
            serverThread.start();
        }
    }

    private void stopServerThread() {
        serverThread = null;
    }

    public synchronized void handle(Dest from, String input) {
        if (input.equals(".bye")) {
            clientMap.get(from).send(".bye");
            remove(from);
        } else {
            Dest to = mt.getDest(input);
            String payload = mt.getPayload(input);
            log.debug("relaying message from {} to {} : {}", from, to, payload);

            if (to == Dest.Broadcast) {
                for (ClientConnector c : clientMap.values()) {
                    c.send(payload);
                }
            } else {
                ClientConnector c = clientMap.get(to);
                c.send(payload);
            }
        }
    }

    public synchronized void remove(Dest d) {
        ClientConnector toTerminate = clientMap.get(d);
        log.info("Removing client thread " + d);
        clientCount--;
        try {
            toTerminate.close();
        } catch (IOException ioe) {
            log.error("Error closing thread: " + ioe);
        }
        toTerminate.stop();
        clientMap.put(d, null);
    }

    private void addThread(Socket socket) {
        if (clientCount < 5) {
            log.info("Client accepted: " + socket);

            //todo make this dynamic
            Dest dest = getNextDest();

            ClientConnector clientConnectorThread = new ClientConnector(this, socket, dest);

            clientMap.put(dest, clientConnectorThread);

            try {
                clientConnectorThread.open();
                clientConnectorThread.start();
                clientCount++;
            } catch (IOException ioe) {
                log.error("Error opening thread: " + ioe);
            }
        } else
            log.warn("Client refused: maximum 5 reached.");
    }

    private Dest getNextDest() {
        if (clientCount == 0) return Dest.Server;
        if (clientCount == 1) return Dest.Player1;
        if (clientCount == 2) return Dest.Player2;
        if (clientCount == 3) return Dest.Player3;
        if (clientCount == 4) return Dest.Player4;
        return null;
    }

}