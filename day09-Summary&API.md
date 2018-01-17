# ����
	1.��Ʒ����ҳ��չʾ����̬չʾ jsp + redis
	2.ʹ��freemarkerʵ����ҳ��̬��
	3.ActiveMqͬ�����ɾ�̬��ҳ
	
------

# �ĵ����&ѧ���Ķ���
	1.redis��ʹ�ã�
		����Ʒ������Ϣ��ӻ����ʱ�򣬲�������hash�洢����Ϊhash������������ÿ��key-value�Ĺ���ʱ��,hash�ʺϳ��ڱ��棬Ҫ���ù���ʱ�䣬����stringȥ�洢get,set,expire

	2.freemarker��ʹ�ã�
		ѭ��ʹ�ø�ʽ��
	<#list stuList as stu>
			<tr>
				<td>${stu.id}</td>
				<td>${stu.name}</td>
				<td>${stu.age}</td>
				<td>${stu.address}</td>
			</tr>
		</#list>
	
	if�жϵ�ʹ�ã�
		<#--���б�ɫ-->
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

	���ڵ�ʹ�ã�
		    ��ǰ���ڣ�${date?date}<br>
    ��ǰʱ�䣺${date?time}<br>
    ��ǰ���ں�ʱ�䣺${date?datetime}<br>
    �Զ������ڸ�ʽ��${date?string("yyyy/MM/dd HH:mm:ss")}<br>


	nullֵ�Ĵ���nullֵ�Ĵ���${val!"Ĭ��ֵ"}<br>
    nullֵ�Ĵ���${val!}<br>
    nullֵ���жϣ�
    <#if val??>
        val��Ϊ��
    <#else >
        val�ǿ�ֵ
    </#if>



	include��ǩ��ʹ�ã�
	include��ǩ���ԣ�<#include "hello.ftl">

	3.@RequestParam��@Pathvariable������:
		@RequestParam����url������ʺŴ�������ʱ��ʹ�ã���/item?id=100
		@PathVariable����url����ռλ��ʱ��ʹ�ã���/item/{itemId}
[����](https://www.cnblogs.com/helloworld-hyyx/p/5295514.html)	
# ����������
	1.�ڷ����߼���ͼ��ʱ���ǲ�����Controller����д@ResponseBody��
	
----

�ھ���ѧϰ��API
=====
##  1.��Ʒ����ҳ��(localhost:8086)
### 1.1 ��Ʒ����ҳ��չʾ��RequestMapping��"/item/{itemId}"��Verb��GET
���������
```javascript
{
	"itemId":43
}
```
�����߼���ͼview��model���������Ʒ��Ϣ
```javascript
model:
{
	"item":item, //��ƷItem�࣬����ͼƬ
	"itemDesc":itemDesc //��Ʒ������ItemDesc
```

## 2.��springmvc�����freemarker������
```xml
    <!-- freemarker������ -->
    <bean id="freeMarkerConfigurer" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
        <property name="templateLoaderPath" value="/WEB-INF/ftl/"/>
        <property name="defaultEncoding" value="utf-8"></property>
    </bean>
```