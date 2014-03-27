package com.neodem.coup.common.game;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayerInfo implements Serializable {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupPlayerInfo)) return false;

        CoupPlayerInfo that = (CoupPlayerInfo) o;

        if (active != that.active) return false;
        if (coins != that.coins) return false;
        if (cardOne != null ? !cardOne.equals(that.cardOne) : that.cardOne != null) return false;
        if (cardTwo != null ? !cardTwo.equals(that.cardTwo) : that.cardTwo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (active ? 1 : 0);
        result = 31 * result + coins;
        result = 31 * result + (cardOne != null ? cardOne.hashCode() : 0);
        result = 31 * result + (cardTwo != null ? cardTwo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();

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
}
