# 任务
	1.购物车实现
	2.订单确认页面展示
	
------

# 心得体会&学到的东西
	1.添加购物车：
		先查询cookie，若cookie中存在此商品，则数量增加，若没有商品，则创建对象，设置数量，写入cookie，在返回添加成功页面的时候，将cookie发送至浏览器

	2.更新数量：
		先查询cookie，将对应商品的数量信息更新，在写入cookie
	
	3.删除购物车商品：
		先查询cookie，找到对应的商品，将其移除，在写入cookie，重定向该页面

	4.购物车的同步：
		在用户登陆的时候，查询服务端redis保存的用户的购物车信息，然后将此信息与浏览器的cookie中的购物车信息对比，若有相同的商品，则将此商品数量相加，若没有，则添加这个商品到服务端，最后在将浏览器的cookie清空，redis可以使用hset，用户名-商品ID-商品信息

	5.拦截器方法：
		LoginInterceptor implements HandlerInterceptor{
			preHandle，执行handler(controller方法)之前，先执行此方法，返回值true：放行；返回false：拦截。
			postHandler，handler执行之后，ModelAndView返回之前
			afterComoletion，ModelAndView返回之后，执行，很少用到，一般做异常处理
		}

	6.拦截器的原理：
		要在springmvc中先配置拦截器
		1) 从cookie中取token信息
		2) 若没有token信息，跳转sso登陆页面，设置回调页面为当前页
		3) 若有token信息，调用sso系统服务，判断用户是否登陆
		4) 若未登录，跳转sso登陆页面，设置回调页面为当前页
		5) 若登陆，将用户信息添加进request，然后放行，执行controller
		
# 遇到的问题
	N/A
	
----

第十二天学习的API
=====
##  1. 购物车(localhost:8089)
### 1.1 添加到购物车，RequestMapping："/cart/add/{itemId}"，Verb：GET
请求参数：
```javascript
{
	"itemId":"12312312313",
	"num":1 //购买数量
}
```
返回view逻辑视图，加入model，并更新cookie：`cartSuccess.jsp`
```javascript
model:
{
	"status":200,
	"msg":"ok",
	"data":flase //查询到数据返回false
}
```
### 1.2 展示购物车商品列表，RequestMapping："/cart/cart"，Verb：GET
返回vie逻辑视图，加入model：`cart.jsp`
```javascript
model:
{
	"cartList":List<TbItem>
}
```
### 1.3 修改购物车商品数量，RequestMapping："/cart/update/num/{itemId}/{num}"，Verb：GET
请求参数：
```javascript
{
	"itemId":"商品ID"，
	"num":3 //修改后的商品数量
}
```
返回json数据，并更新cookie：`TaotaoResult`
```javascript
{
	"status":200,
	"msg":"ok"
}
```
### 1.4 删除购物车商品，RequestMapping："/cart/delete/{itemId}"，Verb：GET
请求参数：
```javascript
{
	"itemId":123
}
```
返回jview逻辑视图，并更新cookie：`重定向该页面cart.html`

## 2. 订单页面(localhost:8091)
### 2.1 展示订单确认页面，RequestMapping："/order/order-cart"，Verb：GET
返回view逻辑视图：`请求转发商品列表cartList`

### 2.2 提交订单，RequestMapping："/order/create"，Verb：POST
请求参数：
```javascript
{
	"orderItems":[
		{
			"itemID":"123",
			"num":"123",//商品购买数量
			"price":"123",//商品价格
			"totalFee",//商品总价格
			"title":"商品标题",
			"picPath":"图片路径"
		}
	],
	"orderShipping":{
		"receiverName":"收货人",
		"receiverMobile":"138888888888",
		"receiverState":"四川",
		"receiverCity":"成都",
		"receiverDistrict":"高新区",
		"receiverAddress":"天府软件园"
	}
}
```
返回view逻辑视图：`success.jsp`
```javascript
model:
{
	"orderId":"123",
	"payment":"123",//总付款多少钱
	"date":"2017-01-21" //预计送达时间
}
```

-----