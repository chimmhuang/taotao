# 任务
	1.搭建solr
	2.Solrj使用测试
	3.把数据库中的数据导入索引库
	4.实现搜索功能
		
------

# 心得体会&学到的东西
	1.solr安装：需要tomcat，发布solr.war，然后将solr-4.1解压目录下的solr拷贝到/usr/local/solr/solrhome。然后将
	  example/lib/ext/下的jar包拷贝到solr/WEB-INF下的lib里面，最后在web.xml中配置solrhome的地址。
	2.将要搜索的字段，编写如schema.xml文件中，schema文件位于solrhome里面。
	3.配置filedType，将要分词的类型填入,`<field name="item_title" type="text_ik" indexed="true" stored="true"/>`
	  `<field name="item_image" type="string" indexed="false" stored="true" />`
	4.因为有中文，所以要加入中文分词器，将IK分词器.jar，加入solr/WEB-INF下的lib里面，并将IKAnalyzer.cfg.xml、
	  ext_stopword.dic、mydict.dic ，这三个文件，复制到WEB-INF下的classes目录下(没有则创建)
	5.在索引库里面存入id的时候，schema文件传入的类型为String，所以取出来来的时候，要么用String去接受，
	  要么就转换类型为Long
	6.SolrServer的使用：
	  //创建一个SolrServer对象。创建一个HttpSolrServer对象
          //需要指定solr服务的url
          //创建一个文档对象SolrInputDocument
          //向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
          //把文档对象写入索引库
          //提交 
    	 7.由于之前自定义了一个mapper。使用sqlsessionfactory拿到代理对象，所以要在applicationContext-dao.xml中
	   添加mapper映射包扫描器
    	 8.新建一个applicationContext-solr，让spring容器创建一个SolrServer连接服务器
    	 9.要在有mapper文件的pom文件里，添加
	   <build>
		<resources>
			<resource>
				<directory>src/main/java</directory>
				<includes>
					<include>**/*.xml</include>
				</includes>
			</resource>
			<resource>
				<directory>src/main/resources</directory>
				<includes>
					<include>**/*.xml</include>
					<include>**/*.properties</include>
				</includes>
			</resource>
		</resources>
	  </build>
	  否则不会将xml文件发布到目标目录下
	
# 遇到的问题
	1.设置查询条件时，query.setStart()是起始记录数，不是起始页面，query.setRows()是每页显示数。
	2.要在applicationContext-service.xml文件中，将包扫描的扫描范围上升到com.taotao.search，否则无法扫描到dao层，
	  就会造成service无法注入dao
	3.在搜索的controller层需要对请求参数进行转码处理
	
----

第六天学习的API
=====
##  1.后台索引库操作(localhost:8081)
### 1.1 添加商品到索引库，RequestMapping："/index/import"，Verb：POST
返回json数据：`TaotaoResult`
```javascript
{
	"status":200,
	"msg":"ok"
}
```

------

## 2.前台搜索系统(localhost:8085)
### 2.1 展示搜索页面，RequestMapping："/search"，Verb：GET
请求参数：
```javascript
{
	"q":"查询条件",
	"page":1 //页码，默认为1，默认不传值
}
```
返回view页面：`search.html`，需要将数据加入model，请求转发
```javascript
model:
{
	"query":"手机",
	"totalpage":42, //总记录数
	"itemList":[//存储自定义的SearchResult类
		{
			"id":"146124",
			"title":"海尔<font color='red'>手机</font>...",
			"sell_point":"下单即送100豆",
			"price":19900,
			"image":"http://image.taotao.com/...",
			"category_name":"手机",
			"item_desc":null //不需要传递商品内容介绍
		}
	]
}
```
