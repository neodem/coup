package com.neodem.coup.common.game.cards;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;

import static com.neodem.coup.common.game.cards.CoupCardType.Captain;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/26/14
 */
public class CoupCardTest {

    @Test
    public void coupCardShouldSerializeCorrectly() {
        Serializable original = new CoupCard(1, Captain);
        Serializable copy = (Serializable) SerializationUtils.clone(original);
        assertThat(original, equalTo(copy));
    }
}
