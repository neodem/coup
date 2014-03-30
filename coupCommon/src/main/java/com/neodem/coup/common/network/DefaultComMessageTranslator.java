package com.neodem.coup.common.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public class DefaultComMessageTranslator implements ComMessageTranslator {

    private static final String DEST = "to";
    private static final String PAYLOAD = "p";

    DefaultComMessageTranslator() {
    }

    @Override
    public int getDest(String m) {
        JSONObject j;
        int result = ComServer.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getInt(DEST);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String getPayload(String m) {
        JSONObject j;
        String result = "<unknown>";

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(PAYLOAD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String makeMessage(int to, String payload) {
        JSONObject j = new JSONObject();
        setDest(to, j);
        setPayload(payload, j);
        return j.toString();
    }

    @Override
    public String makeBroadcastMessage(String payload) {
        return makeMessage(ComServer.Broadcast, payload);
    }

    protected void setDest(int d, JSONObject j) {
        if (j != null) {
            try {
                j.put(DEST, d);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setPayload(String payload, JSONObject j) {
        if (payload != null && j != null) {
            try {
                j.put(PAYLOAD, payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
