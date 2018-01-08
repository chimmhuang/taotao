package com.taotao.contentTest;

import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类测试
 */
public class TestContentCatgory {

    /**
     * 测试内容分类添加dao
     */
    @Test
    public void testInsertReturn(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbContentCategoryMapper contentCategoryMapper = applicationContext.getBean(TbContentCategoryMapper.class);

        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //补全对象属性
        contentCategory.setParentId((long) 30);
        contentCategory.setName("测试2");
        //状态。可选值：1(正常)，2(删除)
        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        int insert = contentCategoryMapper.insert(contentCategory);

        System.out.println(insert);
        System.out.println(contentCategory.getId());

    }


    @Test
    public void test(){
        List<String> list = new ArrayList<>();
        System.out.println(list.size());
    }
}
