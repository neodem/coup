package com.neodem.coup.common.proxy;

import com.google.common.collect.Multiset;
import com.neodem.bandaid.proxy.PlayerCallbackProxy;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.messaging.CoupMessageTranslator;

import static com.neodem.coup.common.messaging.CoupMessageType.*;

/**
 * This class will communicate with it's parter on the client side
 * via messages and simulate a CoupPlayerCallback object serverside.
 * * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupPlayerCallbackProxy extends PlayerCallbackProxy implements CoupPlayerCallback {

    protected CoupMessageTranslator coupMessageTranslator;

    public CoupPlayerCallbackProxy(int clientNetworkId, String playerName, CoupMessageTranslator coupMessageTranslator) {
        super(clientNetworkId, playerName, coupMessageTranslator);
        this.coupMessageTranslator = coupMessageTranslator;
    }

    @Override
    public CoupAction yourTurn(CoupGameContext gc) {
        String m = coupMessageTranslator.marshalMessage(yourTurn, gc);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalCoupActionFromMessage(reply);
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        String m = coupMessageTranslator.marshalMessage(updateContext, gc);
        sendGameMessage(clientNetworkId, m);
    }

    @Override
    public void actionHappened(String playerName, CoupAction hisAction, CoupGameContext gc) {
        String m = coupMessageTranslator.marshalMessage(actionHappened, hisAction, playerName, gc);
        sendGameMessage(clientNetworkId, m);
    }

    @Override
    public void tryAgain(String reason) {
        String m = coupMessageTranslator.marshalMessage(tryAgain, reason);
        sendGameMessage(clientNetworkId, m);
    }

    @Override
    public void messageFromGM(String message) {
        String m = coupMessageTranslator.marshalMessage(gmMessage, message);
        sendGameMessage(clientNetworkId, m);
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        String m = coupMessageTranslator.marshalMessage(intializePlayer, g);
        sendGameMessage(clientNetworkId, m);
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        String m = coupMessageTranslator.marshalMessage(counterAction, theAction, thePlayer, gc);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        String m = coupMessageTranslator.marshalMessage(challengeAction, theAction, thePlayer, gc);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(String playerCountering) {
        String m = coupMessageTranslator.marshalPlayerMessage(challengeCounter, playerCountering);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType) {
        String m = coupMessageTranslator.marshalMessage(proveCard, challengedCardType);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalBooleanFromMessage(reply);
    }

    @Override
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {
        String m = coupMessageTranslator.marshalMessage(exchangeCards, cards);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalCardMultisetFromMessage(reply);
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        String m = coupMessageTranslator.marshalMessage(looseInfluence);
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return coupMessageTranslator.unmarshalCoupCardFromMessage(reply);
    }
}
