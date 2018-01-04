package com.taotao.manager.testDao;

import com.taotao.mapper.TbItemMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class TestShelvesItem {

    /**
     * 测试商品上架功能（更改status）
     */
    @Test
    public void testUpdateStatus(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
        itemMapper.updateStatusById(143762283607646L,new Date(), (byte) 1);
    }
}
