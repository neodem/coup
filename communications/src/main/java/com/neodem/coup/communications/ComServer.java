package com.neodem.coup.communications;

import com.neodem.coup.communications.ComBaseClient.Dest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ComServer implements Runnable {

    private static Logger log = LogManager.getLogger(ComServer.class.getName());
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    private ComMessageTranslator mt = new DefaultComMessageTranslator();
    private Map<Dest, ComServerThread> clientMap = new HashMap<>();

    public ComServer(int port) {
        try {
            log.info("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            log.info("Server started: " + server);
            start();
        } catch (IOException ioe) {
            log.error("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public static void main(String args[]) {
        if (args.length != 1)
            System.out.println("Usage: java ComServer port");
        else
            new ComServer(Integer.parseInt(args[0]));
    }

    public void run() {
        while (thread != null) {
            try {
                log.info("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                log.error("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public synchronized void handle(Dest from, String input) {
        log.debug("handle message from {} : {}", from, input);
        if (input.equals(".bye")) {
            clientMap.get(from).send(".bye");
            remove(from);
        } else {
            Dest dest = mt.getDest(input);
            String payload = mt.getPayload(input);

            if (dest == Dest.Broadcast) {
                for (ComServerThread c : clientMap.values()) {
                    c.send(payload);
                }
            } else {
                ComServerThread client = clientMap.get(dest);
                client.send(payload);
            }
        }
    }

    public synchronized void remove(Dest d) {
        ComServerThread toTerminate = clientMap.get(d);
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
            Dest dest = getNextDest();

            ComServerThread client = new ComServerThread(this, socket, dest);

            clientMap.put(dest, client);

            try {
                client.open();
                client.start();
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