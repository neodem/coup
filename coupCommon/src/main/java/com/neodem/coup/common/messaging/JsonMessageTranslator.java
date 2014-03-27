package com.neodem.coup.common.messaging;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.BaseCoupPlayer;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.GamePlayer;
import org.json.JSONArray;
import org.json.JSONException;
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
    private static final String FACEUP = "FaceUp";
    private static final String CARDS = "CardCollection";

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
    public Message makeMessage(MessageType type, GamePlayer p) {
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
    public Message makeMessage(MessageType type, CoupAction a, GamePlayer p, CoupGameContext gc) {
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
    public GamePlayer getPlayer(Message m) {
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
        JSONObject j;
        CoupCardType result = CoupCardType.Unknown;

        try {
            j = new JSONObject(m.content);
            String type = j.getString(CARDTYPE);
            result = CoupCardType.valueOf(type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
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

    @Override
    public CoupAction getCoupAction(Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CoupCard getCoupCard(Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Multiset<CoupCard> getCardMultiset(Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CoupGameContext getCoupGameContext(Message m) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        if (c != null) {
            try {
                j.put(CARDTYPE, c.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addAction(CoupAction a, JSONObject j) {
        if (a != null) {
            JSONObject action = new JSONObject();
            try {
                action.put(ACTIONTYPE, a.getActionType().name());
                addPlayer(a.getActionOn(), j);
                j.put(ACTION, action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addPlayer(GamePlayer p, JSONObject j) {
        if (p != null) {
            try {
                j.put(PLAYER, p.getMyName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCoupCard(CoupCard card, JSONObject j) {
        if (card != null) {
            try {
                JSONObject jcard = makeCard(card);
                j.put(CARD, jcard);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject makeCard(CoupCard c) {
        JSONObject jcard = new JSONObject();
        try {
            addCoupCardType(c.type, jcard);
            jcard.put(FACEUP, c.faceUp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jcard;
    }

    private void addCardSet(Multiset<CoupCard> cards, JSONObject j) {
        if (cards != null) {
            try {
                JSONArray array = new JSONArray();
                for (CoupCard c : cards)
                    array.put(makeCard(c));
                j.put(CARDS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
