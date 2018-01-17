package com.taotao.content.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {

    @Test
    public void testJedisClientPoll() throws Exception{
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");

        //从容器中获得JedisClient对象
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        //使用JedisClient对象操作redis
        jedisClient.set("jedisClient","mytest");
        String s = jedisClient.get("jedisClient");
        System.out.println(s);
    }
}
