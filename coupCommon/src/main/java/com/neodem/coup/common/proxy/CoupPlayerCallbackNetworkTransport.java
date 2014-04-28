package com.neodem.coup.common.proxy;

import com.google.common.collect.Multiset;
import com.neodem.bandaid.proxy.PlayerCallbackNetworkTransport;
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
 * Will make a CoupPlayerCallback communicate over the network to the server.
 * <p/>
 * Created by vfumo on 4/27/14.
 */
public class CoupPlayerCallbackNetworkTransport extends PlayerCallbackNetworkTransport {

    private static final Logger log = LogManager.getLogger(CoupPlayerCallbackNetworkTransport.class.getName());
    private final CoupMessageTranslator messageTranslator;
    private CoupPlayerCallback player;

    public CoupPlayerCallbackNetworkTransport(String hostname, CoupPlayerCallback player, CoupMessageTranslator messageTranslator) {
        super(hostname, player, messageTranslator);
        this.player = player;
        this.messageTranslator = messageTranslator;
    }

    @Override
    protected String handleGameMessageWithReply(int from, String m) {
        log.trace("handleGameMessageWithReply({},{})", from, m);

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

    @Override
    protected void handleGameMessage(int from, String m) {
        log.trace("handleGameMessage({},{})", from, m);

        CoupMessageType type = messageTranslator.unmarshalMessageTypeFromMessage(m);
        CoupGameContext gc;
        String playerName;
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
                playerName = messageTranslator.unmarshalPlayerNameFromMessage(m);
                a = messageTranslator.unmarshalCoupActionFromMessage(m);
                player.actionHappened(playerName, a, gc);
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
