package com.neodem.coup.common.game.player;

import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayerInfo implements Serializable {
    private boolean active = true;
    private int coins = 0;
    private CoupCard cardOne = CoupCard.newUnknownCard();
    private CoupCard cardTwo = CoupCard.newUnknownCard();

    public CoupPlayerInfo(boolean active, int coins) {
        this.active = active;
        this.coins = coins;
    }

    public CoupPlayerInfo(boolean active, int coins, CoupCard cardOne, CoupCard cardTwo) {
        this.active = active;
        this.coins = coins;
        this.cardOne = cardOne;
        this.cardTwo = cardTwo;
    }

    public void addUpCard(CoupCard card) {
        if (cardOne.getCardType() == CoupCardType.Unknown) {
            cardOne = card;
        } else {
            cardTwo = card;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupPlayerInfo)) return false;

        CoupPlayerInfo info = (CoupPlayerInfo) o;

        if (active != info.active) return false;
        if (coins != info.coins) return false;
        if (!cardOne.equals(info.cardOne)) return false;
        if (!cardTwo.equals(info.cardTwo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (active ? 1 : 0);
        result = 31 * result + coins;
        result = 31 * result + cardOne.hashCode();
        result = 31 * result + cardTwo.hashCode();
        return result;
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();

        if (!active) {
            b.append(">>> This player is not active <<<");
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

    public boolean isActive() {
        return active;
    }

    public int getCoinCount() {
        return coins;
    }

    public CoupCard getCardOne() {
        return cardOne;
    }

    public CoupCard getCardTwo() {
        return cardTwo;
    }
}
