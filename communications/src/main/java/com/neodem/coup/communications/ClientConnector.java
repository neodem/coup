package com.neodem.coup.communications;

import com.neodem.coup.communications.ComBaseClient.Dest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnector extends Thread {
    private static Logger log = LogManager.getLogger(ClientConnector.class.getName());

    private ComServer server = null;
    private Socket socket = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Dest d;

    public ClientConnector(ComServer _server, Socket _socket, Dest d) {
        super();
        server = _server;
        socket = _socket;

        setName("ClientConnectionThread-" + d);
        this.d = d;
    }

    public void send(String msg) {
        log.debug("send message to {} : {}", d, msg);
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException ioe) {
            log.error(d + " ERROR sending: " + ioe.getMessage());
            server.remove(d);
            stop();
        }
    }

    public void run() {
        log.info("Server Thread connected to: " + d);
        while (true) {
            try {
                server.handle(d, streamIn.readUTF());
            } catch (IOException ioe) {
                System.out.println(d + " ERROR reading: " + ioe.getMessage());
                server.remove(d);
                stop();
            }
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new
                BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new
                BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
        if (streamIn != null) streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}