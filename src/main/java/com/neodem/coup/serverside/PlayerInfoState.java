package com.neodem.coup.serverside;

import com.neodem.coup.CoupPlayerInfo;
import com.neodem.coup.cards.CoupCard;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class PlayerInfoState {
    public int coins;
    public CoupCard card1;
    public CoupCard card2;
    public boolean active;
    public String name;

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name);
        b.append(" (");
        b.append(coins);
        b.append(") : ");

        b.append(card1);
        b.append(",");
        b.append(card2);

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

        if (card1.faceUp) pi.addUpCard(card1);
        if (card2.faceUp) pi.addUpCard(card2);

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

        cpi.cardOne = card1;
        cpi.cardTwo = card2;

        return cpi;
    }

    /**
     * @return true if this player has at least one card face down, false if otherwise
     */
    public boolean evaluateActive() {
        if (!card1.faceUp) return true;
        if (!card2.faceUp) return true;
        return false;
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

    public boolean handHasTwoOfTheSameCard() {
        return card1.equals(card2);
    }

    /**
     * return the face up card the player has (if any)
     *
     * @return the face up card the player has (if any) or null if none
     */
    public CoupCard getUpCard() {
        if (card1.faceUp) return card1;
        if (card2.faceUp) return card2;
        return null;
    }

    public boolean hasCard(CoupCard card) {
        return card1.equals(card) || card2.equals(card);
    }

    /**
     * return true if the player has this card and it is face down
     *
     * @param testCard the card we are testing for
     * @return true if the player has this card and it is face down, false if not or if its null
     */
    public boolean validInfluence(CoupCard testCard) {
        if (testCard == null) return false;
        if (card1.equals(testCard) && !card1.faceUp) return true;
        if (card2.equals(testCard) && !card2.faceUp) return true;
        return false;
    }

    /**
     * if the player has this card and it's face down, turn it over
     *
     * @param testCard the card we are testing for
     */
    public void turnFaceUp(CoupCard testCard) {
        if (testCard == null) return;
        if (card1.equals(testCard)) card1.faceUp = true;
        else if (card2.equals(testCard)) card2.faceUp = true;
    }

    public Collection<CoupCard> getDownCards() {
        Collection<CoupCard> downCards = new ArrayList<>();
        if (!card1.faceUp) downCards.add(card1);
        if (!card2.faceUp) downCards.add(card2);
        return downCards;
    }

    /**
     * if this card exists in the hand, remove it (set to null)
     *
     * @param card
     */
    public void removeCard(CoupCard card) {
        if (card == null) return;
        if (card1.equals(card)) card1 = null;
        else if (card2.equals(card)) card2 = null;
    }

    /**
     * if there is an empty (null) spot, add this card else do nothing
     *
     * @param card
     */
    public void addCard(CoupCard card) {
        if (card == null) return;
        if (card1 == null) card1 = card;
        else if (card2 == null) card2 = card;
    }
}
