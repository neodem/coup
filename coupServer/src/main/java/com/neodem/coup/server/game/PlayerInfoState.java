package com.neodem.coup.server.game;

import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.game.player.CoupPlayerInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * The internal player state held by the server
 * <p/>
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/24/14
 */
public class PlayerInfoState {
    private final String name;
    private int coinCount;
    private CoupCard card1;
    private CoupCard card2;
    private boolean active = true;

    public PlayerInfoState(int coinCount, CoupCard card1, CoupCard card2, String myName) {
        this.coinCount = coinCount;
        this.card1 = card1;
        this.card2 = card2;
        this.name = myName;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name);
        b.append(" (");
        b.append(coinCount);
        b.append(") : ");

        b.append(card1);
        b.append(",");
        b.append(card2);

        return b.toString();
    }

    /**
     * make a CPI that can be seen by anyone
     *
     * @return a CPI that can be seen by anyone
     */
    public CoupPlayerInfo makePublicPlayerInfo() {
        CoupPlayerInfo pi = new CoupPlayerInfo(active, coinCount);
        if (card1.isFaceUp()) pi.addUpCard(card1);
        if (card2.isFaceUp()) pi.addUpCard(card2);

        return pi;
    }

    /**
     * make a CPI for only the actual player (will contain card info)
     *
     * @return a CPI for only the actual player
     */
    public CoupPlayerInfo makePrivatePlayerInfo() {
        return new CoupPlayerInfo(active, coinCount, card1, card2);
    }

    public int removeCoins(int i) {
        if (coinCount >= i) {
            coinCount = coinCount - i;
            return i;
        }

        int avail = coinCount;
        coinCount = 0;
        return avail;
    }

    public void addCoins(int i) {
        coinCount = coinCount + i;
    }

    public boolean hasCard(CoupCardType card) {
        return card1.getCardType().equals(card) || card2.getCardType().equals(card);
    }

    /**
     * return true if the player has this card and it is face down
     *
     * @param testCard the card we are testing for
     * @return true if the player has this card and it is face down, false if not or if its null
     */
    public boolean validInfluence(CoupCard testCard) {
        if (testCard == null) return false;
        if (card1.equals(testCard) && !card1.isFaceUp()) return true;
        return card2.equals(testCard) && !card2.isFaceUp();
    }

    /**
     * if the player has this card and it's face down, turn it over
     *
     * @param testCard the card we are testing for
     */
    public void turnFaceUp(CoupCard testCard) {
        if (testCard == null) return;

        if (card1.equals(testCard)) card1.setFaceUp();
        else if (card2.equals(testCard)) card2.setFaceUp();
        ;

        if (card1.isFaceUp() && card2.isFaceUp()) active = false;
    }

    public Collection<CoupCard> getDownCards() {
        Collection<CoupCard> downCards = new ArrayList<>();
        if (!card1.isFaceUp()) downCards.add(card1);
        if (!card2.isFaceUp()) downCards.add(card2);
        return downCards;
    }

    /**
     * reset the hand to these cards
     *
     * @param newDownCards the cards we want to be set to 'down'
     */
    public void setDownCards(Collection<CoupCard> newDownCards) {
        if (newDownCards.size() != 2 && newDownCards.size() != 1)
            throw new IllegalArgumentException("new down cards must be 1 or 2 cards");

        Iterator<CoupCard> i = newDownCards.iterator();

        if (newDownCards.size() == 1) {
            if (card1.isFaceUp()) card2 = i.next();
            else if (card2.isFaceUp()) card1 = i.next();
            else
                throw new IllegalArgumentException("There are 2 face down cards and only 1 in the new down cards collection");
        } else {
            if (card1.isFaceUp() || card2.isFaceUp())
                throw new IllegalArgumentException("There is at least 1 face up cards and 2 in the new down cards collection");

            card1 = i.next();
            card2 = i.next();
        }
    }

    /**
     * if this card exists in the hand, remove it (set to null)
     *
     * @param card the card to test for
     */
    public CoupCard removeCardOfType(CoupCardType card) {
        CoupCard cardToReturn = null;

        if (card != null) {
            if (card1.getCardType().equals(card)) {
                cardToReturn = card1;
                card1 = null;
            } else if (card2.getCardType().equals(card)) {
                cardToReturn = card2;
                card2 = null;
            }
        }

        return cardToReturn;
    }

    /**
     * if there is an empty (null) spot, add this card else do nothing
     *
     * @param card the card to add to the hand
     */
    public void addCard(CoupCard card) {
        if (card == null) return;
        if (card1 == null) card1 = card;
        else if (card2 == null) card2 = card;
    }

    public boolean hasOneInfluenceLeft() {
        return card1.isFaceUp() || card2.isFaceUp();
    }

    public boolean isActive() {
        return active;
    }

    public int getCoinCount() {
        return coinCount;
    }
}
