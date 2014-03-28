package com.neodem.coup.common.messaging;

import java.io.Serializable;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class Message implements Serializable {
    public String content = null;

    public Message(String content) {
        this.content = content;
    }
}
