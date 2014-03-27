package com.neodem.coup.server.network;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.game.GamePlayer;
import com.neodem.coup.common.messaging.Message;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.messaging.MessageTransport;

import static com.neodem.coup.common.messaging.MessageType.*;
import static com.neodem.coup.common.messaging.MessageType.tryAgain;
import static com.neodem.coup.common.messaging.MessageType.updateContext;
import static com.neodem.coup.common.messaging.MessageType.yourTurn;

/**
 * This class will communicate with it's parter on the client side
 * via messages
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CommunicatingPlayer implements CoupPlayer {
    private String playerName;
    private MessageTranslator messageTranslator;
    private MessageTransport messageTransport;
    private int id;

    public CommunicatingPlayer(String playerName, int id,MessageTranslator messageTranslator, MessageTransport messageTransport) {
        this.playerName = playerName;
        this.id = id;
        this.messageTranslator = messageTranslator;
        this.messageTransport = messageTransport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunicatingPlayer)) return false;

        CommunicatingPlayer that = (CommunicatingPlayer) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public String toString() {
        return playerName;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public CoupAction yourTurn(CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(yourTurn, gc);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getCoupAction(reply);
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(updateContext, gc);
        messageTransport.send(id, m);
    }

    @Override
    public void actionHappened(GamePlayer player, CoupAction hisAction, CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(actionHappened, hisAction, player, gc);
        messageTransport.send(id, m);
    }

    @Override
    public void tryAgain(String reason) {
        Message m = messageTranslator.makeMessage(tryAgain, reason);
        messageTransport.send(id, m);
    }

    @Override
    public String getMyName() {
        return playerName;
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        Message m = messageTranslator.makeMessage(intializePlayer, g);
        messageTransport.send(id, m);
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, GamePlayer thePlayer, CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(counterAction, theAction, thePlayer, gc);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, GamePlayer thePlayer, CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(challengeAction, theAction, thePlayer, gc);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(GamePlayer playerCountering) {
        Message m = messageTranslator.makeMessage(challengeCounter, playerCountering);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType) {
        Message m = messageTranslator.makeMessage(proveCard, challengedCardType);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {
        Message m = messageTranslator.makeMessage(exchangeCards, cards);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getCardMultiset(reply);
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        Message m = messageTranslator.makeMessage(looseInfluence);
        Message reply = messageTransport.sendAndGetReply(id, m);
        return messageTranslator.getCoupCard(reply);
    }
}
