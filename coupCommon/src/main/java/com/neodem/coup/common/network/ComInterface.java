package com.neodem.coup.common.network;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/30/14
 */
public interface ComInterface {
    String sendAndGetReply(int id, String m);

    void sendMessage(int id, String m);

    void sendBroadcastMessage(String m);
}
