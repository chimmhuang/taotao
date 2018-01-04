# 任务
	1.完成商品分类选择
	2.实现图片上传
	3.商品添加功能完成
	
------

# 心得体会&学到的东西
	1.同步加载、异步加载、延迟加载
		同步加载：
			我们平时最常使用的就是这种同步加载形式：
			<script src="http://XXX.com/script.js"></script>
			同步模式，又称阻塞模式，会阻止浏览器的后续处理，停止了后续的解析，因此停止了
			后续的文件加载（如图像）、渲染、代码执行。一般的script标签（不带async等属性）
			加载时会阻塞浏览器，也就是说，浏览器在下载或执行该js代码块时，后面的标签不会被解析
		异步加载：
			异步加载又叫非阻塞，浏览器在下载执行 js 同时，还会继续进行后续页面的处理。这
			种方法是在页面中<script>标签内，用 js 创建一个 script 元素并插入到
			document 中。这样就做到了非阻塞的下载 js 代码。
		延迟加载：
			有些 js 代码并不是页面初始化的时候就立刻需要的，而稍后的某些情况才需要的。
			延迟加载就是一开始并不加载这些暂时不用的js，而是在需要的时候或稍后再通过js 的控制来异步加载
[参照](https://www.cnblogs.com/mylanguage/p/5635971.html)

	2.图片服务器
		使用FastDFS工具类实现对图片的上传，在web工程中新建一个配置文件，里面写入tracker_server的地址，
		并在controller里面引用(classpath:resource/client.config)，详细使用情况见taotao-manager-web下的
		PictureController.java

# 遇到的问题
	1.在写了一个图片服务器地址的配置文件之后，忘记了在springmvc中引用此文件，导致在图片url组合的时候，
	组合出了错误的url，图片上传成功，但是前台不能显示出图片
	2.商品修改功能，前台传入的url有问题，导致这个功能无法实现
	3.大坑，在使用mybatis查询itemParamItem的时候，使用example无法查询出param_data的字段，上网查了很久，
	  发现了问题出自selectbyexample与selectbyexampleBlobs。在mysql里面param_data是text类型的，
	  mybatis自动生成映射文件的时候针对text类型的数据会默认生成这两个方法，使用前者查询不取这个参数，
	  在有需要的时候我们用后者查询来提高查询效率节省资源。
[参照](http://blog.csdn.net/sshuidajiao/article/details/53812746)


第三天学习的API
=====
##  1.后台新增商品(localhost:8081)
### 1.1 商品分类选择，RequestMapping："/item/cat/list"，Verb：POST
请求参数：
```javascript
{
	"id":1 //父节点id，若没有，则为空
}
```
返回json数据
```javascript
{
	[{
		"id":1,
		"text":"手机",
		"status":"closed"	
	},{
		"id":2,
		"text":"电脑",
		"status":"closed"
	}]
}
```
### 1.2 上传图片，RequestMapping："/pic/upload"，Verb：POST
返回json数据
```javascript
上传成功：
{
	"error":0, //0成功，1错误
	"url":"192.168.25.175/group1/..." //图片的url地址
}
上传失败：
{
	"error":1,
	"message":"图片上传失败"
}
```
### 1.3 商品添加功能实现，RequestMapping："/item/save"，Verb：POST
请求参数：
```javascript
{
	"cid":292,//分类id
	"title":"商品标题",
	"sellPoint":"商品卖点",
	"priceView":"1999.00",
	"price":199900,
	"num":95,//库存数量
	"barcode":12415724,//条形码
	"image":"http...",//图片地址
	"desc":"商品描述"
}
```
返回json数据：

```javascript
上传成功：
{
	"status":200,
	"msg":"ok"
}
```

-----
## 2.查询商品后对商品的操作(localhost:8081)
###2.1 商品上架，RequestMapping："/rest/item/reshelf"，Verb：POST
请求参数：
```javascript
{
	"ids":"12351252,6543646,12324653,121245"
}
```
返回json数据：
```javascript
上架成功
{
	"status":200,
	"msg":"ok"
}
```
### 2.2 商品下架，RequestMapping："/rest/item/instock"，Verb：POST
请求参数：
```javascript
{
	"ids":"12351252,6543646,12324653,121245"
}
```
返回json数据：
```javascript
下架成功
{
	"status":200,
	"msg":"ok"
}
```
### 2.3 商品删除，RequestMapping："/rest/item/delete"，Verb：POST
请求参数：
```javascript
{
	"ids":"12351252,6543646,12324653,121245"
}
```
返回json数据：
```javascript
删除成功
{
	"status":200,
	"msg":"ok"
}
```
-----
## 3.商品修改时需要加载的内容(localhost:8081)
### 3.1 加载商品描述，RequestMapping："/rest/item/desc/{id}"，Verb：GET
返回json数据：
```javascript
加载成功：
{
	"status":200,
	"msg":"ok"
	"data":
	{
		"itemId":123,
		"created":"2017/12/31",//会被序列化为1433500495290
		"updated":"2018/1/4",
		"itemDesc":"商品描述"
	}
}
```
### 3.2加载商品规格，RequestMapping："/rest/item/param/item/{id}"，Verb：GET
返回json数据：
```javascript
加载成功：
{
	"status":2000,
	"msg":"ok",
	"data":
	{
		"id":3,
		"itemId":1433500495290,
		"created":"2017/12/31",//会被序列化为1433500495290
		"updated":"2018/1/4",
		"paramData":"[{\"group\":\"主体\"..."
	}
}
```










