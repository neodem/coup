package com.neodem.coup.server;

import com.neodem.coup.common.CoupCard;
import com.neodem.coup.common.CoupCardType;
import com.neodem.coup.common.CoupPlayerInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * The internal player state held by the server
 * <p/>
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
        pi.active = active;
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
        cpi.active = active;
        cpi.coins = coins;

        cpi.cardOne = card1;
        cpi.cardTwo = card2;

        return cpi;
    }

    /**
     * @return true if this player has at least one card face down, false if otherwise
     */
    public boolean evaluateActive() {
        if (!active) return false;

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

    public boolean hasCard(CoupCardType card) {
        return card1.type.equals(card) || card2.type.equals(card);
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
     * reset the hand to these cards
     *
     * @param newDownCards
     */
    public void setDownCards(Collection<CoupCard> newDownCards) {
        if (newDownCards.size() != 2 && newDownCards.size() != 1)
            throw new IllegalArgumentException("new down cards must be 1 or 2 cards");

        Iterator<CoupCard> i = newDownCards.iterator();

        if (newDownCards.size() == 1) {
            if (card1.faceUp) card2 = i.next();
            else if (card2.faceUp) card1 = i.next();
            else
                throw new IllegalArgumentException("There are 2 face down cards and only 1 in the new down cards collection");
        } else {
            if (card1.faceUp || card2.faceUp)
                throw new IllegalArgumentException("There is at least 1 face up cards and 2 in the new down cards collection");

            card1 = i.next();
            card2 = i.next();
        }
    }

    /**
     * if this card exists in the hand, remove it (set to null)
     *
     * @param card
     */
    public CoupCard removeCardOfType(CoupCardType card) {
        CoupCard cardToReturn = null;

        if (card != null) {
            if (card1.type.equals(card)) {
                cardToReturn = card1;
                card1 = null;
            } else if (card2.type.equals(card)) {
                cardToReturn = card2;
                card2 = null;
            }
        }

        return cardToReturn;
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
