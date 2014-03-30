package com.neodem.coup.common.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

class ComClientThread extends Thread {
    private static final Logger log = LogManager.getLogger(ComClientThread.class.getName());
    private Socket socket = null;
    private ComBaseClient client = null;
    private DataInputStream streamIn = null;

    public ComClientThread(ComBaseClient _client, Socket _socket) {
        client = _client;
        socket = _socket;
        setName("ComClientThread");
        open();
        start();
    }

    void open() {
        try {
            streamIn = new DataInputStream(socket.getInputStream());
        } catch (IOException ioe) {
            log.error("Error getting input stream: " + ioe);
            client.stop();
        }
    }

    public void close() {
        try {
            if (streamIn != null) streamIn.close();
        } catch (IOException ioe) {
            log.error("Error closing input stream: " + ioe);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            try {
                client.handleMessage(streamIn.readUTF());
            } catch (IOException ioe) {
                log.error("Listening error: " + ioe.getMessage());
                client.stop();
            }
        }
    }
}
