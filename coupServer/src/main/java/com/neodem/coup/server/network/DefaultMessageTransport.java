package com.neodem.coup.server.network;

import com.neodem.coup.common.messaging.Message;
import com.neodem.coup.common.messaging.MessageClient;
import com.neodem.coup.common.messaging.MessageTransport;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class DefaultMessageTransport implements MessageTransport {
    @Override
    public void send(int id, Message m) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Message sendAndGetReply(int id, Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerNewClient(String playerName, MessageClient client) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
