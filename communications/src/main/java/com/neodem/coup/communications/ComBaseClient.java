package com.neodem.coup.communications;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class ComBaseClient {

    public enum Dest {
        Broadcast, Server, Player1, Player2, Player3, Player4
    }

    private final String serverName;
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ComClientThread client = null;
    private ComMessageTranslator mt = new DefaultComMessageTranslator();

    public ComBaseClient(String serverName) {
        this.serverName = serverName;
    }

    public void init() {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, 6969);
            System.out.println("Connected: " + socket);
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ComClientThread(this, socket);
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    protected void send(String m) {
        try {
            streamOut.writeUTF(m);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
            stop();
        }
    }

    /**
     * send a message to user asynchronously
     *
     * @param destination the dest to send the message to
     * @param message     the message to send
     */
    protected void sendTo(Dest destination, String message) {
        String m = mt.makeMessage(destination, message);
        send(m);
    }

    /**
     * send a message to all registered users
     *
     * @param message the message to send
     */
    protected void broadcast(String message) {
        String m = mt.makeMessage(Dest.Broadcast, message);
        send(m);
    }

    protected abstract void handleMessage(String msg);

    public void stop() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
        client.close();
        client.stop();
    }
}