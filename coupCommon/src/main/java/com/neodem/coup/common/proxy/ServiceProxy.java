package com.neodem.coup.common.proxy;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.messaging.CoupMessageTranslator;
import com.neodem.coup.common.messaging.MessageType;
import com.neodem.coup.common.network.ComBaseClient;
import com.neodem.coup.common.network.ComServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.neodem.coup.common.messaging.MessageType.reply;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class ServiceProxy extends ComBaseClient {

    private static final Logger log = LogManager.getLogger(ServiceProxy.class.getName());
    private final CoupMessageTranslator messageTranslator;
    private final CoupCommunicationInterface player;

    public ServiceProxy(CoupCommunicationInterface target, CoupMessageTranslator messageTranslator, String host, int port) {
        super(host, port);
        this.player = target;
        this.messageTranslator = messageTranslator;
    }

    @Override
    public void init() {
        super.init();
        String m = messageTranslator.marshalRegistrationMesage(player.getPlayerName());
        log.debug("{} : registering with the server : {}", player.getPlayerName(), reply);
        send(ComServer.Server, m);
    }

    @Override
    public void handleMessage(int from, String m) {
        log.trace("{} : message received from {} : {}", player.getPlayerName(), from, m);

        MessageType type = messageTranslator.unmarshalMessageTypeFromMessage(m);

        if (type.requiresReply()) {
            String reply = handleMessageWithReply(type, m);
            log.trace("{} : replying to server : {}", player.getPlayerName(), reply);
            send(ComServer.Server, reply);
        } else {
            handleAsynchonousMessage(type, m);
        }
    }

    public String handleMessageWithReply(MessageType type, String m) {
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

    public void handleAsynchonousMessage(MessageType type, String m) {
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
