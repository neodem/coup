package com.neodem.coup.communications;

import com.neodem.coup.communications.ComBaseClient.Dest;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public interface ComMessageTranslator {

    Dest getDest(String m);

    String getPayload(String m);

    String makeMessage(Dest d, String payload);


}
