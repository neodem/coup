package com.neodem.coup.common.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class ComBaseClient {

    private static Logger log = LogManager.getLogger(ComBaseClient.class.getName());

    private final String serverName;
    private final int port;
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ComClientThread client = null;
    private ComMessageTranslator mt = new DefaultComMessageTranslator();

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
            client = new ComClientThread(this, socket);
        } catch (UnknownHostException uhe) {
            log.error("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            log.error("Unexpected exception: " + ioe.getMessage());
        }
    }

    /**
     * send a message to user asynchronously
     *
     * @param destination the dest to send the message to
     * @param message     the message to send
     */
    public void send(Dest destination, String message) {
        String m = mt.makeMessage(destination, message);

        log.trace("send to ComServer to route to {} : {}", destination, message);

        try {
            streamOut.writeUTF(m);
            streamOut.flush();
        } catch (IOException ioe) {
            log.error("Sending error: " + ioe.getMessage());
            stop();
        }
    }

    protected abstract void handleMessage(String msg);

    public void stop() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            log.error("Error closing ...");
        }
        client.close();
        client.stop();
    }

    public enum Dest {
        Broadcast, Server, Player1, Player2, Player3, Player4, Unknown
    }
}