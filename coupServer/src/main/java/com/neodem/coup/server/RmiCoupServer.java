package com.neodem.coup.server;

import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.CoupServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public class RmiCoupServer implements CoupServer {

    private static Logger log = LogManager.getLogger(RmiCoupServer.class.getName());
    protected List<CoupPlayer> registeredPlayers;
    private Collection<String> usedNames;
    private int maxPlayers;
    private CoupGameMaster cgm;

    public RmiCoupServer() {
        usedNames = new HashSet<>();
        registeredPlayers = new ArrayList<>();
        maxPlayers = 4;
    }

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
        log.info("server up and ready for connections!");
    }

    @Override
    public void registerPlayer(CoupPlayer player) {
        String playerName = player.getMyName();

        log.debug("registerPlayer(" + playerName + ")");

        if (usedNames.contains(playerName)) throw new IllegalArgumentException("name already used");
        usedNames.add(playerName);

        if (registeredPlayers.size() == maxPlayers) {
            throw new IllegalStateException("max players already");
        }

        if (registeredPlayers.contains(player)) throw new IllegalArgumentException("Already registered");
        registeredPlayers.add(player);
    }

    // todo determine when/how the game starts. For now we simply do it here
    @Override
    public void triggerGameStart() {

        log.info("initializing Game");
        cgm.initGame(registeredPlayers);

        log.info("Starting Game");
        CoupPlayer winningPlayer = cgm.runGameLoop();

        log.info("The game is over : " + winningPlayer.getMyName() + " was the winner!");
    }

    public void setCoupGameMaster(CoupGameMaster cgm) {
        this.cgm = cgm;
    }
}
