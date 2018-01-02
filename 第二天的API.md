第二天学习的API
=====
##  1.展示后台页面
### 1.1 RequestMapping:  "localhost:8080/" ， Verb：GET
返回View，index.jsp后台主页

###1.2 RequestMapping："/{page}"，Verb：GET
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











