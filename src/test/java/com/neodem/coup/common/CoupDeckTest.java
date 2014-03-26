package com.neodem.coup.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class CoupDeckTest {
    private CoupDeck deck;

    @Before
    public void before() {
        deck = new CoupDeck();
    }

    @After
    public void after() {
        deck = null;
    }

    @Test
    public void coupDeckShouldAlwaysReturnACardWhenOneIsInTheDeck() {
        for (int i = 0; i < 15; i++)
            assertThat(deck.takeCard(), not(nullValue()));
    }


}


