package com.neodem.coup.common.messaging;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface MessageTranslator {

    Message makeMessage(MessageType type);

    Message makeMessage(MessageType type, CoupGameContext gc);

    Message makeMessage(MessageType type, String message);

    Message makeMessage(MessageType type, CoupPlayer p);

    Message makeMessage(MessageType type, CoupAction a);

    Message makeMessage(MessageType type, CoupAction a, CoupPlayer p, CoupGameContext gc);

    Message makeMessage(MessageType type, CoupCardType c);

    Message makeMessage(MessageType type, Multiset<CoupCard> cards);

    Message makeMessage(MessageType type, CoupCard card);

    Message makeMessage(MessageType type, boolean bool);

    CoupAction getCoupAction(Message m);

    Boolean getBoolean(Message m);

    CoupCard getCoupCard(Message m);

    Multiset<CoupCard> getCardMultiset(Message m);

    CoupGameContext getCoupGameContext(Message m);

    CoupPlayer getCoupPlayer(Message m);

    String getString(Message m);

    CoupCardType getCoupCardType(Message m);

    MessageType getType(Message m);
}
