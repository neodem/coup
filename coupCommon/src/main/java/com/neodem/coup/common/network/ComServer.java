package com.neodem.coup.common.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ComServer implements Runnable {

    private static final Logger log = LogManager.getLogger(ComServer.class.getName());
    private final ComMessageTranslator mt = new DefaultComMessageTranslator();
    private volatile Map<Integer, ClientConnector> clientMap = new HashMap<>();
    private volatile Thread serverThread = null;
    private ServerSocket server = null;
    private int clientCount = 0;
    private int port = 6969;

    // special destinations
    public static final int Broadcast = -1;
    public static final int Server = -2;
    public static final int Unknown = -3;

    public class ClientConnector extends Thread {

        private int clientId;
        private Socket socket = null;
        private DataInputStream streamIn = null;
        private DataOutputStream streamOut = null;

        public ClientConnector(Socket socket, int id) {
            super();
            this.socket = socket;
            this.clientId = id;
        }

        public void send(String msg) {
            log.trace("send message to {} : {}", clientId, msg);
            try {
                streamOut.writeUTF(msg);
                streamOut.flush();
            } catch (IOException ioe) {
                log.error(clientId + " ERROR sending: " + ioe.getMessage());
                removeClientConnector(clientId);
            }
        }

        public void run() {
            log.info("Server Thread connected. Id = {}", clientId);

            Thread thisThread = Thread.currentThread();
            while (clientMap.get(clientId) == thisThread) {
                try {
                    handle(clientId, streamIn.readUTF());
                } catch (IOException ioe) {
                    System.out.println(clientId + " ERROR reading: " + ioe.getMessage());
                    removeClientConnector(clientId);
                }
            }
        }

        public void openCommunication() throws IOException {
            streamIn = new DataInputStream(new
                    BufferedInputStream(socket.getInputStream()));
            streamOut = new DataOutputStream(new
                    BufferedOutputStream(socket.getOutputStream()));
        }

        public void closeCommunication() throws IOException {
            if (socket != null) socket.close();
            if (streamIn != null) streamIn.close();
            if (streamOut != null) streamOut.close();
        }
    }

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
                addClientThread(server.accept());
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

    public synchronized void handle(int from, String input) {
        if (input.equals(".bye")) {
            clientMap.get(from).send(".bye");
            removeClientConnector(from);
        } else {
            int to = mt.getDest(input);
            String payload = mt.getPayload(input);
            log.debug("relaying message from {} to {} : {}", from, to, payload);

            //todo add the from here??

            if (to == Broadcast) {
                for (ClientConnector c : clientMap.values()) {
                    c.send(payload);
                }
            } else {
                ClientConnector c = clientMap.get(to);
                c.send(payload);
            }
        }
    }

    public synchronized void removeClientConnector(int id) {
        ClientConnector toTerminate = clientMap.get(id);
        log.info("Removing client thread " + id);
        clientCount--;
        try {
            toTerminate.closeCommunication();
        } catch (IOException ioe) {
            log.error("Error closing thread: " + ioe);
        }
        clientMap.put(id, null);
    }

    private void addClientThread(Socket socket) {
        if (clientCount < 5) {
            log.info("Client accepted: " + socket);

            //todo make this dynamic
            int dest = getNextDest();

            // make thread
            ClientConnector clientConnectorThread = new ClientConnector(socket, dest);
            clientConnectorThread.setName("ClientConnectionThread-" + dest);

            // add to map
            clientMap.put(dest, clientConnectorThread);

            // start thread
            try {
                clientConnectorThread.openCommunication();
                clientConnectorThread.start();
            } catch (IOException ioe) {
                log.error("Error opening thread: " + ioe);
            }

            clientCount++;
        } else
            log.warn("Client refused: maximum 5 reached.");
    }

    private int getNextDest() {
        if (clientCount == 0) return Server;
        return clientCount;
    }

}