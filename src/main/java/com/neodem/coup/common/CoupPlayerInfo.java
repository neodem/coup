package com.neodem.coup.common;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayerInfo {
    public boolean active = true;
    public int coins = 0;
    public CoupCard cardOne = new CoupCard(CoupCardType.Unknown);
    public CoupCard cardTwo = new CoupCard(CoupCardType.Unknown);

    public void addUpCard(CoupCard card) {
        if (cardOne.type == CoupCardType.Unknown) {
            cardOne = card;
        } else {
            cardTwo = card;
        }
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();

        if (!active) {
            b.append("This player is not active");
            b.append('\n');
        } else {
            b.append("coins : ");
            b.append(coins);
            b.append('\n');
        }

        b.append("card1 : ");
        b.append(cardOne.displayCard());
        b.append('\n');

        b.append("card2 : ");
        b.append(cardTwo.displayCard());
        b.append('\n');

        return b.toString();
    }
}
