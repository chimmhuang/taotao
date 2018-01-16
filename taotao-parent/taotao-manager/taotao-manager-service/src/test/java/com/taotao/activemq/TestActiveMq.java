package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class TestActiveMq {

    //queue
    //producer

    /**
     * 测试消息队列发送queue
     * @throws Exception
     */
    @Test
    public void testQueueProducer() throws Exception{
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定MQ服务的IP及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.31.100:61616");
        //2.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接，调用Connection对象的start方法
        connection.start();
        //4.使用Connection对象创建一个Session对象
        //第一个参数是：是否开启事务(MQ的事务，分布式事务)，一般不使用事务。保证数据的最终一致，可以使用消息队列实现
        //如果第一个参数为true，那么第二个参数自动忽略，如果不开启事务false，第二个参数为消息的应答模式。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在应该使用queue
        //参数是消息队列的名称
        Queue queue = session.createQueue("test-queue");
        //6.使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个TextMessage对象
//        TextMessage textMessage = new ActiveMQTextMessage();
//        textMessage.setText("hello activeMq");
        TextMessage textMessage = session.createTextMessage("hello activemq");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }


    /**
     * 测试消息队列接受queue
     * @throws Exception
     */
    @Test
    public void testQueueConsumer()  throws Exception{
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.31.100:61616");
        //使用连接工厂对象创建一个Session对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用Session创建一个Destination，Destination应该和消息的发送端一致
        Queue queue = session.createQueue("test-queue");
        //使用Session创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(queue);
        //向Consumer对象中设置一个MessageListener对象，用来接受消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        //打印消息内容
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        //如果没有接受到消息，系统等待接受消息
//        while (true){
//            Thread.sleep(100);
//        }
        System.in.read();//等待输入
        //关闭资源
        consumer.close();
        session.close();
        connection.close();

    }


    //topiic
    //producer

    /**
     * 测试消息队列发送topic生产者
     * @throws Exception
     */
    @Test
    public void testTopicProducer() throws Exception{
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.31.100:61616");
        //创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建Destination，应该使用topic
        Topic topic = session.createTopic("test-topic");
        //创建一个Producer对象
        MessageProducer producer = session.createProducer(topic);
        //创建一个TextMessage对象
        TextMessage textMessage = session.createTextMessage("hello activemq topic");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }


    /**
     * 测试消息队列接受queue
     * @throws Exception
     */
    @Test
    public void testTopicConsumer() throws Exception{
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.31.100:61616");
        //使用连接工厂对象创建一个Session对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用Session创建一个Destination，Destination应该和消息的发送端一致
        Topic topic = session.createTopic("test-topic");
        //使用Session创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(topic);
        //向Consumer对象中设置一个MessageListener对象，用来接受消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        //打印消息内容
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        //如果没有接受到消息，系统等待接受消息
//        while (true){
//            Thread.sleep(100);
//        }
        System.out.println("topic消费者2.。。");
        System.in.read();//等待输入
        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
