package com.neodem.coup.common.network;

import com.neodem.coup.common.network.ComBaseClient.Dest;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public interface ComMessageTranslator {

    Dest getDest(String m);

    Dest getFrom(String m);

    String getPayload(String m);

    String makeMessage(Dest to, String payload);

    String makeMessage(Dest to, Dest from, String payload);
}
