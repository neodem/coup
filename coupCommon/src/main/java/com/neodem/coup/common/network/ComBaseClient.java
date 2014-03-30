package com.neodem.coup.common.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class ComBaseClient {

    private static final Logger log = LogManager.getLogger(ComBaseClient.class.getName());
    private final String serverName;
    private final int port;
    private final DataInputStream console = null;
    private final ComMessageTranslator mt = new DefaultComMessageTranslator();
    private Socket socket = null;
    private DataOutputStream streamOut = null;
    private volatile ComClientThread clientThread = null;

    /**
     * for handling the client stream data. It will wait until
     * a UTF datagram is received and then call handleMessage()
     */
    private class ComClientThread extends Thread {
        private DataInputStream streamIn = null;

        public void openCommunications() {
            try {
                streamIn = new DataInputStream(socket.getInputStream());
            } catch (IOException ioe) {
                log.error("Error getting input stream: " + ioe);
                stopClientThread();
            }
        }

        public void closeCommunications() {
            try {
                if (streamIn != null) streamIn.close();
            } catch (IOException ioe) {
                log.error("Error closing input stream: " + ioe);
            }
        }

        public void run() {
            Thread thisThread = Thread.currentThread();
            while (clientThread == thisThread)
                try {
                    handle(streamIn.readUTF());
                } catch (IOException ioe) {
                    log.error("Listening error: " + ioe.getMessage());
                    stopClientThread();
                }
        }
    }

    public ComBaseClient(String host, int port) {
        this.serverName = host;
        this.port = port;
    }

    public void init() {
        log.info("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, port);
            log.info("Connected to ComServer : " + socket);
            streamOut = new DataOutputStream(socket.getOutputStream());
            startClientThread();
        } catch (UnknownHostException uhe) {
            log.error("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            log.error("Unexpected exception: " + ioe.getMessage());
        }
    }

    protected void startClientThread() {
        if (clientThread == null) {
            clientThread = new ComClientThread();
            clientThread.setName("ComClientThread");
            clientThread.openCommunications();
            clientThread.start();
        }
    }

    /**
     * send a message to user asynchronously
     *
     * @param destination the dest to send the message to
     * @param message     the message to send
     */
    public void send(int destination, String message) {
        String m = mt.makeMessage(destination, message);

        log.trace("send to ComServer to route to {} : {}", destination, message);

        try {
            streamOut.writeUTF(m);
            streamOut.flush();
        } catch (IOException ioe) {
            log.error("Sending error: " + ioe.getMessage());
            stopClientThread();
        }
    }

    public void broadcast(String message) {
        send(ComServer.Broadcast, message);
    }

    private final void handle(String netMessage) {
        handleMessage(netMessage);
    }

    /**
     * clients need to implement this to deal with messages that it gets
     *
     * @param msg
     */
    protected abstract void handleMessage(String msg);

    public void stopClientThread() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            log.error("Error closing ...");
        }
        clientThread.closeCommunications();
        clientThread = null;
    }
}