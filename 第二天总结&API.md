# 任务：
	1.使用Mybatis逆向工程
	2.整合SSM
	3.发布Dubbo服务
	4.完成商品列表页面展示
	
-----------------


# 心得体会&学到的东西：
	1.学会了使用Mybatis逆向工程，在生成逆向工程的xml文件里，一定要写清楚包名
	2.使用pagehelper进行分页搜索，这个工具包感觉还是挺强大的
	3.@RequestMapping和@RequestBody的使用
	4.使用VM开启虚拟机进行远程访问，减轻我自学电脑的压力，zookeeper从此可以在远程电脑上访问了


------------------

# 遇到的问题：
	1.在Mybatis逆向工程生成完成时，不知为什么，在mapper的xml文件里，包名总是对不上，在namespace中
	  会多出com.taotao，如(com.taotao.com.taotao.mapper)，然后在resultMap里又会少pojo，如(com.taotao.TbItem)，
	  最终自己的解决办法是手动改（绝望~）
	2.在Service服务层往zookeeper注册的时候，也是未知原因一直注册不上(现在怀疑有可能是IP写错了，
	  但是我依然认为在自查了许多遍之后，应该不会是这个问题)，导致controller表现层所谓消费者，却没有发布者，
	  所以程序一直没有走通，最终自己的解决办法是重新安装了zookeeper(绝望*2~)
	3.在使用了PageHelper之后，一直报警告， Hessian/Burlap：xxx is an unknown class in sun.misc.Launcher
	  java.lang.ClassNotFoundException（下次的总结应该把错误代码也带上），这个是因为web层没有依赖pagehelper，
	  导致在反序列化的时候找不到page类（报警告是因为page继承了ArrayList，在return过后会反序列化为ArrayList），
	  解决办法就是在web层依赖pagehelper
	4.这个问题是最蠢的，找了一天，要想返回json数据的时候，一定在加上@RequestBody，我在返回数据的时候一直报404，
	  错误信息也没有，真的难受了一天，不过吃一堑，长一智嘛。
	5.疏忽的地方：解决mapper映射文件不发布问题，应该在dao的pom文件里面加上build，添加resource，
	  将mapper和对应的xml都包含在一起
	  
	  
----------
第二天学习的API
=====
##  1.展示后台页面
### 1.1 RequestMapping:  "localhost:8080/" ， Verb：GET
返回View，index.jsp后台主页

### 1.2 RequestMapping："/{page}"，Verb：GET
返回View，指定的页面，如item-add.jsp

----

## 2.商品列表页面
### 2.1 查询商品列表，RequestMapping："/item/list"，Verb：GET，参数：page=1&rows=30
返回json数据
```javascript
{
	total:3180,//总数
	rows:[{//List，行数
	id=1,
	title="test",
	price=1,
	num=1,
	...
	...
	/*   List<TbItem>  */
	}]
}
```
### 2.2 查询单个商品，RequestMapping："/item/{itemId}"，Verb：GET
返回json数据，TbItem类
```javascript
	id=1,
	title="test",
	price=1,
	num=1,
	...
	...
	/*   List<TbItem>  */
```











