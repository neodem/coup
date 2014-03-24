package com.neodem.coup.coup;

import com.neodem.bandaid.game.GameMaster;
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

        GameMaster gm = (GameMaster) context.getBean("coupGameMaster");

        gm.startGame();
    }

}
