package com.neodem.coup.server.network;

import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.communications.ComBaseClient;
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
public class CoupServer extends ComBaseClient {

    private static Logger log = LogManager.getLogger(CoupServer.class.getName());
    protected Map<Integer, CommunicatingPlayer> registeredPlayers;
    //protected Map<Integer, MessageClient> clients;
    private int maxPlayers;
    private CoupGameMaster cgm;
    private int nextId = 1;

    private MessageTranslator messageTranslator;

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
    }

    public CoupServer() {
        super("localhost");
        registeredPlayers = new HashMap<>();
        //clients = new HashMap<>();
        maxPlayers = 4;
        nextId = 1;
    }

    @Override
    protected void handleMessage(String msg) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


//    public void registerNewClient(String playerName, MessageClient client) {
//        log.debug("registerPlayer(" + playerName + ")");
//
//        if (registeredPlayers.keySet().contains(playerName)) throw new IllegalArgumentException("name already used");
//
//        if (registeredPlayers.size() == maxPlayers) {
//            throw new IllegalStateException("max players already");
//        }
//
//        CommunicatingPlayer cp = new CommunicatingPlayer(playerName, ++nextId, messageTranslator, this);
//
//        registeredPlayers.put(nextId, cp);
//        clients.put(nextId, client);
//
//        if (nextId == 5) {
//            start();
//        }
//    }

    private void start() {
        log.info("initializing Game");
        cgm.initGame(new ArrayList(registeredPlayers.values()));

        log.info("Starting Game");
        CoupPlayer winningPlayer = cgm.runGameLoop();

        log.info("The game is over : " + winningPlayer.getPlayerName() + " was the winner!");
    }

    public void setCgm(CoupGameMaster cgm) {
        this.cgm = cgm;
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }


}
