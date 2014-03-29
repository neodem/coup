package com.neodem.coup.common.messaging;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.ComplexCoupAction;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.actions.CoupAction.ActionType;
import com.neodem.coup.common.game.actions.CoupActionFactory;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.game.player.CoupPlayerInfo;
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

    @Override
    public String makeMessage(MessageType type) {
        JSONObject j = new JSONObject();
        setType(type, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupGameContext(gc, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, String message) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setString(message, j);
        return j.toString();
    }

    @Override
    public String makeRegistrationMesage(String playerName) {
        JSONObject j = new JSONObject();
        setType(MessageType.register, j);
        setPlayerName(playerName, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, CoupAction a, String playerName, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupAction(a, j);
        setPlayerName(playerName, j);
        setCoupGameContext(gc, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, CoupAction a) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupAction(a, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, CoupCardType c) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupCardType(c, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, Multiset<CoupCard> cards) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCardMultiset(cards, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, CoupCard card) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setCoupCard(card, j);
        return j.toString();
    }

    @Override
    public String makeMessage(MessageType type, boolean bool) {
        JSONObject j = new JSONObject();
        setType(type, j);
        setBoolean(bool, j);
        return j.toString();
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
    public Boolean getBoolean(String m) {
        JSONObject j;
        Boolean result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
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
    public String getString(String m) {
        JSONObject j;
        String result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setPlayerName(String playerName, JSONObject j) {
        if (playerName != null && j != null) {
            try {
                j.put(PLAYER, playerName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected String getPlayerNameFromJSONObject(JSONObject j) {
        String result = null;
        if (j != null) {
            try {
                result = j.getString(PLAYER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String getPlayerName(String m) {
        JSONObject j;
        String result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(PLAYER);
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
    public CoupCardType getCoupCardType(String m) {
        JSONObject j;
        CoupCardType result = CoupCardType.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
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
                if (a instanceof ComplexCoupAction)
                    setPlayerName(((ComplexCoupAction) a).getActionOn(), action);
                j.put(ACTION, action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CoupAction getCoupAction(String m) {
        JSONObject j;
        CoupAction result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);

                JSONObject action = j.getJSONObject(ACTION);

                String type = action.getString(ACTIONTYPE);
                ActionType actionType = ActionType.valueOf(type);

                if (actionType.isSimple()) {
                    result = CoupActionFactory.newAction(actionType, null);
                } else {
                    String playerName = getPlayerNameFromJSONObject(action);
                    result = CoupActionFactory.newAction(actionType, playerName);
                }
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
    public MessageType getType(String m) {
        JSONObject j;
        MessageType result = MessageType.unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
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
    public CoupCard getCoupCard(String m) {
        JSONObject j;
        CoupCard result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
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
    public Multiset<CoupCard> getCardMultiset(String m) {
        Multiset<CoupCard> result = HashMultiset.create();
        JSONObject j;

        if (m != null) {
            try {
                j = new JSONObject(m);
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
    public CoupGameContext getCoupGameContext(String m) {
        CoupGameContext result = null;
        JSONObject j;

        if (m != null) {
            try {
                j = new JSONObject(m);

                JSONObject context = j.getJSONObject(CONTEXT);
                JSONArray players = context.getJSONArray(PLAYERS);
                List<String> playerList = makePlayerListFromJSONObject(players);

                JSONObject playerInfos = context.getJSONObject(PLAYERINFOS);
                Map<String, CoupPlayerInfo> playerInfoMap = makePlayerInfoMapFromJSONObject(playerInfos);

                result = new CoupGameContext(playerList, playerInfoMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private JSONArray makePlayers(List<String> players) {
        JSONArray j = new JSONArray();

        for (String p : players)
            j.put(p);

        return j;
    }

    private List<String> makePlayerListFromJSONObject(JSONArray j) {
        List<String> result = Lists.newArrayList();

        if (j != null) {
            try {
                int len = j.length();
                for (int i = 0; i < len; i++) {
                    String name = j.getString(i);
                    result.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private JSONObject makePlayerInfos(Map<String, CoupPlayerInfo> coupPlayerInfos) {
        JSONObject j = new JSONObject();
        try {
            JSONArray a = new JSONArray();
            for (String playerName : coupPlayerInfos.keySet()) {
                CoupPlayerInfo info = coupPlayerInfos.get(playerName);
                JSONObject playerInfo = makePlayerInfo(info);

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

    private Map<String, CoupPlayerInfo> makePlayerInfoMapFromJSONObject(JSONObject j) {
        Map<String, CoupPlayerInfo> result = Maps.newHashMap();

        if (j != null) {
            try {
                JSONArray array = j.getJSONArray(PLAYERINFOS);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject element = array.getJSONObject(i);
                    String playerName = (String) element.keys().next();
                    JSONObject playerInfoJSON = element.getJSONObject(playerName);

                    CoupPlayerInfo playerInfo = makePlayerInfoFromJSONObject(playerInfoJSON);

                    result.put(playerName, playerInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
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
