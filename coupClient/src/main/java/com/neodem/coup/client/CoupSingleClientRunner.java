package com.neodem.coup.client;

import com.neodem.coup.client.players.RandomCoupPlayer;
import com.neodem.coup.common.messaging.JsonCoupMessageTranslator;
import com.neodem.coup.common.proxy.CoupPlayerCallbackNetworkTransport;

/**
 * Fires up a single client
 * <p/>
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/27/14
 */
public class CoupSingleClientRunner {
    public static void main(String[] args) {
        CoupPlayerCallbackNetworkTransport cp = new CoupPlayerCallbackNetworkTransport("localhost", new RandomCoupPlayer("Player4"), new JsonCoupMessageTranslator());
    }
}
