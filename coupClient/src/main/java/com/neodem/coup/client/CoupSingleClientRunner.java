package com.neodem.coup.client;

import com.neodem.coup.client.players.RandomCoupPlayer;
import com.neodem.coup.common.messaging.JsonCoupMessageTranslator;
import com.neodem.coup.common.proxy.CoupBandaidServerNetworkedProxyServerSide;

/**
 * Fires up a single client
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupSingleClientRunner {
    public static void main(String[] args) {
        CoupBandaidServerNetworkedProxyServerSide sp = new CoupBandaidServerNetworkedProxyServerSide(new RandomCoupPlayer("Player4"), new JsonCoupMessageTranslator(), "localhost", 6969);
        sp.init();
    }
}
