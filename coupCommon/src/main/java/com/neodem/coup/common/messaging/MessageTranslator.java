package com.neodem.coup.common.messaging;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface MessageTranslator {

    String makeMessage(MessageType type);

    String makeMessage(MessageType type, CoupGameContext gc);

    String makeMessage(MessageType type, String message);

    String makeMessage(MessageType type, CoupAction a);

    String makeMessage(MessageType type, CoupAction a, String playerName, CoupGameContext gc);

    String makeMessage(MessageType type, CoupCardType c);

    String makeMessage(MessageType type, Multiset<CoupCard> cards);

    String makeMessage(MessageType type, CoupCard card);

    String makeMessage(MessageType type, boolean bool);

    String makePlayerMessage(MessageType type, String playerName);

    CoupAction getCoupAction(String m);

    Boolean getBoolean(String m);

    CoupCard getCoupCard(String m);

    Multiset<CoupCard> getCardMultiset(String m);

    CoupGameContext getCoupGameContext(String m);

    String getString(String m);

    String getPlayerName(String m);

    CoupCardType getCoupCardType(String m);

    MessageType getType(String m);

    String makeRegistrationMesage(String playerName);


}
