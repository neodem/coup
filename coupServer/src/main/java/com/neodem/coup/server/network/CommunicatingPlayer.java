package com.neodem.coup.server.network;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.messaging.CoupServer;
import com.neodem.coup.common.messaging.Message;
import com.neodem.coup.common.messaging.MessageTranslator;

import static com.neodem.coup.common.messaging.MessageType.*;

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
    private CoupServer coupServer;
    private int id;

    public CommunicatingPlayer(String playerName, int id, MessageTranslator messageTranslator, CoupServer coupServer) {
        this.playerName = playerName;
        this.id = id;
        this.messageTranslator = messageTranslator;
        this.coupServer = coupServer;
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
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getCoupAction(reply);
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(updateContext, gc);
        coupServer.send(id, m);
    }

    @Override
    public void actionHappened(String playerName, CoupAction hisAction, CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(actionHappened, hisAction, playerName, gc);
        coupServer.send(id, m);
    }

    @Override
    public void tryAgain(String reason) {
        Message m = messageTranslator.makeMessage(tryAgain, reason);
        coupServer.send(id, m);
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        Message m = messageTranslator.makeMessage(intializePlayer, g);
        coupServer.send(id, m);
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(counterAction, theAction, thePlayer, gc);
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        Message m = messageTranslator.makeMessage(challengeAction, theAction, thePlayer, gc);
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(String playerCountering) {
        Message m = messageTranslator.makeMessage(challengeCounter, playerCountering);
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType) {
        Message m = messageTranslator.makeMessage(proveCard, challengedCardType);
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getBoolean(reply);
    }

    @Override
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {
        Message m = messageTranslator.makeMessage(exchangeCards, cards);
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getCardMultiset(reply);
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        Message m = messageTranslator.makeMessage(looseInfluence);
        Message reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.getCoupCard(reply);
    }
}
