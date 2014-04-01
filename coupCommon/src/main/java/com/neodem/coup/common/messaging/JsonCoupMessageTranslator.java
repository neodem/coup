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
public class JsonCoupMessageTranslator implements CoupMessageTranslator {

    private static final String CONTEXT = "GameContext";
    private static final String ACTION = "Action";
    private static final String CARDTYPE = "CardType";
    private static final String CARD = "Card";
    private static final String ACTIONTYPE = "ActionType";
    private static final String FACEUP = "FaceUp";
    private static final String CARDS = "CardCollection";
    private static final String PLAYERS = "Players";
    private static final String PLAYERINFOS = "PlayerInfos";
    private static final String ACTIVE = "Active";
    private static final String COINCOUNT = "CoinCount";
    private static final String CARD1 = "Card1";
    private static final String CARD2 = "Card2";
    private static final String CARDID = "CardId";

    private static final String TYPE = "CoupMessageType";
    private static final String MESSAGE = "Message";
    private static final String PLAYER = "PlayerName";
    private static final String BOOL = "TrueFalse";

    @Override
    public String marshalMessage(CoupMessageType type) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(CoupMessageType type, String message) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setStringIntoJSONObject(message, j);
        return j.toString();
    }

    @Override
    public String marshalPlayerMessage(CoupMessageType type, String playerName) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setPlayerNameIntoJSONObject(playerName, j);
        return j.toString();
    }


    @Override
    public String marshalMessage(CoupMessageType type, boolean bool) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setBooleanIntoJSONObject(bool, j);
        return j.toString();
    }

    @Override
    public Boolean unmarshalBooleanFromMessage(String m) {
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

    @Override
    public String unmarshalStringFromMessage(String m) {
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

    @Override
    public String unmarshalPlayerNameFromMessage(String m) {
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

    @Override
    public CoupMessageType unmarshalMessageTypeFromMessage(String m) {
        JSONObject j;
        CoupMessageType result = CoupMessageType.unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                String type = j.getString(TYPE);
                result = CoupMessageType.valueOf(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected String unmarshalPlayerNameFromJSONObject(JSONObject j) {
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

    protected void setBooleanIntoJSONObject(boolean bool, JSONObject j) {
        if (j != null) {
            try {
                j.put(BOOL, Boolean.valueOf(bool));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setStringIntoJSONObject(String message, JSONObject j) {
        if (message != null && j != null) {
            try {
                j.put(MESSAGE, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setPlayerNameIntoJSONObject(String playerName, JSONObject j) {
        if (playerName != null && j != null) {
            try {
                j.put(PLAYER, playerName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setMessageTypeIntoJSONObject(CoupMessageType type, JSONObject j) {
        if (type != null && j != null) {
            try {
                j.put(TYPE, type.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String marshalMessage(CoupMessageType type, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setCoupGameContextIntoJSONObject(gc, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(CoupMessageType type, CoupAction a, String playerName, CoupGameContext gc) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setCoupActionIntoJSONObject(a, j);
        setPlayerNameIntoJSONObject(playerName, j);
        setCoupGameContextIntoJSONObject(gc, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(CoupMessageType type, CoupAction a) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setCoupActionIntoJSONObject(a, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(CoupMessageType type, CoupCardType c) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setCoupCardTypeIntoJSONObject(c, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(CoupMessageType type, Multiset<CoupCard> cards) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setCardMultisetIntoJSONObject(cards, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(CoupMessageType type, CoupCard card) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setCoupCardIntoJSONObject(card, j);
        return j.toString();
    }

    @Override
    public CoupAction unmarshalCoupActionFromMessage(String m) {
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
                    String playerName = unmarshalPlayerNameFromJSONObject(action);
                    result = CoupActionFactory.newAction(actionType, playerName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public CoupCard unmarshalCoupCardFromMessage(String m) {
        JSONObject j;
        CoupCard result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                JSONObject card = j.getJSONObject(CARD);
                result = unmarshalCoupCardFromJSONObject(card);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public Multiset<CoupCard> unmarshalCardMultisetFromMessage(String m) {
        Multiset<CoupCard> result = HashMultiset.create();
        JSONObject j;

        if (m != null) {
            try {
                j = new JSONObject(m);
                JSONArray array = j.getJSONArray(CARDS);
                int len = array.length();
                for (int i = 0; i < len; i++) {
                    JSONObject jcard = array.getJSONObject(i);
                    CoupCard card = unmarshalCoupCardFromJSONObject(jcard);
                    result.add(card);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public CoupGameContext unmarshalCoupGameContextFromMessage(String m) {
        CoupGameContext result = null;
        JSONObject j;

        if (m != null) {
            try {
                j = new JSONObject(m);

                JSONObject context = j.getJSONObject(CONTEXT);
                JSONArray players = context.getJSONArray(PLAYERS);
                List<String> playerList = unmarshalPlayerListFromJSONObject(players);

                JSONObject playerInfos = context.getJSONObject(PLAYERINFOS);
                Map<String, CoupPlayerInfo> playerInfoMap = unmarshalPlayerInfoMapFromJSONObject(playerInfos);

                result = new CoupGameContext(playerList, playerInfoMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public CoupCardType unmarshalCoupCardTypeFromMessage(String m) {
        JSONObject j;
        CoupCardType result = CoupCardType.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = unmarshalCoupCardTypeFromJSONObject(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected JSONArray marshalPlayerListToJSONArray(List<String> players) {
        JSONArray j = new JSONArray();

        for (String p : players)
            j.put(p);

        return j;
    }

    protected List<String> unmarshalPlayerListFromJSONObject(JSONArray j) {
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

    protected JSONObject marshalPlayerInfosToJSONObject(Map<String, CoupPlayerInfo> coupPlayerInfos) {
        JSONObject j = new JSONObject();
        try {
            JSONArray a = new JSONArray();
            for (String playerName : coupPlayerInfos.keySet()) {
                CoupPlayerInfo info = coupPlayerInfos.get(playerName);
                JSONObject playerInfo = marshalPlayerInfoToJSONObject(info);

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

    protected Map<String, CoupPlayerInfo> unmarshalPlayerInfoMapFromJSONObject(JSONObject j) {
        Map<String, CoupPlayerInfo> result = Maps.newHashMap();

        if (j != null) {
            try {
                JSONArray array = j.getJSONArray(PLAYERINFOS);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject element = array.getJSONObject(i);
                    String playerName = (String) element.keys().next();
                    JSONObject playerInfoJSON = element.getJSONObject(playerName);

                    CoupPlayerInfo playerInfo = unmarshalCoupPlayerInfoFromJSONObject(playerInfoJSON);

                    result.put(playerName, playerInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected JSONObject marshalPlayerInfoToJSONObject(CoupPlayerInfo coupPlayerInfo) {
        JSONObject j = new JSONObject();
        try {
            j.put(ACTIVE, coupPlayerInfo.isActive());
            j.put(COINCOUNT, coupPlayerInfo.getCoinCount());
            j.put(CARD1, marshalCoupCardToJSONObject(coupPlayerInfo.getCardOne()));
            j.put(CARD2, marshalCoupCardToJSONObject(coupPlayerInfo.getCardTwo()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }

    protected CoupPlayerInfo unmarshalCoupPlayerInfoFromJSONObject(JSONObject j) {
        CoupPlayerInfo result = null;
        if (j != null) {
            try {
                boolean active = j.getBoolean(ACTIVE);
                int coins = j.getInt(COINCOUNT);
                CoupCard card1 = unmarshalCoupCardFromJSONObject(j.getJSONObject(CARD1));
                CoupCard card2 = unmarshalCoupCardFromJSONObject(j.getJSONObject(CARD2));

                result = new CoupPlayerInfo(active, coins, card1, card2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected CoupCardType unmarshalCoupCardTypeFromJSONObject(JSONObject j) {

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

    protected CoupCard unmarshalCoupCardFromJSONObject(JSONObject j) {
        CoupCardType cardType;
        CoupCard result = CoupCard.newUnknownCard();
        boolean faceUp;

        if (j != null) {
            try {
                cardType = unmarshalCoupCardTypeFromJSONObject(j);
                faceUp = j.getBoolean(FACEUP);
                int id = j.getInt(CARDID);
                result = new CoupCard(id, cardType, faceUp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected JSONObject marshalCoupCardToJSONObject(CoupCard c) {

        JSONObject j = new JSONObject();
        if (c != null) {
            try {
                setCoupCardTypeIntoJSONObject(c.getCardType(), j);
                j.put(FACEUP, c.isFaceUp());
                j.put(CARDID, c.getCardId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return j;
    }

    protected void setCoupCardTypeIntoJSONObject(CoupCardType c, JSONObject j) {
        if (c != null && j != null) {
            try {
                j.put(CARDTYPE, c.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setCoupActionIntoJSONObject(CoupAction a, JSONObject j) {
        if (a != null && j != null) {
            JSONObject action = new JSONObject();
            try {
                action.put(ACTIONTYPE, a.getActionType().name());
                if (a instanceof ComplexCoupAction)
                    setPlayerNameIntoJSONObject(((ComplexCoupAction) a).getActionOn(), action);
                j.put(ACTION, action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setCoupCardIntoJSONObject(CoupCard card, JSONObject j) {
        if (card != null && j != null) {
            try {
                JSONObject jcard = marshalCoupCardToJSONObject(card);
                j.put(CARD, jcard);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setCardMultisetIntoJSONObject(Multiset<CoupCard> cards, JSONObject j) {
        if (cards != null && j != null) {
            try {
                JSONArray array = new JSONArray();
                for (CoupCard c : cards)
                    array.put(marshalCoupCardToJSONObject(c));
                j.put(CARDS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setCoupGameContextIntoJSONObject(CoupGameContext gc, JSONObject j) {
        if (gc != null && j != null) {
            try {
                JSONObject playerInfos = marshalPlayerInfosToJSONObject(gc.getCoupPlayerInfos());
                JSONArray players = marshalPlayerListToJSONArray(gc.getPlayers());

                JSONObject context = new JSONObject();
                context.put(PLAYERS, players);
                context.put(PLAYERINFOS, playerInfos);

                j.put(CONTEXT, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
