package com.neodem.coup.client.network;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.game.GamePlayer;
import com.neodem.coup.common.messaging.Message;
import com.neodem.coup.common.messaging.MessageClient;
import com.neodem.coup.common.messaging.MessageTranslator;

import static com.neodem.coup.common.messaging.MessageType.reply;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class ServiceProxy implements MessageClient {

    private final MessageTranslator messageTranslator;
    private CoupPlayer player;

    public ServiceProxy(CoupPlayer target, MessageTranslator messageTranslator) {
        this.player = target;
        this.messageTranslator = messageTranslator;
    }

    @Override
    public Message handleMessageWithReply(Message m) {
        Message replyMessage = null;

        CoupGameContext gc;
        GamePlayer p;
        CoupAction a;
        boolean bool;
        CoupCardType cardType;
        CoupCard card;

        switch (messageTranslator.getType(m)) {
            case yourTurn:
                gc = messageTranslator.getCoupGameContext(m);
                a = player.yourTurn(gc);
                replyMessage = messageTranslator.makeMessage(reply, a);
                break;
            case counterAction:
                gc = messageTranslator.getCoupGameContext(m);
                a = messageTranslator.getCoupAction(m);
                p = messageTranslator.getPlayer(m);
                bool = player.doYouWantToCounterThisAction(a, p, gc);
                replyMessage = messageTranslator.makeMessage(reply, bool);
                break;
            case challengeAction:
                gc = messageTranslator.getCoupGameContext(m);
                a = messageTranslator.getCoupAction(m);
                p = messageTranslator.getPlayer(m);
                bool = player.doYouWantToChallengeThisAction(a, p, gc);
                replyMessage = messageTranslator.makeMessage(reply, bool);
                break;
            case challengeCounter:
                p = messageTranslator.getPlayer(m);
                bool = player.doYouWantToChallengeThisCounter(p);
                replyMessage = messageTranslator.makeMessage(reply, bool);
                break;
            case proveCard:
                cardType = messageTranslator.getCoupCardType(m);
                bool = player.doYouWantToProveYouHaveACardOfThisType(cardType);
                replyMessage = messageTranslator.makeMessage(reply, bool);
                break;
            case looseInfluence:
                card = player.youMustLooseAnInfluence();
                replyMessage = messageTranslator.makeMessage(reply, card);
                break;
            case exchangeCards:
                Multiset<CoupCard> cards = messageTranslator.getCardMultiset(m);
                Multiset<CoupCard> hand = player.exchangeCards(cards);
                replyMessage = messageTranslator.makeMessage(reply, hand);
                break;
        }

        return replyMessage;
    }

    @Override
    public void getAsynchonousMessage(Message m) {
        CoupGameContext gc;
        GamePlayer p;
        CoupAction a;
        String message;

        switch (messageTranslator.getType(m)) {
            case updateContext:
                gc = messageTranslator.getCoupGameContext(m);
                player.updateContext(gc);
                break;
            case tryAgain:
                message = messageTranslator.getString(m);
                player.tryAgain(message);
                break;
            case actionHappened:
                p = messageTranslator.getPlayer(m);
                a = messageTranslator.getCoupAction(m);
                gc = messageTranslator.getCoupGameContext(m);
                player.actionHappened(p, a, gc);
                break;
            case intializePlayer:
                gc = messageTranslator.getCoupGameContext(m);
                player.initializePlayer(gc);
                break;
        }
    }
}
