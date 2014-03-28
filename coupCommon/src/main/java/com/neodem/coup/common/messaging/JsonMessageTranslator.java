package com.neodem.coup.common.messaging;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.BaseCoupPlayer;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerInfo;
import com.neodem.coup.common.game.GamePlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

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
    private static final String PLAYERS = "Players";
    private static final String PLAYERINFOS = "PlayerInfos";
    private static final String ACTIVE = "Active";
    private static final String COINCOUNT = "CoinCount";
    private static final String CARD1 = "Card1";
    private static final String CARD2 = "Card2";
    public boolean active = true;
    public int coins = 0;
    public CoupCard cardOne = new CoupCard(CoupCardType.Unknown);
    public CoupCard cardTwo = new CoupCard(CoupCardType.Unknown);

    @Override
    public Message makeMessage(MessageType type) {
        JSONObject j = new JSONObject();
        setType(type, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupGameContext(gc, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, String message) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setString(message, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, GamePlayer p) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setGamePlayer(p, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupAction a) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupAction(a, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupAction a, GamePlayer p, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupAction(a, j);
        setGamePlayer(p, j);
        setCoupGameContext(gc, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupCardType c) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupCardType(c, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, Multiset<CoupCard> cards) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCardMultiset(cards, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, CoupCard card) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupCard(card, j);
        return new Message(j.toString());
    }

    @Override
    public Message makeMessage(MessageType type, boolean bool) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setBoolean(bool, j);
        return new Message(j.toString());
    }

    protected void setBoolean(boolean bool, JSONObject j) {
        if (j != null) {
            try {
                j.put(BOOL, Boolean.valueOf(bool));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Boolean getBoolean(Message m) {
        JSONObject j;
        Boolean result = null;

        if (m != null) {
            try {
                j = new JSONObject(m.content);
                result = j.getBoolean(BOOL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setString(String message, JSONObject j) {
        if (message != null && j != null) {
            try {
                j.put(MESSAGE, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getString(Message m) {
        JSONObject j;
        String result = null;


        if (m != null) {
            try {
                j = new JSONObject(m.content);
                result = j.getString(MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setGamePlayer(GamePlayer p, JSONObject j) {
        if (p != null && j != null) {
            try {
                j.put(PLAYER, p.getMyName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public GamePlayer getGamePlayer(Message m) {
        JSONObject j;
        GamePlayer result = null;

        if (m != null) {
            try {
                j = new JSONObject(m.content);
                result = new BaseCoupPlayer(j.getString(PLAYER));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setCoupCardType(CoupCardType c, JSONObject j) {
        if (c != null && j != null) {
            try {
                j.put(CARDTYPE, c.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CoupCardType getCoupCardType(Message m) {
        JSONObject j;
        CoupCardType result = CoupCardType.Unknown;


        if (m != null) {
            try {
                j = new JSONObject(m.content);
                result = getCoupCardTypeFromJSONObject(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setCoupAction(CoupAction a, JSONObject j) {
        if (a != null && j != null) {
            JSONObject action = new JSONObject();
            try {
                action.put(ACTIONTYPE, a.getActionType().name());
                setGamePlayer(a.getActionOn(), j);
                j.put(ACTION, action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CoupAction getCoupAction(Message m) {
        JSONObject j;
        CoupAction result = null;


        if (m != null) {
            try {
                j = new JSONObject(m.content);
                String type = j.getString(ACTIONTYPE);
                GamePlayer player = getGamePlayer(m);
                result = new CoupAction(player, CoupAction.ActionType.valueOf(type));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setType(MessageType type, JSONObject j) {
        if (type != null && j != null) {
            try {
                j.put(TYPE, type.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MessageType getType(Message m) {
        JSONObject j;
        MessageType result = MessageType.unknown;


        if (m != null) {
            try {
                j = new JSONObject(m.content);
                String type = j.getString(TYPE);
                result = MessageType.valueOf(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setCoupCard(CoupCard card, JSONObject j) {
        if (card != null && j != null) {
            try {
                JSONObject jcard = makeCard(card);
                j.put(CARD, jcard);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CoupCard getCoupCard(Message m) {
        JSONObject j;
        CoupCard result = null;


        if (m != null) {
            try {
                j = new JSONObject(m.content);
                JSONObject card = j.getJSONObject(CARD);
                result = getCardFromJSONObject(card);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setCardMultiset(Multiset<CoupCard> cards, JSONObject j) {
        if (cards != null && j != null) {
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

    @Override
    public Multiset<CoupCard> getCardMultiset(Message m) {
        Multiset<CoupCard> result = HashMultiset.create();
        JSONObject j;

        if (m != null) {
            try {
                j = new JSONObject(m.content);
                JSONArray array = j.getJSONArray(CARDS);
                int len = array.length();
                for (int i = 0; i < len; i++) {
                    JSONObject jcard = array.getJSONObject(i);
                    CoupCard card = getCardFromJSONObject(jcard);
                    result.add(card);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setCoupGameContext(CoupGameContext gc, JSONObject j) {
        if (gc != null && j != null) {
            try {
                JSONObject playerInfos = makePlayerInfos(gc.getCoupPlayerInfos());
                JSONArray players = makePlayers(gc.getPlayers());

                JSONObject context = new JSONObject();
                context.put(PLAYERS, players);
                context.put(PLAYERINFOS, playerInfos);

                j.put(CONTEXT, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CoupGameContext getCoupGameContext(Message m) {
        CoupGameContext result = null;
        JSONObject j;

        if (m != null) {
            try {
                j = new JSONObject(m.content);

                JSONObject context = j.getJSONObject(CONTEXT);
                JSONArray players = context.getJSONArray(PLAYERS);
                List<GamePlayer> playerList = makePlayerListFromJSONObject(players);

                JSONObject playerInfos = context.getJSONObject(PLAYERINFOS);
                Map<GamePlayer, CoupPlayerInfo> playerInfoMap = makePlayerInfoMapFromJSONObject(playerInfos);

                result = new CoupGameContext(playerList, playerInfoMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private JSONArray makePlayers(List<GamePlayer> players) {
        JSONArray j = new JSONArray();

        for (GamePlayer p : players)
            j.put(p.getMyName());

        return j;
    }

    private List<GamePlayer> makePlayerListFromJSONObject(JSONArray j) {
        List<GamePlayer> result = Lists.newArrayList();

        if (j != null) {
            try {
                int len = j.length();
                for (int i = 0; i < len; i++) {
                    String name = j.getString(i);
                    result.add(new BaseCoupPlayer(name));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private JSONObject makePlayerInfos(Map<GamePlayer, CoupPlayerInfo> coupPlayerInfos) {
        JSONObject j = new JSONObject();
        try {
            JSONArray a = new JSONArray();
            for (GamePlayer player : coupPlayerInfos.keySet()) {
                CoupPlayerInfo info = coupPlayerInfos.get(player);
                JSONObject playerInfo = makePlayerInfo(info);
                String playerName = player.getMyName();

                JSONObject element = new JSONObject();
                element.put(playerName, playerInfo);

                a.put(element);
            }
            j.put(PLAYERINFOS, a);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }

    private Map<GamePlayer, CoupPlayerInfo> makePlayerInfoMapFromJSONObject(JSONObject j) {
        return null;
    }

    private JSONObject makePlayerInfo(CoupPlayerInfo coupPlayerInfo) {
        JSONObject j = new JSONObject();
        try {
            j.put(ACTIVE, coupPlayerInfo.active);
            j.put(COINCOUNT, coupPlayerInfo.coins);
            j.put(CARD1, makeCard(coupPlayerInfo.cardOne));
            j.put(CARD2, makeCard(coupPlayerInfo.cardTwo));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }

    private CoupPlayerInfo makePlayerInfoFromJSONObject(JSONObject j) {
        CoupPlayerInfo result = null;
        if (j != null) {
            try {
                boolean active = j.getBoolean(ACTIVE);
                int coins = j.getInt(COINCOUNT);
                CoupCard card1 = getCardFromJSONObject(j.getJSONObject(CARD1));
                CoupCard card2 = getCardFromJSONObject(j.getJSONObject(CARD2));

                result = new CoupPlayerInfo(active, coins, card1, card2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private CoupCardType getCoupCardTypeFromJSONObject(JSONObject j) {

        CoupCardType result = CoupCardType.Unknown;
        if (j != null) {
            try {
                String type = j.getString(CARDTYPE);
                result = CoupCardType.valueOf(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private CoupCard getCardFromJSONObject(JSONObject j) {
        CoupCardType cardType;
        CoupCard result = new CoupCard(CoupCardType.Unknown);
        boolean faceUp;

        if (j != null) {
            try {
                cardType = getCoupCardTypeFromJSONObject(j);
                faceUp = j.getBoolean(FACEUP);
                result = new CoupCard(cardType, faceUp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private JSONObject makeCard(CoupCard c) {

        JSONObject j = new JSONObject();
        if (c != null) {
            try {
                setCoupCardType(c.type, j);
                j.put(FACEUP, c.faceUp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return j;
    }
}
