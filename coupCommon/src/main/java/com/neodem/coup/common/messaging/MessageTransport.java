package com.neodem.coup.common.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public interface MessageTransport {

    /**
     * send a message to user asynchronously
     *
     * @param destination the dest to send the message to
     * @param m           the message to send
     */
    void sendTo(String destination, String message);

    /**
     * send a message to all registered users
     *
     * @param m the message to send
     */
    void broadcast(String message);

    void registerNewDestination(String destinationId);

}
