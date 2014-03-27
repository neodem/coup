package com.neodem.coup.client.network;

import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageTransport;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class NetworkGateway {

    private MessageTranslator messageTranslator;
    private MessageTransport messageTransport;

    public void registerPlayer(CoupPlayer player) {
        ServiceProxy proxy = new ServiceProxy(player, messageTranslator);
        messageTransport.registerNewClient(player.getMyName(), proxy);
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    public void setMessageTransport(MessageTransport messageTransport) {
        this.messageTransport = messageTransport;
    }
}
