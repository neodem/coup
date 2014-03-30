package com.neodem.coup.client;

import com.neodem.coup.client.network.ServiceProxy;
import com.neodem.coup.client.players.RandomCoupPlayer;
import com.neodem.coup.common.messaging.JsonMessageTranslator;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupSingleClientRunner {
    public static void main(String[] args) {
        ServiceProxy sp = new ServiceProxy(new RandomCoupPlayer("Player4"), new JsonMessageTranslator(), "localhost", 6969);
        sp.init();
    }
}
