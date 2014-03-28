package com.neodem.coup.common.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface CoupServer {
    /**
     * send a message to user asynchronously
     *
     * @param id the user to send to
     * @param m  the message to send
     */
    void send(int id, Message m);

    /**
     * send a message to a user and wait for a reply
     *
     * @param id the user to send to
     * @param m  the message to send
     * @return the reply message
     */
    Message sendAndGetReply(int id, Message m);

    void registerNewClient(String playerName, MessageClient client);
}
