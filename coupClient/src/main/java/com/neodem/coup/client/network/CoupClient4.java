package com.neodem.coup.client.network;

import com.neodem.coup.client.game.RandomCoupPlayer;
import com.neodem.coup.common.messaging.JsonMessageTranslator;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupClient4 {
    public static void main(String[] args) {
        ServiceProxy sp = new ServiceProxy(new RandomCoupPlayer("Player4"), new JsonMessageTranslator(), "localhost", 6969);
        sp.init();
    }
}
