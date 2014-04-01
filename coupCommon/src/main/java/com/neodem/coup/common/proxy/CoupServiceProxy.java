package com.neodem.coup.common.proxy;

import com.google.common.collect.Multiset;
import com.neodem.bandaid.proxy.ServiceProxy;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.messaging.CoupMessageTranslator;
import com.neodem.coup.common.messaging.CoupMessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.neodem.coup.common.messaging.CoupMessageType.reply;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupServiceProxy extends ServiceProxy {

    private static final Logger log = LogManager.getLogger(CoupServiceProxy.class.getName());
    private final CoupMessageTranslator messageTranslator;
    private final CoupPlayerCallback player;

    public CoupServiceProxy(CoupPlayerCallback target, CoupMessageTranslator messageTranslator, String host, int port) {
        super(target, host, port);
        this.player = target;
        this.messageTranslator = messageTranslator;
    }

    public String handleMessageWithReply(String m) {
        CoupMessageType type = messageTranslator.unmarshalMessageTypeFromMessage(m);
        String replyMessage = null;

        CoupGameContext gc;
        String playerName;

        CoupAction a;
        boolean bool;
        CoupCardType cardType;
        CoupCard card;

        switch (type) {
            case yourTurn:
                gc = messageTranslator.unmarshalCoupGameContextFromMessage(m);
                a = player.yourTurn(gc);
                replyMessage = messageTranslator.marshalMessage(reply, a);
                break;
            case counterAction:
                gc = messageTranslator.unmarshalCoupGameContextFromMessage(m);
                a = messageTranslator.unmarshalCoupActionFromMessage(m);
                playerName = messageTranslator.unmarshalPlayerNameFromMessage(m);
                bool = player.doYouWantToCounterThisAction(a, playerName, gc);
                replyMessage = messageTranslator.marshalMessage(reply, bool);
                break;
            case challengeAction:
                gc = messageTranslator.unmarshalCoupGameContextFromMessage(m);
                a = messageTranslator.unmarshalCoupActionFromMessage(m);
                playerName = messageTranslator.unmarshalPlayerNameFromMessage(m);
                bool = player.doYouWantToChallengeThisAction(a, playerName, gc);
                replyMessage = messageTranslator.marshalMessage(reply, bool);
                break;
            case challengeCounter:
                playerName = messageTranslator.unmarshalPlayerNameFromMessage(m);
                bool = player.doYouWantToChallengeThisCounter(playerName);
                replyMessage = messageTranslator.marshalMessage(reply, bool);
                break;
            case proveCard:
                cardType = messageTranslator.unmarshalCoupCardTypeFromMessage(m);
                bool = player.doYouWantToProveYouHaveACardOfThisType(cardType);
                replyMessage = messageTranslator.marshalMessage(reply, bool);
                break;
            case looseInfluence:
                card = player.youMustLooseAnInfluence();
                replyMessage = messageTranslator.marshalMessage(reply, card);
                break;
            case exchangeCards:
                Multiset<CoupCard> cards = messageTranslator.unmarshalCardMultisetFromMessage(m);
                Multiset<CoupCard> hand = player.exchangeCards(cards);
                replyMessage = messageTranslator.marshalMessage(reply, hand);
                break;
        }

        return replyMessage;
    }

    public void handleAsynchonousMessage(String m) {
        CoupMessageType type = messageTranslator.unmarshalMessageTypeFromMessage(m);
        CoupGameContext gc;
        String playerNane;
        CoupAction a;
        String message;

        switch (type) {
            case updateContext:
                gc = messageTranslator.unmarshalCoupGameContextFromMessage(m);
                player.updateContext(gc);
                break;
            case tryAgain:
                message = messageTranslator.unmarshalStringFromMessage(m);
                player.tryAgain(message);
                break;
            case actionHappened:
                gc = messageTranslator.unmarshalCoupGameContextFromMessage(m);
                playerNane = messageTranslator.unmarshalPlayerNameFromMessage(m);
                a = messageTranslator.unmarshalCoupActionFromMessage(m);
                player.actionHappened(playerNane, a, gc);
                break;
            case intializePlayer:
                gc = messageTranslator.unmarshalCoupGameContextFromMessage(m);
                player.initializePlayer(gc);
                break;
            case gmMessage:
                message = messageTranslator.unmarshalStringFromMessage(m);
                player.messageFromGM(message);
        }
    }
}