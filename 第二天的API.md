�ڶ���ѧϰ��API
=====
##  1.չʾ��̨ҳ��
### 1.1 RequestMapping:  "localhost:8080/" �� Verb��GET
����View��index.jsp��̨��ҳ

###1.2 RequestMapping��"/{page}"��Verb��GET
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











