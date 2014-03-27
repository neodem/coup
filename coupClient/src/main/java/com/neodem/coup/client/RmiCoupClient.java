package com.neodem.coup.client;

import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.CoupServer;
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
    private CoupServer coupServer;

    public static void main(String[] args) {
        String springContextFile = "client-config.xml";
        log.info(springContextFile);
        ApplicationContext context = new ClassPathXmlApplicationContext(springContextFile);

        RmiCoupClient c = (RmiCoupClient) context.getBean("coupClient");
        c.start();
    }

    private void start() {
        CoupPlayer p1 = new RandomCoupPlayer("player1");
        coupServer.registerPlayer(p1);

        CoupPlayer p2 = new RandomCoupPlayer("player2");
        coupServer.registerPlayer(p2);

        CoupPlayer p3 = new RandomCoupPlayer("player3");
        coupServer.registerPlayer(p3);

        CoupPlayer p4 = new RandomCoupPlayer("player4");
        coupServer.registerPlayer(p4);

        coupServer.triggerGameStart();
    }

    public void setCoupServer(CoupServer coupServer) {
        this.coupServer = coupServer;
    }
}
