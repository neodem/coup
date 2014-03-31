package com.neodem.coup.common.messaging;

import com.google.common.collect.Multiset;
import com.neodem.bandaid.messaging.MessageTranslator;
import com.neodem.bandaid.messaging.MessageType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface CoupMessageTranslator extends MessageTranslator {

    String marshalMessage(MessageType type, CoupGameContext gc);

    String marshalMessage(MessageType type, CoupAction a);

    String marshalMessage(MessageType type, CoupAction a, String playerName, CoupGameContext gc);

    String marshalMessage(MessageType type, CoupCardType c);

    String marshalMessage(MessageType type, Multiset<CoupCard> cards);

    String marshalMessage(MessageType type, CoupCard card);

    String marshalPlayerMessage(MessageType type, String playerName);

    CoupAction unmarshalCoupActionFromMessage(String m);

    CoupCard unmarshalCoupCardFromMessage(String m);

    Multiset<CoupCard> unmarshalCardMultisetFromMessage(String m);

    CoupGameContext unmarshalCoupGameContextFromMessage(String m);

    CoupCardType unmarshalCoupCardTypeFromMessage(String m);

}
