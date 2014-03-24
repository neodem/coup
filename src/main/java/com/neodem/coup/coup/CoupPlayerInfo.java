package com.neodem.coup.coup;

import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.bandaid.game.Player;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayerInfo {
    public int coins;
    public CoupCard cardOne = CoupCard.Unknown;
    public CoupCard cardTwo = CoupCard.Unknown;

    public void addUpCard(CoupCard card)  {
        if(cardOne == CoupCard.Unknown) {
            cardOne = card;
        }

        cardTwo = card;
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
