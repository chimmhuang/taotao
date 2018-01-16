# 任务
	1.ActiveMQ的使用方法
	2.添加商品同步索引库
		
------

# 心得体会&学到的东西
	1.ActiveMQ安装：
	  解压就可以直接使用了，不过会有一个问题，就是在后台访问的时候，点击queue的时候会出西安503错误，
	  解决Activemq访问后台(192.168.31.100:8161)出现503错误:
		1).查看机器名
		   [root@server bin]# cat /etc/sysconfig/network
		   NETWORKING=yes
		   HOSTNAME=server

		2).修改host文件
		   [root@server bin]# cat /etc/hosts
		   127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4 server
		   ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
		   [root@server bin]# 

		3).重启Activemq服务
		
	2.8161是web访问的端口。61616是消息服务的端口
	
	3.queue和topic的区别：
		1).queue仅仅允许一个消息传送个一个客户（一对一），接收者获得消息后，消息就会在消息队列中消失，其他人
		   就不能在得到它，但是如果没有消费者在监听queue的消息，该消息会一直保留在消息队列，直到被消费
		2).topic可以有多个客户端（一对多），只有监听topic的才会收到消息，如果没有消费者监听topic，那么该topic
		   的消息就在消息队列中丢失
	4.@Autowired是spring的注解，@Resource是java提供的注解，一般通过name来注解就可以使用@Resource
	
# 遇到的问题
	1.在测试active与spring整合的时候，一直报错，错误部分代码:Invalid broker URI: 192.168.31.100:61616，错误原因是，
	  activemq的connection连接，是tcp协议的连接，所以应该在brokerURL上面，加入tcp://，，原来错误配置：
	  <property name="brokerURL" value="${LOCAL_INTEGRATED_SERVER}:61616"/>
	  改进后的配置：<property name="brokerURL" value="tcp://${LOCAL_INTEGRATED_SERVER}:61616"/>
	
	2.问题：spring容器只允许有一个<context:property-placeholder/>，由于每个模块的xml都各自引用了所需要的配置文件，
	  导致spring在读取了第一个xml所需要的配置文件后，后面的xml文件不会取读取了，原因就是，Spring容器仅允许最多定义一个
	  PropertyPlaceholderConfigurer(或<context:property-placeholder/>)，其余的会被Spring忽略掉。。。解决办法：
	  我个人的解决办法是在applicationContext-service.xml中，引用全部所需的配置文件，然后将其余xml文件中的引用给注释掉
[参照](http://blog.csdn.net/a12458/article/details/52506649)

	3.在配置好消息监听器的时候，要记得在applicationContext-activemq.xml中配置bean，否则无法注入，无法创建消费者

	4.[大坑]测试添加商品到索引库的时候，无限报错The destination temp-topic://item-add-topic does not exist。。
	  找了许久的原因，最后发现，在配置Destination目的地的时候，bean的class写成了
	  org.apache.activemq.command.ActiveMQTempTopic，应该ActiveMQTopic这个class才对
	
----

第七天新的功能内容
=====
##  1.添加商品同步索引库
### 1.1 在ItemServiceImpl里面的添加商品方法，return 成功之前，向activemq发送商品添加消息
```java
        //向activemq发送商品添加消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //要发送商品ID
                TextMessage message = session.createTextMessage(itemId+"");
                return message;
            }
        });
```
### 1.2 在search工程里面，添加消息接收监听器，接收到消息之后，将新添加的商品信息添加到索引库
```java
/**
 * 监听商品添加事件，同步索引库
 */
public class ItemAddMessageListener implements MessageListener{

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        //从消息中取商品ID
        TextMessage textMessage = (TextMessage) message;
        String text = null;
        try {
            text = textMessage.getText();
            long itemId = Long.parseLong(text);
            //根据商品ID，查询数据库，取商品信息
            Thread.sleep(1000);//等待商品添加事务提交
            SearchItem searchItem = searchItemMapper.getItemById(itemId);
            //创建文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档对象中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            document.addField("item_desc",searchItem.getItem_desc());
            //把文档对象写入索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
### 1.3 生产者的applicationContext-activemq.xml
```xml
<!-- 真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供 -->
    <bean id="targetConnectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
        <property name="brokerURL" value="tcp://${DELL_SERVER_ZOOKEEPER}:61616"/>
    </bean>

    <!--
        Spring用于管理真正的ConnectionFactory的ConnectionFactory
        Spring对ConnectionFactory的封装
    -->
    <bean id="connectionFactory" class="org.springframework.jms.connection.SingleConnectionFactory">
        <property name="targetConnectionFactory" ref="targetConnectionFactory"/>
    </bean>

    <!--配置JMSTemplate-->
    <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
        <property name="connectionFactory" ref="connectionFactory"/>
    </bean>

    <!--配置消息目的地Destination对象-->
    <bean id="test-queue" class="org.apache.activemq.command.ActiveMQQueue">
        <constructor-arg name="name" value="test-queue"/>
    </bean>
    <bean id="itemAddTopic" class="org.apache.activemq.command.ActiveMQTopic">
        <constructor-arg name="name" value="item-add-topic"/>
    </bean>
```
### 1.4 消费者的applicationContext-activemq.xml
```xml
<!--
        Spring用于管理真正的ConnectionFactory的ConnectionFactory
        Spring对ConnectionFactory的封装
    -->
    <bean id="connectionFactory" class="org.springframework.jms.connection.SingleConnectionFactory">
        <property name="targetConnectionFactory" ref="targetConnectionFactory"/>
    </bean>


    <!--
        配置消息目的地Destination对象
    -->
    <!--这个是队列目的地，点对点的-->
    <bean id="test-queue" class="org.apache.activemq.command.ActiveMQQueue">
        <constructor-arg name="name" value="test-queue"/>
    </bean>
    <!--这个是主题目的地，一对多的-->
    <bean id="itemAddTopic" class="org.apache.activemq.command.ActiveMQTopic">
        <constructor-arg name="name" value="item-add-topic"/>
    </bean>

    <!--
        配置消息的接收者
    -->
    <!--配置监听器-->
    <bean id="myMessageListen" class="com.taotao.search.listener.MyMessageListener"/>
    <!--消息监听容器-->
    <bean id="defaultMessageListenerContainer" 
	  class="org.springframework.jms.listener.DefaultMessageListenerContainer">
        <property name="connectionFactory" ref="connectionFactory"/>
        <property name="destination" ref="test-queue"/>
        <property name="messageListener" ref="myMessageListen"/>
    </bean>
    <bean id="itemAddMessageListener" class="com.taotao.search.listener.ItemAddMessageListener"/>
    <bean class="org.springframework.jms.listener.DefaultMessageListenerContainer">
        <property name="connectionFactory" ref="connectionFactory" />
        <property name="destination" ref="itemAddTopic" />
        <property name="messageListener" ref="itemAddMessageListener" />
    </bean>
```
