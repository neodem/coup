package com.neodem.coup.communications;

import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class ComBaseClient {

    private final String serverName;
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ComClientThread client = null;
    private ComMessageTranslator mt = new DefaultComMessageTranslator();

    public ComBaseClient(String serverName) {
        this.serverName = serverName;
    }

    protected abstract Logger getLog();

    public void init() {
        getLog().info("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, 6969);
            getLog().info("Connected to ComServer : " + socket);
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ComClientThread(this, socket);
        } catch (UnknownHostException uhe) {
            getLog().error("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            getLog().error("Unexpected exception: " + ioe.getMessage());
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

        try {
            streamOut.writeUTF(m);
            streamOut.flush();
        } catch (IOException ioe) {
            getLog().error("Sending error: " + ioe.getMessage());
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
            getLog().error("Error closing ...");
        }
        client.close();
        client.stop();
    }

    public enum Dest {
        Broadcast, Server, Player1, Player2, Player3, Player4
    }
}