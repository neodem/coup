package com.neodem.coup.common.messaging;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.game.BaseCoupPlayer;import org.json.JSONException;
import org.json.JSONObject;

/**
 * Will create/read messages using JSON
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class JsonMessageTranslator implements MessageTranslator {

    private static final String TYPE = "MessageType";
    private static final String CONTEXT = "GameContext";
    private static final String MESSAGE = "Message";
    private static final String PLAYER = "PlayerName";
    private static final String ACTION = "Action";
    private static final String CARDTYPE = "CardType";
    private static final String CARD = "Card";
    private static final String BOOL = "TrueFalse";
    private static final String ACTIONTYPE = "ActionType";

    @Override
    public Message makeMessage(MessageType type) {
        JSONObject j = new JSONObject();
        addType(type, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addContext(gc, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, String message) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addMessage(message, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupPlayer p) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addPlayer(p, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupAction a) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addAction(a, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupAction a, CoupPlayer p, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addAction(a, j);
        addPlayer(p, j);
        addContext(gc, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupCardType c) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addCoupCardType(c, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, Multiset<CoupCard> cards) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addCardSet(cards, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupCard card) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addCoupCard(card, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, boolean bool) {
        JSONObject j = new JSONObject();
        addType(type, j);
        addBoolean(bool, j);
        return new Message(j.toString());
    }

    @Override
    public CoupAction getCoupAction(Message reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean getBoolean(Message m) {
        JSONObject j;
        Boolean result = null;

        try {
            j = new JSONObject(m.content);
            result = j.getBoolean(BOOL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public CoupCard getCoupCard(Message reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Multiset<CoupCard> getCardMultiset(Message reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CoupGameContext getCoupGameContext(Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GamePlayer getCoupPlayer(Message m) {
        JSONObject j;
        GamePlayer result = null;

        try {
            j = new JSONObject(m.content);
            result = new BaseCoupPlayer(j.getString(PLAYER));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;



    }

    @Override
    public String getString(Message m) {
        JSONObject j;
        String result = null;

        try {
            j = new JSONObject(m.content);
            result = j.getString(MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public CoupCardType getCoupCardType(Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MessageType getType(Message m) {
        JSONObject j;
        MessageType result = MessageType.unknown;

        try {
            j = new JSONObject(m.content);
            String type = j.getString(TYPE);
            result = MessageType.valueOf(type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void addBoolean(boolean bool, JSONObject j) {
        try {
            j.put(BOOL, Boolean.valueOf(bool));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addType(MessageType type, JSONObject j) {
        if (type != null) {
            try {
                j.put(TYPE, type.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMessage(String message, JSONObject j) {
        if (message != null) {
            try {
                j.put(MESSAGE, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addContext(CoupGameContext gc, JSONObject j) {
    }

    private void addCoupCardType(CoupCardType c, JSONObject j) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void addAction(CoupAction a, JSONObject j) {
        JSONObject action = new JSONObject();
        try {
            action.put(ACTIONTYPE, a.getActionType().name());
            addPlayer(a.getActionOn(), j);
            j.put(ACTION, action);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addPlayer(CoupPlayer p, JSONObject j) {
        if (p != null) {
            try {
                j.put(PLAYER, p.getMyName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCoupCard(CoupCard card, JSONObject j) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void addCardSet(Multiset<CoupCard> cards, JSONObject j) {
        //To change body of created methods use File | Settings | File Templates.
    }

}
