package com.neodem.coup.common.messaging;

import com.google.common.collect.Multiset;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface CoupMessageTranslator extends ServerMessageTranslator {

    String marshalMessage(CoupMessageType type, CoupGameContext gc);

    String marshalMessage(CoupMessageType type, CoupAction a);

    String marshalMessage(CoupMessageType type, CoupAction a, String playerName, CoupGameContext gc);

    String marshalMessage(CoupMessageType type, CoupCardType c);

    String marshalMessage(CoupMessageType type, Multiset<CoupCard> cards);

    String marshalMessage(CoupMessageType type, CoupCard card);

    CoupAction unmarshalCoupActionFromMessage(String m);

    CoupCard unmarshalCoupCardFromMessage(String m);

    Multiset<CoupCard> unmarshalCardMultisetFromMessage(String m);

    CoupGameContext unmarshalCoupGameContextFromMessage(String m);

    CoupCardType unmarshalCoupCardTypeFromMessage(String m);

    String marshalMessage(CoupMessageType type);

    String marshalMessage(CoupMessageType type, String message);

    String marshalMessage(CoupMessageType type, boolean bool);

    String marshalPlayerMessage(CoupMessageType type, String playerName);

    Boolean unmarshalBooleanFromMessage(String m);

    String unmarshalStringFromMessage(String m);

    String unmarshalPlayerNameFromMessage(String m);

    CoupMessageType unmarshalMessageTypeFromMessage(String m);

}
