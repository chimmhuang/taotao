package com.taotao.test.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {

    /**
     * 测试添加索引库
     * @throws Exception
     */
    @Test
    public void testAddDocument() throws Exception{
        //创建一个SolrServer对象。创建一个HttpSolrServer对象
        //需要指定solr服务的url
//        SolrServer solrServer = new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
        SolrServer solrServer = new HttpSolrServer("http://192.168.31.100:8080/solr/collection1");
        //创建一个文档对象SolrInputDocument
        SolrInputDocument solrDocument = new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
        solrDocument.addField("id","test001");
        solrDocument.addField("item_title","测试商品1");
        solrDocument.addField("item_price",1000);
        //把文档对象写入索引库
        solrServer.add(solrDocument);
        //提交
        solrServer.commit();

    }

    /**
     * 测试删除索引库
     * @throws Exception
     */
    @Test
    public void deleteDocumentById() throws Exception{
//        SolrServer solrServer = new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
        SolrServer solrServer = new HttpSolrServer("http://192.168.31.100:8080/solr/collection1");
        solrServer.deleteById("test001");
        solrServer.commit();

    }


    /**
     * 测试删除索引库
     * @throws Exception
     */
    @Test
    public void deleteDocumentByQuery() throws Exception{
//        SolrServer solrServer = new HttpSolrServer("http://192.168.25.175:8080/solr/collection1");
        SolrServer solrServer = new HttpSolrServer("http://192.168.31.100:8080/solr/collection1");
        solrServer.deleteByQuery("item_price:1000");
        solrServer.commit();
    }


    /**
     * 测试查询索引库
     * @throws Exception
     */
    @Test
    public void searchDocument() throws Exception{
        //创建一个SolrServer对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.31.100:8080/solr/collection1");
        //创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件，过滤条件，分页条件，排序条件，设置高亮
//        query.set("q","*:*");
//        query.setQuery("*:*");//查询所有无法指定高亮
        query.setQuery("手机");
        query.setStart(30); //起始记录数
        query.setRows(20); //每页显示记录数
        //设置默认搜索域
        query.set("df","item_keywords");
        //设置高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");//设置高亮显示的域
        query.setHighlightSimplePre("<div>");//前缀
        query.setHighlightSimplePost("</div>");//后缀
        //执行查询，得到一个Response对象
        QueryResponse response = solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //取查询结果的总记录数
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = null;
            /*有高亮就显示高亮，没有高亮就显示原来的值*/
            if (list != null && list.size() >0){
                itemTitle = list.get(0);
            }else{
                itemTitle = (String) solrDocument.get("item_title");
            }
            /*有高亮就显示高亮，没有高亮就显示原来的值*/
            System.out.println(itemTitle);
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
            System.out.println("=============================================");
        }
    }
}
