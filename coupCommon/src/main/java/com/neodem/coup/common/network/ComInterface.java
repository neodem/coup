package com.neodem.coup.common.network;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/30/14
 */
public interface ComInterface {
    String sendAndGetReply(ComBaseClient.Dest id, String m);

    void sendMessage(ComBaseClient.Dest id, String m);
}
