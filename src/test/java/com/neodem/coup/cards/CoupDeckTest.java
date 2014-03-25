package com.neodem.coup.cards;

import org.junit.After;
import org.junit.Before;

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

}


