package com.neodem.coup;

import com.neodem.coup.client.RandomCoupPlayer;
import com.neodem.coup.common.CoupPlayer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: vfumo
 * Date: 3/3/14
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] args) {
        String springContextFile = "spring-config.xml";
        System.out.println(springContextFile);
        ApplicationContext context = new ClassPathXmlApplicationContext(springContextFile);

        CoupServer s = (CoupServer) context.getBean("coupServer");

        CoupPlayer p1 = new RandomCoupPlayer("player1");
        s.registerPlayer(p1);

        CoupPlayer p2 = new RandomCoupPlayer("player2");
        s.registerPlayer(p2);

        CoupPlayer p3 = new RandomCoupPlayer("player3");
        s.registerPlayer(p3);

        CoupPlayer p4 = new RandomCoupPlayer("player4");
        s.registerPlayer(p4);

        s.triggerGameStart();
    }

}
