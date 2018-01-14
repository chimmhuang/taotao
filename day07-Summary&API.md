# 任务
	1.搭建solr集群
	2.使用Solrj管理集群
	3.添加全局异常处理器
		
------

# 心得体会&学到的东西
	1.solr集群的搭建：
	  先搭建zookeeper集群，在搭建solr集群
	  1).编写zookeeper编号，在data目录下的myid
	  2).将dataDir更改 
	  3).在zoo.cfc下添加server.1=192.168.31.100:2881:3881  ，一个是服务器连接端口，一个是集群选举端口
	2.solr集群的搭建：
	  1).复制4份tomcat，更改conf/server.xml的3个 端口号/port
	  2).复制4份solrhome，更改solr.xml的host和hostport，每一个solrhome对应一个tomcat
	  3).建立tomcat与solr的关联关系
	  3.1)更改tomcat下面的solr工程下的web.xml，配置solrhome
	  3.2)修改tomcat下面的bin下面的catalina.sh的JAVA_OPTS为-DzkHost=192.168.31.100:2181,192.168.31.100:2182,192.168.31.100:2183
	  4).将solrhome/collection1/conf这个文件夹，上传到zookeeper，要用到root/solr/example/scripts/cloud-scripts的zkcli.sh，命令为./zkcli.sh -zkhost 192.168.25.154:2181,192.168.25.154:2182,192.168.25.154:2183 -cmd upconfig -confdir /usr/local/solr-cloud/solrhome01/collection1/conf/ -confname myconf
	3.配置全局异常处理器，写完java文件后，一定要在springmvc里面配置，否则将无法引用
	
# 遇到的问题
	N/A
	
----

第七天学习的API
=====
##  1.配置全局异常处理器
```javascript
返回ModelAndView
{
	"message":"出错啦，攻城狮正在努力抢修中~"
}
```