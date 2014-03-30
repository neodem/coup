package com.neodem.coup.common.network;

import com.neodem.coup.common.network.ComBaseClient.Dest;
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
    private volatile Map<Dest, ClientConnector> clientMap = new HashMap<>();
    private volatile Thread serverThread = null;
    private ServerSocket server = null;
    private int clientCount = 0;
    private int port = 6969;

    public class ClientConnector extends Thread {

        private final Dest d;
        private Socket socket = null;
        private DataInputStream streamIn = null;
        private DataOutputStream streamOut = null;

        public ClientConnector(Socket socket, Dest d) {
            super();
            this.socket = socket;
            this.d = d;
        }

        public void send(String msg) {
            log.trace("send message to {} : {}", d, msg);
            try {
                streamOut.writeUTF(msg);
                streamOut.flush();
            } catch (IOException ioe) {
                log.error(d + " ERROR sending: " + ioe.getMessage());
                remove(d);
            }
        }

        public void run() {
            log.info("Server Thread connected to: " + d);

            Thread thisThread = Thread.currentThread();
            while (clientMap.get(d) == thisThread) {
                try {
                    handle(d, streamIn.readUTF());
                } catch (IOException ioe) {
                    System.out.println(d + " ERROR reading: " + ioe.getMessage());
                    remove(d);
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
            toTerminate.closeCommunication();
        } catch (IOException ioe) {
            log.error("Error closing thread: " + ioe);
        }
        clientMap.put(d, null);
    }

    private void addClientThread(Socket socket) {
        if (clientCount < 5) {
            log.info("Client accepted: " + socket);

            //todo make this dynamic
            Dest dest = getNextDest();

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

    private Dest getNextDest() {
        if (clientCount == 0) return Dest.Server;
        if (clientCount == 1) return Dest.Player1;
        if (clientCount == 2) return Dest.Player2;
        if (clientCount == 3) return Dest.Player3;
        if (clientCount == 4) return Dest.Player4;
        return null;
    }

}