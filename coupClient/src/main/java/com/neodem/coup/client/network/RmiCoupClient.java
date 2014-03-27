package com.neodem.coup.client.network;

import com.neodem.coup.client.game.RandomCoupPlayer;
import com.neodem.coup.common.game.CoupPlayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class RmiCoupClient {

    private static Log log = LogFactory.getLog(RmiCoupClient.class.getName());
    private NetworkGateway gateway;

    public static void main(String[] args) {
        String springContextFile = "client-config.xml";
        log.info(springContextFile);
        ApplicationContext context = new ClassPathXmlApplicationContext(springContextFile);

        RmiCoupClient c = (RmiCoupClient) context.getBean("coupClient");
        c.start();
    }

    private void start() {
        CoupPlayer p1 = new RandomCoupPlayer("player1");
        gateway.registerPlayer(p1);

        CoupPlayer p2 = new RandomCoupPlayer("player2");
        gateway.registerPlayer(p2);

        CoupPlayer p3 = new RandomCoupPlayer("player3");
        gateway.registerPlayer(p3);

        CoupPlayer p4 = new RandomCoupPlayer("player4");
        gateway.registerPlayer(p4);
    }

    public void setGateway(NetworkGateway gateway) {
        this.gateway = gateway;
    }
}
