package com.neodem.coup.communications;

import com.neodem.coup.communications.ComMessageTranslator.Dest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ComServerThread extends Thread {
    private ComServer server = null;
    private Socket socket = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Dest d;

    public ComServerThread(ComServer _server, Socket _socket, Dest d) {
        super();
        server = _server;
        socket = _socket;
        this.d = d;
    }

    public void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(d + " ERROR sending: " + ioe.getMessage());
            server.remove(d);
            stop();
        }
    }

    public void run() {
        System.out.println("Server Thread " + d + " running.");
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