package com.neodem.coup.common.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface MessageClient {

    Message handleMessageWithReply(Message m);

    void handleAsynchonousMessage(Message m);
}
