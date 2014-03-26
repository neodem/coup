package com.neodem.coup.serverside.actionProcessors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.PlayerInfoState;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class ExchangeActionProcessor {
    private static Log log = LogFactory.getLog(ExchangeActionProcessor.class.getName());
    private ServerSideGameContext context;

    public ExchangeActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    /**
     * @param actingPlayer
     */
    public void handleExchange(CoupPlayer actingPlayer) {
        log.debug(String.format("%s is doing an exchange...", actingPlayer));

        PlayerInfoState currentPlayerInfo = context.getPlayerInfo(actingPlayer);

        CoupCard card1 = context.getDeck().takeCard();
        CoupCard card2 = context.getDeck().takeCard();

        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(card1);
        handCards.add(card2);
        handCards.addAll(currentPlayerInfo.getDownCards());

        Multiset<CoupCard> returnedCards = getReturnedCards(actingPlayer, currentPlayerInfo, handCards);

        log.debug(String.format("%s is putting back 2 cards", actingPlayer));

        // TODO update the info object!!!

        // put the cards back into the deck and shuffle it
        for (CoupCard c : returnedCards) {
            context.getDeck().putCard(c);
        }

        context.getDeck().shuffleDeck();
    }

    private Multiset<CoupCard> getReturnedCards(CoupPlayer p, PlayerInfoState info, Multiset<CoupCard> handCards) {

        // deal with exchange
        Multiset<CoupCard> returnedCards = p.exchangeCards(handCards);

        // collection must return 2 cards
        if (returnedCards.size() != 2) {
            p.tryAgain("You need to return " + 2 + " cards");
            return getReturnedCards(p, info, handCards);
        }

        // the returned collection must have come from the handCards. (eg. all returned cards must be in handCards)
        if (isReturnedCollectionOk(handCards, returnedCards)) {
            p.tryAgain("The returned cards need to come from the given hand cards");

            return getReturnedCards(p, info, handCards);
        }

        return returnedCards;
    }

    protected boolean isReturnedCollectionOk(Multiset<CoupCard> handCards, Multiset<CoupCard> returnedCards) {
        Multiset<CoupCard> intersection = Multisets.intersection(handCards, returnedCards);
        if (intersection.equals(returnedCards)) {
            return true;
        }
        return false;
    }
}