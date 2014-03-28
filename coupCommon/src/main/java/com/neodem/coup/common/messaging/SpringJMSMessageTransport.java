package com.neodem.coup.common.messaging;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public class SpringJMSMessageTransport implements MessageTransport {

    private JmsTemplate jms;
    private Map<String, Queue> queues;

    public static void main(String[] args) {
        String springContextFile = "messageTransport-spring.xml";
        new ClassPathXmlApplicationContext(springContextFile);
    }

    @Override
    public void sendTo(final String destination, final String message) {
        jms.execute(new SessionCallback() {
            @Override
            public Object doInJms(Session session) throws JMSException {
                Queue q = queues.get(destination);

                MessageProducer prd = session.createProducer(q);
                TextMessage msg = session.createTextMessage(message);
                prd.send(msg);

                return null;
            }
        });
    }

    @Override
    public void broadcast(String message) {
        for (String destination : queues.keySet()) {
            sendTo(destination, message);
        }
    }

    @Override
    public void registerNewDestination(final String destinationId) {
        jms.execute(new SessionCallback() {
            @Override
            public Object doInJms(Session session) throws JMSException {
                Queue q = session.createQueue(destinationId);
                queues.put(destinationId, q);
                return null;
            }
        });
    }

    public void setJms(JmsTemplate jms) {
        this.jms = jms;
    }
}
