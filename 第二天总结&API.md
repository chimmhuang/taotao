# ����
	1.ʹ��Mybatis���򹤳�
	2.����SSM
	3.����Dubbo����
	4.�����Ʒ�б�ҳ��չʾ
	
-----------------


# �ĵ����&ѧ���Ķ�����
	1.ѧ����ʹ��Mybatis���򹤳̣����������򹤳̵�xml�ļ��һ��Ҫд�������
	2.ʹ��pagehelper���з�ҳ������������߰��о�����ͦǿ���
	3.@RequestMapping��@RequestBody��ʹ��
	4.ʹ��VM�������������Զ�̷��ʣ���������ѧ���Ե�ѹ����zookeeper�Ӵ˿�����Զ�̵����Ϸ�����


------------------

# ���������⣺
	1.��Mybatis���򹤳��������ʱ����֪Ϊʲô����mapper��xml�ļ���������ǶԲ��ϣ���namespace��
	  ����com.taotao����(com.taotao.com.taotao.mapper)��Ȼ����resultMap���ֻ���pojo����(com.taotao.TbItem)��
	  �����Լ��Ľ���취���ֶ��ģ�����~��
	2.��Service�������zookeeperע���ʱ��Ҳ��δ֪ԭ��һֱע�᲻��(���ڻ����п�����IPд���ˣ�
	  ��������Ȼ��Ϊ���Բ�������֮��Ӧ�ò������������)������controller���ֲ���ν�����ߣ�ȴû�з����ߣ�
	  ���Գ���һֱû����ͨ�������Լ��Ľ���취�����°�װ��zookeeper(����*2~)
	3.��ʹ����PageHelper֮��һֱ�����棬 Hessian/Burlap��xxx is an unknown class in sun.misc.Launcher
	  java.lang.ClassNotFoundException���´ε��ܽ�Ӧ�ðѴ������Ҳ���ϣ����������Ϊweb��û������pagehelper��
	  �����ڷ����л���ʱ���Ҳ���page�ࣨ����������Ϊpage�̳���ArrayList����return����ᷴ���л�ΪArrayList����
	  ����취������web������pagehelper
	4.�������������ģ�����һ�죬Ҫ�뷵��json���ݵ�ʱ��һ���ڼ���@RequestBody�����ڷ������ݵ�ʱ��һֱ��404��
	  ������ϢҲû�У����������һ�죬������һǵ����һ���
	5.����ĵط������mapperӳ���ļ����������⣬Ӧ����dao��pom�ļ��������build�����resource��
	  ��mapper�Ͷ�Ӧ��xml��������һ��
	  
	  
----------
�ڶ���ѧϰ��API
=====
##  1.չʾ��̨ҳ��
### 1.1 RequestMapping:  "localhost:8080/" �� Verb��GET
����View��index.jsp��̨��ҳ

### 1.2 RequestMapping��"/{page}"��Verb��GET
����View��ָ����ҳ�棬��item-add.jsp

----

## 2.��Ʒ�б�ҳ��
### 2.1 ��ѯ��Ʒ�б�RequestMapping��"/item/list"��Verb��GET��������page=1&rows=30
����json����
```javascript
{
	total:3180,//����
	rows:[{//List������
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
### 2.2 ��ѯ������Ʒ��RequestMapping��"/item/{itemId}"��Verb��GET
����json���ݣ�TbItem��
```javascript
	id=1,
	title="test",
	price=1,
	num=1,
	...
	...
	/*   List<TbItem>  */
```











