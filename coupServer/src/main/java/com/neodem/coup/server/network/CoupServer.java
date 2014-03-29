package com.neodem.coup.server.network;

import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageType;
import com.neodem.coup.communications.ComBaseClient;
import com.neodem.coup.communications.ComBaseClient.Dest;
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
    private String mostRecentMessage = null;

    public class MessageHandler extends ComBaseClient implements Runnable {

        private final CoupServer server;

        public MessageHandler(String serverName, CoupServer server) {
            super(serverName);
            this.server = server;
        }

        @Override
        protected Logger getLog() {
            return log;
        }

        @Override
        protected void handleMessage(String msg) {
            getLog().debug("Server : handle message : " + msg);
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
                mostRecentMessage = msg;
            }
        }

        public void run() {
            init();
        }
    }

    public CoupServer() {
        registeredPlayers = new HashMap<>();

        messageHandler = new MessageHandler("localhost", this);

        (new Thread(messageHandler)).start();
    }

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
    }

    public void sendMessage(Dest dest, String msg) {
        messageHandler.send(dest, msg);
    }

    public String sendAndGetReply(Dest dest, String msg) {
        messageHandler.send(dest, msg);
        while (mostRecentMessage == null) ;
        String returnedMsg = mostRecentMessage;
        mostRecentMessage = null;
        return returnedMsg;
    }

    private void startGame() {
        log.info("initializing Game");
        cgm.initGame(new ArrayList(registeredPlayers.values()));

        log.info("Starting Game");
        CoupCommunicationInterface winningPlayer = cgm.runGameLoop();

        log.info("The game is over : " + winningPlayer.getPlayerName() + " was the winner!");
    }

    public void setCgm(CoupGameMaster cgm) {
        this.cgm = cgm;
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }
}
