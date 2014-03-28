package com.neodem.coup.client.network;

import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.messaging.CoupServer;
import com.neodem.coup.common.messaging.MessageTranslator;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class NetworkGateway {

    private MessageTranslator messageTranslator;
    private CoupServer coupServer;

    public void registerPlayer(CoupPlayer player) {
        ServiceProxy proxy = new ServiceProxy(player, messageTranslator);
        coupServer.registerNewClient(player.getPlayerName(), proxy);
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    public void setCoupServer(CoupServer coupServer) {
        this.coupServer = coupServer;
    }
}
