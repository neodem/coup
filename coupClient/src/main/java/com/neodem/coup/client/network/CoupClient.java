package com.neodem.coup.client.network;

import com.neodem.coup.client.game.RandomCoupPlayer;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.messaging.MessageTranslator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class CoupClient {

    private static Log log = LogFactory.getLog(CoupClient.class.getName());
    private JmsTemplate jmsTemplate;
    private MessageTranslator messageTranslator;

    public static void main(String[] args) {
        String springContextFile = "client-config.xml";
        log.info(springContextFile);
        ApplicationContext context = new ClassPathXmlApplicationContext(springContextFile);

        CoupClient c = (CoupClient) context.getBean("coupClient");
        c.start();
    }

    private void start() {
        setupPlayer(new RandomCoupPlayer("player1"));
        setupPlayer(new RandomCoupPlayer("player2"));
        setupPlayer(new RandomCoupPlayer("player3"));
        setupPlayer(new RandomCoupPlayer("player4"));
    }

    private void setupPlayer(CoupPlayer player) {
        ServiceProxy sp = new ServiceProxy(player, messageTranslator);
        sp.init();
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setMessageTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }
}
