package com.neodem.coup.common.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/28/14
 */
public class DefaultComMessageTranslator implements ComMessageTranslator {

    private static final String DEST = "to";
    private static final String FROM = "from";
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
    public int getFrom(String m) {
        JSONObject j;
        int result = ComServer.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getInt(FROM);
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
    public String addFrom(int from, String m) {
        JSONObject j;
        String result = m;

        if (m != null) {
            try {
                j = new JSONObject(m);
                j.put(FROM, from);
                result = j.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
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
