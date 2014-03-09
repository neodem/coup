package com.neodem.coup.coup.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupDeck {

    private List<CoupCard> actualDeck = new ArrayList<CoupCard>();

    public CoupDeck() {
        actualDeck.add(CoupCard.Assasin);
        actualDeck.add(CoupCard.Assasin);
        actualDeck.add(CoupCard.Assasin);
        actualDeck.add(CoupCard.Captain);
        actualDeck.add(CoupCard.Captain);
        actualDeck.add(CoupCard.Captain);
        actualDeck.add(CoupCard.Contessa);
        actualDeck.add(CoupCard.Contessa);
        actualDeck.add(CoupCard.Contessa);
        actualDeck.add(CoupCard.Duke);
        actualDeck.add(CoupCard.Duke);
        actualDeck.add(CoupCard.Duke);
        actualDeck.add(CoupCard.Ambassador);
        actualDeck.add(CoupCard.Ambassador);
        actualDeck.add(CoupCard.Ambassador);

        shuffleDeck();
    }

    public void shuffleDeck() {
        long seed = System.nanoTime();
        Collections.shuffle(actualDeck, new Random(seed));
    }

    public CoupCard takeCard() {
        return actualDeck.remove(0);
    }

    public void putCard(CoupCard card) {
         actualDeck.add(card);
    }
}
