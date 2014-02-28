package com.neodem.coup.coup;

import com.neodem.coup.coup.cards.CoupCard;

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
}
