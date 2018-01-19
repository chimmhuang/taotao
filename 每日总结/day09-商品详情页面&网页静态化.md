# 任务
	1.商品详情页面展示，动态展示 jsp + redis
	2.使用freemarker实现网页静态化
	3.ActiveMq同步生成静态网页
	
------

# 心得体会&学到的东西
	1.redis的使用：
		在商品详情信息添加缓存的时候，不能设置hash存储，因为hash不能设置里面每个key-value的过期时间,hash适合长期
		保存，要设置过期时间，就用string去存储get,set,expire

	2.freemarker的使用：
		1).循环使用格式：
			<#list stuList as stu>
				<tr>
					<td>${stu.id}</td>
					<td>${stu.name}</td>
					<td>${stu.age}</td>
					<td>${stu.address}</td>
				</tr>
			</#list>
	
		2).if判断的使用：
			<#--隔行变色-->
			<#if stu_index % 2 == 0>
			    <tr bgcolor="red">
			<#else >
			    <tr bgcolor="green">
			</#if>
				<td>${stu_index}</td>
				<td>${stu.id}</td>
				<td>${stu.name}</td>
				<td>${stu.age}</td>
				<td>${stu.address}</td>
			    </tr>

		3).日期的使用：
		    当前日期：${date?date}<br>
		    当前时间：${date?time}<br>
		    当前日期和时间：${date?datetime}<br>
		    自定义日期格式：${date?string("yyyy/MM/dd HH:mm:ss")}<br>


		4).null值的处理：null值的处理：${val!"默认值"}<br>
		    null值的处理：${val!}<br>
		    null值的判断：
		    <#if val??>
			val不为空
		    <#else >
			val是空值
		    </#if>

		5).include标签的使用：
			include标签测试：<#include "hello.ftl">

	3.@RequestParam和@Pathvariable的区别:
		@RequestParam是在url后面跟问号传参数的时候使用，如/item?id=100
		@PathVariable是在url中有占位的时候使用，如/item/{itemId}
[参照](https://www.cnblogs.com/helloworld-hyyx/p/5295514.html)	
# 遇到的问题
	1.在返回逻辑视图的时候，是不能在Controller里面写@ResponseBody的
	
----

第九天学习的API
=====
##  1.商品详情页面(localhost:8086)
### 1.1 商品详情页面展示，RequestMapping："/item/{itemId}"，Verb：GET
请求参数：
```javascript
{
	"itemId":43
}
```
返回逻辑视图view，model里面包含商品信息
```javascript
model:
{
	"item":item, //商品Item类，包含图片
	"itemDesc":itemDesc //商品描述类ItemDesc
```

## 2.在springmvc中添加freemarker的配置
```xml
    <!-- freemarker的配置 -->
    <bean id="freeMarkerConfigurer" 
	  class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
        <property name="templateLoaderPath" value="/WEB-INF/ftl/"/>
        <property name="defaultEncoding" value="utf-8"></property>
    </bean>
```
