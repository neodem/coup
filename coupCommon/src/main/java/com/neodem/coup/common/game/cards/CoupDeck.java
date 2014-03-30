package com.neodem.coup.common.game.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.neodem.coup.common.game.cards.CoupCardType.Ambassador;
import static com.neodem.coup.common.game.cards.CoupCardType.Assasin;
import static com.neodem.coup.common.game.cards.CoupCardType.Captain;
import static com.neodem.coup.common.game.cards.CoupCardType.Contessa;
import static com.neodem.coup.common.game.cards.CoupCardType.Duke;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupDeck {


    private final Random r = new Random(System.currentTimeMillis());

    private final List<CoupCard> actualDeck = new ArrayList<>();

    public CoupDeck() {
        actualDeck.add(new CoupCard(r.nextInt(), Assasin));
        actualDeck.add(new CoupCard(r.nextInt(), Assasin));
        actualDeck.add(new CoupCard(r.nextInt(), Assasin));
        actualDeck.add(new CoupCard(r.nextInt(), Captain));
        actualDeck.add(new CoupCard(r.nextInt(), Captain));
        actualDeck.add(new CoupCard(r.nextInt(), Captain));
        actualDeck.add(new CoupCard(r.nextInt(), Contessa));
        actualDeck.add(new CoupCard(r.nextInt(), Contessa));
        actualDeck.add(new CoupCard(r.nextInt(), Contessa));
        actualDeck.add(new CoupCard(r.nextInt(), Duke));
        actualDeck.add(new CoupCard(r.nextInt(), Duke));
        actualDeck.add(new CoupCard(r.nextInt(), Duke));
        actualDeck.add(new CoupCard(r.nextInt(), Ambassador));
        actualDeck.add(new CoupCard(r.nextInt(), Ambassador));
        actualDeck.add(new CoupCard(r.nextInt(), Ambassador));

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
