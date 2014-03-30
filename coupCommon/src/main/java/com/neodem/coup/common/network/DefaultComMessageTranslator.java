package com.neodem.coup.common.network;

import com.neodem.coup.common.network.ComBaseClient.Dest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public class DefaultComMessageTranslator implements ComMessageTranslator {

    private static final String DEST = "to";
    private static final String ORIG = "from";
    private static final String PAYLOAD = "p";

    @Override
    public ComBaseClient.Dest getDest(String m) {
        JSONObject j;
        Dest result = Dest.Broadcast;

        if (m != null) {
            try {
                j = new JSONObject(m);
                String d = j.getString(DEST);
                result = Dest.valueOf(d);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public Dest getFrom(String m) {
        JSONObject j;
        Dest result = Dest.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                String d = j.getString(ORIG);
                result = Dest.valueOf(d);
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
    public String makeMessage(Dest to, String payload) {
        return makeMessage(to, null, payload);
    }

    @Override
    public String makeMessage(Dest to, Dest from, String payload) {
        JSONObject j = new JSONObject();
        setDest(to, j);
        setFrom(from, j);
        setPayload(payload, j);
        return j.toString();
    }

    protected void setDest(Dest d, JSONObject j) {
        if (d != null && j != null) {
            try {
                j.put(DEST, d.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setFrom(Dest d, JSONObject j) {
        if (d != null && j != null) {
            try {
                j.put(ORIG, d.name());
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
