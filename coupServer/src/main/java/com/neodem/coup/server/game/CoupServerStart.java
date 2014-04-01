package com.neodem.coup.server.game;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public class CoupServerStart {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("server-config.xml");
    }
}
