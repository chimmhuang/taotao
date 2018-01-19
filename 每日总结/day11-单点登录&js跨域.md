# 任务
	1.sso注册功能实现
	2.sso登陆功能实现
	3.通过token获得用户信息
	4.Ajax跨域请求
	
------

# 心得体会&学到的东西
	1.在给taotao-common添加servlet-api依赖的时候，有一个属性是<scope>provided</scope>，这个是在编译、测试、的时候
	  会有，但是在打包发布的时候不会有，因为tomcat已经提供了这个功能，若发布这个依赖，则会造成包冲突

	2.n$.post("/user/register",$("#personRegForm").serialize()。将表单序列化，形成key-value的格式

	3.Ajax跨域请求，会在参数后面带上callback，返回js语句
	
	4.在用户登陆成功后，应该生成一个唯一的SessionID，保存在cookie里面，然后用户下次可以直接通过这个sessionId查找
	  用户名和密码，直接登陆
	  
	5.用户登陆：
		先判断用户名密码是否正确，验证通过后，生成UUID，将UUID作为SessionID，存入redis，设置期限为30分钟，
		然后将SessionID存入cookie，登陆成功后，返回给浏览器
		
	6.通过token(SessionID)取用户信息：
		先通过token查询redis缓存，若查不到，则回写用户过期，请重新登陆。若查到，则重新设置有效期，并将查询到
		的user信息返回给浏览器。若参数里有callback，则将字符串组合成js语句形式，返回，若没有，则返回json数据
		
# 遇到的问题
	N/A
	
----

第十一天学习的API
=====
##  1. 检查数据是否可用(localhost:8088)
###RequestMapping："/user/check/{param}/{type}"，Verb：GET
请求参数：
```javascript
{
	"param":"要检验的数据",
	"type":1 //1.username   2.phone  3.email
}
```
返回json数据：`TaotaoResult`
```javascript
{
	"status":200,
	"msg":"ok",
	"data":flase //查询到数据返回false
}
```

-----

## 2.单点登陆操作(localhost:8088)
### 2.1 用户注册，RequestMapping："/user/register"，Verb：POST
请求参数：
```javascript
{
	"username":"zhangsan",
	"password":"123",
	"phone":"110",
	"email":"110@163.com"
}
```
返回json数据：`TaotaoResult`
```javascript
{
注册成功：
	"status":200,
	"msg":"ok"
}
```
### 2.1 用户登陆，RequestMapping："/user/login"，Verb：POST
请求参数：
```javascript
{
	"username":"用户名",
	"password":"密码"
}
```
返回json数据：`TaotaoResult`
```javascript
{
登陆成功：
	"status":200,
	"msg":"ok",
	"data":"SessionID" //登陆成功，返回token
}
```
### 2.2 通过token查询用户信息，RequestMapping："/user/token/{token}"，Verb：GET
请求参数：
```javascript
{
	"token":"asdasdasdasd" //SessionId
}
```
返回json数据：`TaotaoResult`
```javascript
{
查询成功：
	"status":200,
	"msg":"ok",
		"data":"{//TbUser类
				"id":1,
				"username":"用户名",
				"phone":"110",
				"email":"110@163.com"	
			}"
}
```
### 2.3 安全退出，RequestMapping："/user/logout/{token}"，Verb：GET
请求参数：
```javascript
{
	"taken":"asdasdasd"//sessionID
}
```
返回json数据：`TaotaoResult`
```javascript
{
	"status":200,
	"msg":"ok"
}
```
