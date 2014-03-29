package com.neodem.coup.server.network;

import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageType;
import com.neodem.coup.communications.ComBaseClient;
import com.neodem.coup.communications.ComBaseClient.Dest;
import com.neodem.coup.communications.ComServer;
import com.neodem.coup.server.game.CoupGameMaster;
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
public final class CoupServer {

    private static Logger log = LogManager.getLogger(CoupServer.class.getName());
    protected Map<Dest, PlayerProxy> registeredPlayers;
    private MessageHandler messageHandler;
    private CoupGameMaster cgm;
    private MessageTranslator messageTranslator;
    private ComServer comServer;

    public class MessageHandler extends ComBaseClient implements Runnable {

        private final CoupServer server;
        private String mostRecentMessage = null;

        public MessageHandler(String host, int port, CoupServer server) {
            super(host, port);
            this.server = server;
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(String msg) {
            log.debug("Server : handle message : " + msg);
            MessageType type = messageTranslator.getType(msg);
            if (type == MessageType.register) {
                String playerName = messageTranslator.getPlayerName(msg);
                Dest dest = Dest.valueOf(playerName);
                PlayerProxy proxy = new PlayerProxy(playerName, dest, messageTranslator, server);
                registeredPlayers.put(dest, proxy);
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

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
    }

    public void startCoupServer() {
        registeredPlayers = new HashMap<>();
        messageHandler = new MessageHandler("localhost", 6969, this);

        Thread mt = new Thread(messageHandler);
        mt.setName("Coup Server MessageHandler");
        mt.start();
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }

    public void sendMessage(Dest dest, String msg) {
        messageHandler.send(dest, msg);
    }

    public String sendAndGetReply(Dest dest, String msg) {
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
        cgm.initGame(new ArrayList(registeredPlayers.values()));

        log.info("Starting Game");
        Thread game = new Thread(cgm);
        game.setName("Coup GameMaster");
        game.start();


//        CoupCommunicationInterface winningPlayer = cgm.getWinningPlayer();
//
//        log.info("The game is over : " + winningPlayer.getPlayerName() + " was the winner!");
    }

    public void setCgm(CoupGameMaster cgm) {
        this.cgm = cgm;
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }


}
