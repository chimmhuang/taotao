package com.taotao.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class TestJedis {

    /**
     * 使用jedis连接redis
     * @throws Exception
     */
    @Test
    public void testJedis() throws Exception{
        //创建一个jedis对象，需要指定服务的ip和端口号
        Jedis jedis = new Jedis("192.168.31.100",6379);
        //直接操作数据库
        jedis.set("jedis-key","1234");
        String s = jedis.get("jedis-key");

        System.out.println(s);
        //关闭jedis
        jedis.close();

    }


    /**
     * 使用连接池  jedisPoll连接redis
     */
    @Test
    public void testJedisPoll() throws Exception{
        //创建一个数据库连接池对象（单例），需要指定服务的ip和端口号
        JedisPool jedisPool = new JedisPool("192.168.31.100",6379);
        //从连接池中获得链接
        Jedis jedis = jedisPool.getResource();
        //使用jedis操作数据库（方法级别使用）
        String s = jedis.get("jedis-key");
        System.out.println(s);
        //一定要关闭链接
        jedis.close();
        //系统关闭前，关闭连接池
        jedisPool.close();
    }

    /**
     * 使用jedisCluster连接redis集群
     * @throws Exception
     */
    @Test
    public void testJedisCluster() throws Exception{
        //创建一个JedisCluster对象，构造参数是一个Set类型，集合中每个元素是HostAndPort类型
        Set<HostAndPort> nodes = new HashSet<>();
        //向集合中添加节点
        nodes.add(new HostAndPort("192.168.31.100",7001));
        nodes.add(new HostAndPort("192.168.31.100",7002));
        nodes.add(new HostAndPort("192.168.31.100",7003));
        nodes.add(new HostAndPort("192.168.31.100",7004));
        nodes.add(new HostAndPort("192.168.31.100",7005));
        nodes.add(new HostAndPort("192.168.31.100",7006));
        JedisCluster jedisCluster=  new JedisCluster(nodes);
        //直接使用JedisCluster操作redis，自带连接池，jedisCluster对象可以是单例的
        jedisCluster.set("cluster-test","hello jedis cluster");
        String s = jedisCluster.get("cluster-test");
        System.out.println(s);
        //系统关闭前，关闭jedisCluster
        jedisCluster.close();
    }
}
