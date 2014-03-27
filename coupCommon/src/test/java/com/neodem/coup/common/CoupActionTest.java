package com.neodem.coup.common;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;

import static com.neodem.coup.common.CoupAction.ActionType.Coup;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public class CoupActionTest {

    @Test
    public void coupActionShouldSerializeCorrectly() {
        CoupPlayer p = new DoNothingCoupPlayer();
        Serializable original = new CoupAction(p, Coup);
        Serializable copy = (Serializable) SerializationUtils.clone(original);
        assertThat(original, equalTo(copy));
    }
}
