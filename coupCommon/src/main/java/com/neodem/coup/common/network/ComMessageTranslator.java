package com.neodem.coup.common.network;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public interface ComMessageTranslator {

    int getDest(String m);

    String getPayload(String m);

    /**
     * @param to      the id we want to send to
     * @param payload the message
     * @return a marshaled message
     */
    String makeMessage(int to, String payload);

    ///********

    int getFrom(String netMessage);


    String addFrom(int from, String message);
}
