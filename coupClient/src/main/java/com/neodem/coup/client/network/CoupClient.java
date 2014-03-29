package com.neodem.coup.client.network;

import com.neodem.coup.client.game.RandomCoupPlayer;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.messaging.MessageTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupClient {

    private static Logger log = LogManager.getLogger(CoupClient.class.getName());
    private MessageTranslator messageTranslator;

    public static void main(String[] args) {
        String springContextFile = "client-config.xml";
        log.info(springContextFile);
        ApplicationContext context = new ClassPathXmlApplicationContext(springContextFile);

        CoupClient c = (CoupClient) context.getBean("coupClient");
        c.start();
    }

    private void start() {
        setupPlayer(new RandomCoupPlayer("Player1"));
        setupPlayer(new RandomCoupPlayer("Player2"));
        setupPlayer(new RandomCoupPlayer("Player3"));
        setupPlayer(new RandomCoupPlayer("Player4"));
    }

    private void setupPlayer(CoupCommunicationInterface player) {
        ServiceProxy sp = new ServiceProxy(player, messageTranslator, "localhost", 6969);
        sp.init();
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }
}
