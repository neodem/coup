package com.neodem.coup.common.proxy;

import com.neodem.bandaid.proxy.PlayerProxy;
import com.neodem.bandaid.proxy.PlayerProxyFactory;
import com.neodem.bandaid.server.BandaidGameServerImpl;
import com.neodem.coup.common.messaging.CoupMessageTranslator;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public class CoupPlayerProxyFactory implements PlayerProxyFactory {

    private CoupMessageTranslator coupMessageTranslator;

    public CoupPlayerProxyFactory() {
    }

    @Override
    public PlayerProxy makeNewProxy(String playerName, int from, BandaidGameServerImpl server) {
        return new CoupPlayerProxy(playerName, from, coupMessageTranslator, server);
    }

    public void setCoupMessageTranslator(CoupMessageTranslator coupMessageTranslator) {
        this.coupMessageTranslator = coupMessageTranslator;
    }
}
