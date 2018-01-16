package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接受Activemq发送的消息
 */
public class MyMessageListener implements MessageListener{

    @Override
    public void onMessage(Message message) {
        //接受到消息
        TextMessage textMessage = (TextMessage) message;
        String text = null;
        try {
            text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
