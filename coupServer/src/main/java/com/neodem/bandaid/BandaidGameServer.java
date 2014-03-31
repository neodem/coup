package com.neodem.bandaid;

import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageType;
import com.neodem.coup.common.network.ComBaseClient;
import com.neodem.coup.common.network.ComInterface;
import com.neodem.coup.common.network.ComServer;
import com.neodem.coup.common.proxy.PlayerProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public final class BandaidGameServer implements ComInterface {

    private static final Logger log = LogManager.getLogger(BandaidGameServer.class.getName());
    private final MessageHandler messageHandler = new MessageHandler("localhost", 6969, this);
    //
    // networked players registerd with this server. In the future They may or may not not be in a game
    private final Map<Integer, PlayerProxy> registeredPlayers = new HashMap<>();
    private GameMaster gameMaster;
    private MessageTranslator messageTranslator;
    private Thread gameThread;
    private ComServer comServer;

    public class MessageHandler extends ComBaseClient implements Runnable {

        private final BandaidGameServer server;
        private String mostRecentMessage = null;

        public MessageHandler(String host, int port, BandaidGameServer server) {
            super(host, port);
            this.server = server;
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(int from, String msg) {
            log.trace("Server : handle message : " + msg);
            MessageType type = messageTranslator.unmarshalMessageTypeFromMessage(msg);
            if (type == MessageType.register) {
                String playerName = messageTranslator.unmarshalPlayerNameFromMessage(msg);
                PlayerProxy proxy = new PlayerProxy(playerName, from, messageTranslator, server);
                registeredPlayers.put(from, proxy);

                //checkForGameStart();
                // note this code is temp. It starts the game when there are 4 players registerd.
                // in the future I'd like to enable players to register with the server and then
                // wait until a game is available for them. eg. the server will wait for 4 people,
                // put them into a game and fire it off and then wait for 4 more, etc.
                if (registeredPlayers.size() == 4) {
                    startGame();
                }
            } else if (type == MessageType.reply) {
                synchronized (this) {
                    mostRecentMessage = msg;
                    notify();
                }
            }
        }

        public void run() {
            init();
        }
    }


    // TODO
    private void checkForGameStart() {

        if (registeredPlayers.size() == 4) {
            // move them into their own game
            // the game will have it's own gamemaster (also on the chat channel)
            //
        }
    }

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
    }

    public void startCoupServer() {
        comServer.startComServer();
        startMessageHandler();
    }

    private void startMessageHandler() {
        Thread mt = new Thread(messageHandler);
        mt.setName("MessageHandler");
        mt.start();
    }

    @Override
    public void sendMessage(int dest, String msg) {
        messageHandler.send(dest, msg);
    }

    @Override
    public void sendBroadcastMessage(String msg) {
        messageHandler.broadcast(msg);
    }

    @Override
    public String sendAndGetReply(int dest, String msg) {
        messageHandler.send(dest, msg);

        synchronized (messageHandler) {
            try {
                messageHandler.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return messageHandler.getMostRecentMessage();
    }

    private void startGame() {
        log.info("initializing Game");
        gameMaster.initGame(new ArrayList(registeredPlayers.values()));

        log.info("Starting Game");
        gameMaster.startGame();
    }

    public void setGameMaster(GameMaster cgm) {
        this.gameMaster = cgm;
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }
}
