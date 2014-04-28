package com.neodem.coup.common.game;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/26/14
 */
public class CoupGameContextTest {

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void coupGameContextShouldSerializeCorrectly() {
        CoupCommunicationInterface p = new DoNothingCoupPlayer();
        CoupGameContext gc = new CoupGameContext();
        gc.addPlayer(p.getPlayerName());

        Serializable original = gc;
        Serializable copy = (Serializable) SerializationUtils.clone(original);
        assertThat(original, equalTo(copy));
    }
}
