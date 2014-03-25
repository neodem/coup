package com.neodem.coup;

import com.neodem.coup.cards.CoupCard;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayerInfo {
    public int coins;
    public CoupCard cardOne = CoupCard.Unknown;
    public CoupCard cardTwo = CoupCard.Unknown;

    public void addUpCard(CoupCard card) {
        if (cardOne == CoupCard.Unknown) {
            cardOne = card;
        } else {
            cardTwo = card;
        }
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();
        b.append("coins : ");
        b.append(coins);
        b.append('\n');

        b.append("card1 : ");
        b.append(cardOne.displayCard());
        b.append('\n');

        b.append("card2 : ");
        b.append(cardTwo.displayCard());
        b.append('\n');

        return b.toString();
    }
}
