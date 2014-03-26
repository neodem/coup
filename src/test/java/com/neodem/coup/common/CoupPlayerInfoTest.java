package com.neodem.coup.common;

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

    @Test
    public void coupPlayerInfoShouldSerializeCorrectly() {
        CoupPlayerInfo cpi = new CoupPlayerInfo();
        cpi.active = true;
        cpi.addUpCard(new CoupCard(CoupCardType.Duke));

        Serializable original = cpi;
        Serializable copy = (Serializable) SerializationUtils.clone(original);
        assertThat(original, equalTo(copy));
    }
}
