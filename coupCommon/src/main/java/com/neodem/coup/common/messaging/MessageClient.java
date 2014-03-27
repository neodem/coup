package com.neodem.coup.common.messaging;

import com.neodem.coup.common.messaging.Message;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface MessageClient {

    Message handleMessageWithReply(Message m);

    void getAsynchonousMessage(Message m);
}
