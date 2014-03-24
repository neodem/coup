package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.coup.players.CoupPlayer;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class ExchangeActionProcessor {

    private CoupGameMaster gm;

    public ExchangeActionProcessor(CoupGameMaster gm) {
        this.gm = gm;
    }

    /**
     * @param currentPlayer
     */
    public void handleExchange(CoupPlayer currentPlayer) {
        PlayerInfoState currentPlayerInfo = gm.getPlayerInfoMap().get(currentPlayer);

        int numberOfFaceDownCards = 2 - currentPlayerInfo.getUpCount();

        CoupCard card1 = gm.getDeck().takeCard();
        currentPlayerInfo.cardsInHand.add(card1);

        CoupCard card2 = gm.getDeck().takeCard();
        currentPlayerInfo.cardsInHand.add(card2);
        Collection<CoupCard> returnedCards = getReturnedCards(currentPlayer, currentPlayerInfo, numberOfFaceDownCards, card1, card2);

        // if we are cool, we put the cards back into the deck and shuffle it
        for (CoupCard c : returnedCards) {
            currentPlayerInfo.cardsInHand.remove(c);
            gm.getDeck().putCard(c);
        }

        gm.getDeck().shuffleDeck();
    }

    private Collection<CoupCard> getReturnedCards(CoupPlayer p, PlayerInfoState info, int numberOfFaceDownCards, CoupCard card1, CoupCard card2) {
        // deal with exchange
        Collection<CoupCard> returnedCards = p.exchangeCards(info.cardsInHand);

        // collection must return the number of face down cards the player has
        if (returnedCards.size() != numberOfFaceDownCards) {

            p.tryAgain("You need to return " + numberOfFaceDownCards + " cards");

            return getReturnedCards(p, info, numberOfFaceDownCards, card1, card2);
        }
        return returnedCards;
    }
}
