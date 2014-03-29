package com.neodem.coup.client.network;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageType;
import com.neodem.coup.communications.ComBaseClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.neodem.coup.common.messaging.MessageType.reply;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class ServiceProxy extends ComBaseClient {

    private static Logger log = LogManager.getLogger(ServiceProxy.class.getName());

    private final MessageTranslator messageTranslator;
    private CoupCommunicationInterface player;

    public ServiceProxy(CoupCommunicationInterface target, MessageTranslator messageTranslator, String host, int port) {
        super(host, port);
        this.player = target;
        this.messageTranslator = messageTranslator;
    }

    @Override
    public void init() {
        super.init();
        String m = messageTranslator.makeRegistrationMesage(player.getPlayerName());
        log.debug("{} : registering with the server : {}", player.getPlayerName(), reply);
        send(Dest.Server, m);
    }

    @Override
    public void handleMessage(String m) {
        log.trace("{} : message received : {}", player.getPlayerName(), m);

        MessageType type = messageTranslator.getType(m);

        if (type.requiresReply()) {
            String reply = handleMessageWithReply(type, m);
            log.debug("{} : replying to server : {}", player.getPlayerName(), reply);
            send(Dest.Server, reply);
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
                gc = messageTranslator.getCoupGameContext(m);
                a = player.yourTurn(gc);
                replyMessage = messageTranslator.makeMessage(reply, a);
                break;
            case counterAction:
                gc = messageTranslator.getCoupGameContext(m);
                a = messageTranslator.getCoupAction(m);
                playerName = messageTranslator.getPlayerName(m);
                bool = player.doYouWantToCounterThisAction(a, playerName, gc);
                replyMessage = messageTranslator.makeMessage(reply, bool);
                break;
            case challengeAction:
                gc = messageTranslator.getCoupGameContext(m);
                a = messageTranslator.getCoupAction(m);
                playerName = messageTranslator.getPlayerName(m);
                bool = player.doYouWantToChallengeThisAction(a, playerName, gc);
                replyMessage = messageTranslator.makeMessage(reply, bool);
                break;
            case challengeCounter:
                playerName = messageTranslator.getPlayerName(m);
                bool = player.doYouWantToChallengeThisCounter(playerName);
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

    public void handleAsynchonousMessage(MessageType type, String m) {
        CoupGameContext gc;
        String playerNane;
        CoupAction a;
        String message;

        switch (type) {
            case updateContext:
                gc = messageTranslator.getCoupGameContext(m);
                player.updateContext(gc);
                break;
            case tryAgain:
                message = messageTranslator.getString(m);
                player.tryAgain(message);
                break;
            case actionHappened:
                playerNane = messageTranslator.getPlayerName(m);
                a = messageTranslator.getCoupAction(m);
                gc = messageTranslator.getCoupGameContext(m);
                player.actionHappened(playerNane, a, gc);
                break;
            case intializePlayer:
                gc = messageTranslator.getCoupGameContext(m);
                player.initializePlayer(gc);
                break;
        }
    }
}
