package com.neodem.coup.server.game.actionProcessors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.server.game.PlayerInfoState;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class ExchangeActionProcessor extends BaseActionProcessor implements ActionProcessor {
    private static Logger log = LogManager.getLogger(ExchangeActionProcessor.class.getName());

    public ExchangeActionProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public void process(CoupPlayer actingPlayer, String targetPlayerName, CoupAction currentAction) {
        getLog().debug(String.format("%s is doing an exchange...", actingPlayer));

        PlayerInfoState currentPlayerInfo = context.getPlayerInfo(actingPlayer);

        CoupCard card1 = context.getDeck().takeCard();
        CoupCard card2 = context.getDeck().takeCard();

        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(card1);
        handCards.add(card2);
        handCards.addAll(currentPlayerInfo.getDownCards());

        Multiset<CoupCard> returnedCards = getReturnedCards(actingPlayer, currentPlayerInfo, handCards);

        getLog().debug(String.format("%s is putting back 2 cards", actingPlayer));

        // update the info object
        Multiset<CoupCard> newHand = Multisets.difference(handCards, returnedCards);
        currentPlayerInfo.setDownCards(newHand);

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
        if (!isReturnedCollectionOk(handCards, returnedCards)) {
            p.tryAgain("The returned cards need to come from the given hand cards");

            return getReturnedCards(p, info, handCards);
        }

        return returnedCards;
    }

    protected boolean isReturnedCollectionOk(Multiset<CoupCard> handCards, Multiset<CoupCard> returnedCards) {
        Iterator<CoupCard> i = returnedCards.iterator();
        CoupCard card1 = i.next();
        CoupCard card2 = i.next();
        if (card1.equals(card2)) {
            // this is a special case. We need to see if we have 2 of this card in the handCards set
            if (handCards.count(card1) == 2) {
                return true;
            }
            return false;
        }

        Multiset<CoupCard> intersection = Multisets.intersection(handCards, returnedCards);
        if (intersection.equals(returnedCards)) {
            return true;
        }
        return false;
    }
}
