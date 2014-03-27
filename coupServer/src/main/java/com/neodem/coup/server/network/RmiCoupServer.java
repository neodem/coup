package com.neodem.coup.server.network;

import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageTransport;
import com.neodem.coup.server.game.CoupGameMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public class RmiCoupServer {

    private static Logger log = LogManager.getLogger(RmiCoupServer.class.getName());
    protected Map<String, CoupPlayer> registeredPlayers;
    private int maxPlayers;
    private CoupGameMaster cgm;
    private MessageTranslator messageTranslator;
    private MessageTransport messageTransport;
    private int nextId = 1;

    public RmiCoupServer() {
        registeredPlayers = new HashMap<>();
        maxPlayers = 4;
        nextId = 1;
    }

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
        log.info("server up and ready for connections!");
    }

    public int registerPlayer(String playerName) {

        log.debug("registerPlayer(" + playerName + ")");

        if (registeredPlayers.keySet().contains(playerName)) throw new IllegalArgumentException("name already used");

        if (registeredPlayers.size() == maxPlayers) {
            throw new IllegalStateException("max players already");
        }

        CommunicatingPlayer cp = new CommunicatingPlayer(playerName, ++nextId, messageTranslator, messageTransport);

        registeredPlayers.put(playerName, cp);

        return nextId;
    }

    // todo determine when/how the game starts. For now we simply do it here
    public void triggerGameStart() {

        log.info("initializing Game");
        cgm.initGame(new ArrayList(registeredPlayers.values()));

        log.info("Starting Game");
        CoupPlayer winningPlayer = cgm.runGameLoop();

        log.info("The game is over : " + winningPlayer.getMyName() + " was the winner!");
    }

    public void setCoupGameMaster(CoupGameMaster cgm) {
        this.cgm = cgm;
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    public void setMessageTransport(MessageTransport messageTransport) {
        this.messageTransport = messageTransport;
    }
}
