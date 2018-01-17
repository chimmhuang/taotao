<html>
<head>
	<title>测试页面</title>
</head>
<body>
	学生信息：<br>
	学号：${student.id}<br>
	姓名：${student.name}<br>
	年龄：${student.age}<br>
	家庭住址：${student.address}<br>
	学生列表：<br/>
	<table border="1">
		<tr>
            <th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>家庭住址</th>
		</tr>
		<#--循环取集合中的数据-->
		<#list stuList as stu>
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
		</#list>
	</table>
    <br>
    当前日期：${date?date}<br>
    当前时间：${date?time}<br>
    当前日期和时间：${date?datetime}<br>
    自定义日期格式：${date?string("yyyy/MM/dd HH:mm:ss")}
    <br><hr>
    null值的处理：${val!"默认值"}<br>
    null值的处理：${val!}<br>
    null值的判断：
    <#if val??>
        val不为空，值为：${val}
    <#else >
        val是空值
    </#if>
    <hr>
    include标签测试：
    <#include "hello.ftl">
</body>
</html>