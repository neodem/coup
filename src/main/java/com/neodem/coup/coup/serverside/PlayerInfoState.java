package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.cards.CoupCard;

import java.util.Collection;
import java.util.Iterator;

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
        StringBuilder b = new StringBuilder();
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

    /**
     * make a CPI that can be seen by anyone
     *
     * @return
     */
    public CoupPlayerInfo makePublicPlayerInfo() {
        CoupPlayerInfo pi = new CoupPlayerInfo();
        pi.coins = coins;

        for (CoupCard card : cardsInHand) {
            if (card.faceUp) {
                pi.addUpCard(card);
            }
        }

        return pi;
    }

    /**
     * make a CPI for only the actual player
     *
     * @return
     */
    public CoupPlayerInfo makePrivatePlayerInfo() {
        CoupPlayerInfo cpi = new CoupPlayerInfo();
        cpi.coins = coins;

        Iterator<CoupCard> cardIterator = cardsInHand.iterator();

        cpi.cardOne = cardIterator.next();
        cpi.cardTwo = cardIterator.next();

        return cpi;
    }

    public boolean evaluateActive() {
        int upCount = getUpCount();

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

    public int getUpCount() {
        int upCount = 0;
        for (CoupCard card : cardsInHand) {
            if (card.faceUp) {
                upCount++;
            }
        }

        return upCount;
    }

    public boolean hasCard(CoupCard card) {
        return cardsInHand.contains(card);
    }

    /**
     * return true if the player has this card and it is face down
     *
     * @param testCard the card we are testing for
     * @return true if the player has this card and it is face down, false if not or if its null
     */
    public boolean validInfluence(CoupCard testCard) {
        if (testCard == null) return false;
        for (CoupCard card : cardsInHand) {
            if (card.equals(testCard)) {
                if (!card.faceUp) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * if the player has this card and it's face down, turn it over
     *
     * @param testCard the card we are testing for
     */
    public void turnFaceUp(CoupCard testCard) {
        for (CoupCard card : cardsInHand) {
            if (card.equals(testCard)) {
                if (!card.faceUp) {
                    card.faceUp = true;
                    break;
                }
            }
        }
    }


}
