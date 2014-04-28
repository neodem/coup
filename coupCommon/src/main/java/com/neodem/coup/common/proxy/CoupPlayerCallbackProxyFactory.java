package com.neodem.coup.common.proxy;

import com.neodem.bandaid.gamemasterstuff.PlayerCallbackProxyFactory;
import com.neodem.bandaid.proxy.PlayerCallbackProxy;
import com.neodem.coup.common.messaging.CoupMessageTranslator;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 4/28/14
 */
public class CoupPlayerCallbackProxyFactory implements PlayerCallbackProxyFactory {

    private CoupMessageTranslator coupMessageTranslator;

    @Override
    public PlayerCallbackProxy makeNewProxy(int clientNetworkId, String playerName) {
        return new CoupPlayerCallbackProxy(clientNetworkId, playerName, coupMessageTranslator);
    }

    public void setCoupMessageTranslator(CoupMessageTranslator coupMessageTranslator) {
        this.coupMessageTranslator = coupMessageTranslator;
    }
}
