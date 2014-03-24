package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.cards.CoupCard;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class PlayerInfoState {
    public int coins;
    public Collection<CoupCard> cardsInHand;
    public boolean active;
    public String name;

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(name);
        b.append(" (");
        b.append(coins);
        b.append(") : ");

        for (CoupCard c : cardsInHand) {
            b.append(c);
            b.append(",");
        }

        return b.toString();
    }

    public CoupPlayerInfo makePlayerInfo() {
        CoupPlayerInfo pi = new CoupPlayerInfo();
        pi.coins = coins;

        for (CoupCard card : cardsInHand) {
            if (card.faceUp) {
                pi.addUpCard(card);
            }
        }

        return pi;
    }

    public boolean evaluateActive() {
        int upCount = 0;
        for (CoupCard card : cardsInHand) {
            if (card.faceUp) {
                upCount++;
            }
        }

        if (upCount == 2) active = false;

        return active;
    }

    public void addCoin() {
        coins++;
    }

    public int removeCoins(int i) {
        if (coins >= i) {
            coins = coins - i;
            return i;
        }

        int avail = coins;
        coins = 0;
        return avail;
    }

    public void addCoins(int i) {
        coins = coins + i;
    }
}
