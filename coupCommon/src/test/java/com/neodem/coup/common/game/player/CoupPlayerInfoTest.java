package com.neodem.coup.common.game.player;

import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public class CoupPlayerInfoTest {

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void coupPlayerInfoShouldSerializeCorrectly() {
        CoupPlayerInfo cpi = new CoupPlayerInfo(true, 0, new CoupCard(1, CoupCardType.Duke), new CoupCard(2, CoupCardType.Duke));

        Serializable original = cpi;
        Serializable copy = (Serializable) SerializationUtils.clone(original);
        assertThat(original, equalTo(copy));
    }
}
