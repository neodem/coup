package com.neodem.coup.common.proxy;

import com.google.common.collect.Multiset;
import com.neodem.bandaid.network.ComInterface;
import com.neodem.bandaid.proxy.PlayerProxy;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.messaging.CoupMessageTranslator;

import static com.neodem.bandaid.messaging.MessageType.*;

/**
 * This class will communicate with it's parter on the client side
 * via messages
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupPlayerProxy extends PlayerProxy implements CoupPlayerCallback {

    protected CoupMessageTranslator messageTranslator;

    public CoupPlayerProxy(String playerName, int networkId, CoupMessageTranslator messageTranslator, ComInterface server) {
        super(playerName, networkId, server);
        this.messageTranslator = messageTranslator;
    }

    @Override
    public CoupAction yourTurn(CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(yourTurn, gc);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalCoupActionFromMessage(reply);
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(updateContext, gc);
        server.sendMessage(networkId, m);
    }

    @Override
    public void actionHappened(String playerName, CoupAction hisAction, CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(actionHappened, hisAction, playerName, gc);
        server.sendMessage(networkId, m);
    }

    @Override
    public void tryAgain(String reason) {
        String m = messageTranslator.marshalMessage(tryAgain, reason);
        server.sendMessage(networkId, m);
    }

    @Override
    public void messageFromGM(String message) {
        String m = messageTranslator.marshalMessage(gmMessage, message);
        server.sendMessage(networkId, m);
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        String m = messageTranslator.marshalMessage(intializePlayer, g);
        server.sendMessage(networkId, m);
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(counterAction, theAction, thePlayer, gc);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        String m = messageTranslator.marshalMessage(challengeAction, theAction, thePlayer, gc);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(String playerCountering) {
        String m = messageTranslator.marshalPlayerMessage(challengeCounter, playerCountering);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType) {
        String m = messageTranslator.marshalMessage(proveCard, challengedCardType);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {
        String m = messageTranslator.marshalMessage(exchangeCards, cards);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalCardMultisetFromMessage(reply);
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        String m = messageTranslator.marshalMessage(looseInfluence);
        String reply = server.sendAndGetReply(networkId, m);
        return messageTranslator.unmarshalCoupCardFromMessage(reply);
    }
}
