# 任务
	1.首页大广告展示
	2.Redis服务器搭建
	3.Redis集群搭建
	4.向业务逻辑中添加缓存
	5.同步缓存
		
------

# 心得体会&学到的东西
	1.此次学会了写一个简单的批处理脚本文件，可以一键启动或关闭zookeeper、redis、以后后续的solr等
	2.redis的常用命令：
		查看redis进程 ps aux|grep redis
		1)set str1 12345
		2)get str1
		3)incr str1 //自增长+1，若没有，则新建一个key，默认值0，然后自增1
		4)decr str1 //自减1
		5)hset hash1 field1 abc
		6)hget hash1 field
		7)ttl str1 //查看有效期，若为-1则是永久保存，若为-2则是没有此数据，若为正数则是正在倒计时
		8)expire str1 100 //设置有效期，100秒过期
	3.redis的启动：
		1)前端启动./redis-server
		2)后台启动./redis-server redis.conf
		3)redis-cli
		./redis-cli和./redis-cli-h 默认链接在6379端口的redis服务
		./redis-cli -h 192.168.31.100 -p 6379
		-h：链接的服务器的地址
		-p：服务的端口号
	4.使用Jedis工具包进行对redis的访问
	5.redis集群的搭建
		删除快照文件dump
		修改端口号7001-7006
		将cluster-enabled改为yes

		启动所有redis集群，写批处理文件
		更改start-all.sh的权限，使其变为可执行文件
		chmod +x start-all.sh 
		
		搭建集群：
		安装ruby，安装rubygem，安装gem redis.gem
		将src目录下的redis-tr ib.rb文件复制到/usr/local/redis-cluster目录下
		在cluster目录下，使用ruby脚本搭建集群
		./redis-trib.rb create --replicas 192.168.31.100:7001 192.168.31.100:7002 192.168.31.100:7003 192.168.31.100:7004 192.168.31.100:7005 192.168.31.100:7006
	6.redis集群，有主节点和从节点，哈希槽都在主节点上，从节点上没有槽，但是从节点对应一个主节点，相当于是主节点的备份
	7.centOS下链接redis集群，./redis-cli -p 7006 -c
	8.查看集群信息 cluster info、查看集群节点cluster nodes
	9.在spring容器中，配置redis配置文件
	
# 遇到的问题
	1.在写好resource.properties配置文件的时候，一定要记得在spring配置文件里面引用，否则是无法注入的。
	
----

第五天学习的API
=====
##  1.展示前台主页(localhost:8082)
### 1.1 RequestMapping："/"，Verb：GET
返回view，index.jsp前台主页，并包含model数据：
```javascript
{
	[
	    {
		    "alt": "图片标题",
		    "height": 240, //大屏幕heighy
		    "width": 670, //大屏幕width
	        "heightB": 240, //小屏幕height
	        "widthB": 550, //小屏幕width
		    "src": "http://image.taotao.com/...", //图片1
	        "srcB":"http://image.taotao.com/...", //图片2
	        "href": "http://sale.jd.com/...", //商品链接
		}
	]
}
```