# 任务
	1.前台系统搭建
	2.商城首页展示
	3.Cms系统的实现
		a) 内容分类管理
		b) 内容管理
		
------

# 心得体会&学到的东西
	1.由于搜索引擎对静态网页比较友好，所以在商城首页(用户访问页面)，做网页静态化。
	2.由于首页欢迎页是index.jsp，前端控制器的拦截请求是.html，所以不能通过localhost:8082/ 来访问主页，无法拦截“/”，
	  方式1是设置一个RequestMapping，为“/index”，然后在浏览器访问localhost:8082/index.html即可，方式2是在web.xml中
	  设置欢迎页为<welcome-file>index.html</welcome-file>，在访问localhost:8082时，会加载欢迎，但是由于没有
	  index.html，所以找不到这个网页，被Servlet前端控制器拦截(.html)，接着就会访问RequestMapping为(“/index”)的Controller
	3.mybatis的主键返回：SELECT LAST_INSERT_ID()，有事务隔离，仅在当前执行语句的事务里面执行，要在执行完INSERT语句之后执行

# 遇到的问题
	1.在执行修改操作的时候，kindeditor这个富文本框无法加载单纯文字的内容，但如果内容里面是有图片的，就能加载，很奇怪，未解决。。。

----

第四天学习的API
=====
##  1.展示前台主页(localhost:8082)
### 1.1 RequestMapping："/"，Verb：GET
返回view，index.jso前台主页
### 1.2 RequestMapping："/{page}"，Verb：GET

返回View，指定的页面，如item-add.jsp

-----

## 2.内容分类服务模块(localhost:8083)
### 2.1 内容分类查询，RequestMapping："/content/category/list"，Verb：GET
请求参数：
```javascript
{
	"id":30//当前节点的id，第一次请求是没有参数的，需要给默认值0
}
```
返回json数据：`List<EasyUIITreeNode>`
```javascript
{
	[
		{
			"id":1,
			"text":"节点名称",
			"state":"open(closed)"
		},
		{
			"id":2,
			"text":"节点名称2",
			"state":"open(closed)"
		}
	]
}
```
### 2.2 内容分类添加，RequestMapping："/content/category/create"，Verb：POST
请求参数：
```javascript
{
	"parent":30,
	"name":"新建分类"
}
```
返回json数据：TaotaoResult+新生成的id
```javascript
添加成功：
{
	"status":200,
	"msg":"ok",
	"id":89
}
```
### 2.3 内容分类重命名，RequestMapping："/content/category/update"，Verb：POST
请求参数：
```javascript
{
	"id":100,
	"name":"测试"
}
```
返回json数据：TaotaoResult
```javascript
重命名成功：
{
	"status":200,
	"msg":"ok"
}
```
### 2.4 内容分类删除，RequestMapping："/content/category/delete"，Verb：POST
请求参数：
```javascript
{
	"id":89 //分类id
}
```
返回json数据：TaotaoResult
```javascript
删除成功
{
	"status":200,
	"msg":"ok"
}
```

-----

## 3. 内容服务模块(localhost:8083)
### 3.1 内容列表查询，RequestMapping："/content/category/list"，Verb：GET
请求参数：
```javascript
{
	"categoryId":0, //分类id
	"page":1, //起始页数
	"rows":20 //一页显示的数目
}
```
返回json数据：EasyUIDataGridResult
```javascript
{
	"total":13, //查询结果的总数
	"rows":[
	{
		//TbContent类
		"id":1,
		"title":"aaa",
		"subtitle":"bb",
		...
	}]
}
```
### 3.2 内容新增，RequestMapping："/content/save"，Verb：POST
请求参数：
```javascript
{
	"categoryId":89,
	"title":"内容标题",
	"subTitle":"内容子标题",
	"titleDesc":"内容描述",
	"url":"http://www...", //商品链接
	"pic":"http://www...", //图片1地址
	"pic2":"http://www...", //图片2地址
	"content":"内容"
}
```
返回json数据：TaotaoResult
```javascript
添加成功：
{
	"status":200,
	"msg":"ok"
}
```
### 3.3 内容修改，RequestMapping："/content/edit"，Verb：POST
请求参数：
```javascript
{
	"categoryId":89,
	"title":"内容标题",
	"subTitle":"内容子标题",
	"titleDesc":"内容描述",
	"url":"http://www...", //商品链接
	"pic":"http://www...", //图片1地址
	"pic2":"http://www...", //图片2地址
	"content":"内容"
}
```
返回json数据：TaotaoResult
```javascript
修改成功：
{
	"status":200,
	"msg":"ok"
}
```
### 3.4 内容删除，RequestMapping："/content/delete"，Verb：POST
请求参数：
```javascript
{
	"ids":"39,40" //多个id时，为字符串
}
```
返回Json数据：TaotaoResult
```javascript
删除成功：
{
	"status":200,
	"msg":"ok"
}
```





