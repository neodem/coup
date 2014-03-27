package com.neodem.coup.client;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public class RandomCoupPlayerTest {

    @Test
    public void randomCoupPlayerShouldSerializeCorrectly() {
        Serializable original = new RandomCoupPlayer("test");
        Serializable copy = (Serializable) SerializationUtils.clone(original);
        assertThat(original, equalTo(copy));
    }
}
