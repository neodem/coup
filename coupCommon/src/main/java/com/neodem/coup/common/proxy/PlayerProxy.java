package com.neodem.coup.common.proxy;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.messaging.MessageTranslator;
import com.neodem.coup.common.network.ComBaseClient.Dest;
import com.neodem.coup.common.network.ComInterface;

import static com.neodem.coup.common.messaging.MessageType.*;

/**
 * This class will communicate with it's parter on the client side
 * via messages
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class PlayerProxy implements CoupCommunicationInterface {
    private String playerName;
    private MessageTranslator messageTranslator;
    private ComInterface coupServer;
    private Dest id;

    public PlayerProxy(String playerName, Dest id, MessageTranslator messageTranslator, ComInterface coupServer) {
        this.playerName = playerName;
        this.id = id;
        this.messageTranslator = messageTranslator;
        this.coupServer = coupServer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerProxy)) return false;

        PlayerProxy that = (PlayerProxy) o;

        if (id != that.id) return false;
        if (playerName != null ? !playerName.equals(that.playerName) : that.playerName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerName != null ? playerName.hashCode() : 0;
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return playerName;
    }

    @Override
    public CoupAction yourTurn(CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(yourTurn, gc);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalCoupActionFromMessage(reply);
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(updateContext, gc);
        coupServer.sendMessage(id, m);
    }

    @Override
    public void actionHappened(String playerName, CoupAction hisAction, CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(actionHappened, hisAction, playerName, gc);
        coupServer.sendMessage(id, m);
    }

    @Override
    public void tryAgain(String reason) {
        String m = messageTranslator.marshalMessage(tryAgain, reason);
        coupServer.sendMessage(id, m);
    }

    @Override
    public void messageFromGM(String message) {
        String m = messageTranslator.marshalMessage(gmMessage, message);
        coupServer.sendMessage(id, m);
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        String m = messageTranslator.marshalMessage(intializePlayer, g);
        coupServer.sendMessage(id, m);
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(counterAction, theAction, thePlayer, gc);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(challengeAction, theAction, thePlayer, gc);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(String playerCountering) {
        String m = messageTranslator.marshalPlayerMessage(challengeCounter, playerCountering);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType) {
        String m = messageTranslator.marshalMessage(proveCard, challengedCardType);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {
        String m = messageTranslator.marshalMessage(exchangeCards, cards);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalCardMultisetFromMessage(reply);
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        String m = messageTranslator.marshalMessage(looseInfluence);
        String reply = coupServer.sendAndGetReply(id, m);
        return messageTranslator.unmarshalCoupCardFromMessage(reply);
    }
}
