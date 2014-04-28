package com.neodem.coup.client;

import com.neodem.coup.client.players.RandomCoupPlayer;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.messaging.CoupMessageTranslator;
import com.neodem.coup.common.messaging.JsonCoupMessageTranslator;
import com.neodem.coup.common.proxy.CoupPlayerCallbackNetworkTransport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Will start up 4 clients.
 * <p/>
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/27/14
 */
public class Coup4ClientRunner {

    private static final Logger log = LogManager.getLogger(Coup4ClientRunner.class.getName());
    private CoupMessageTranslator messageTranslator = new JsonCoupMessageTranslator();

    public static void main(String[] args) {
        Coup4ClientRunner runner = new Coup4ClientRunner();
        runner.start();
    }

    private void start() {
        setupPlayer(new RandomCoupPlayer("Player1"));
        setupPlayer(new RandomCoupPlayer("Player2"));
        setupPlayer(new RandomCoupPlayer("Player3"));
        setupPlayer(new RandomCoupPlayer("Player4"));
    }

    private void setupPlayer(CoupPlayerCallback player) {
        log.info("Starting player : " + player);
        CoupPlayerCallbackNetworkTransport cp = new CoupPlayerCallbackNetworkTransport("localhost", player, messageTranslator);
        cp.init();
    }
}
