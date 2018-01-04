package com.taotao.manager.testDao;

import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class TestGetItemParam {
    @Test
    public void testGetItemParam(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemParamItemMapper itemParamItemMapper = applicationContext.getBean(TbItemParamItemMapper.class);

        //通过ID查找规格
        TbItemParamItem tbItemParamItem = itemParamItemMapper.selectByPrimaryKey((long) 3);
        System.out.println(tbItemParamItem.toString());

        //通过item_id查找规格
        TbItemParamItemExample itemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = itemExample.createCriteria();
        criteria.andItemIdEqualTo(1433500495290l);
        List<TbItemParamItem> tbItemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(itemExample);
        for (TbItemParamItem itemParamItem : tbItemParamItems) {
            System.out.println(itemParamItem.toString());
        }
    }
//
//    @Test
//    public void testSelectByItemId(){
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
//        TbItemParamItemMapper itemParamItemMapper = applicationContext.getBean(TbItemParamItemMapper.class);
//
//        TbItemParamItem itemParamItem = itemParamItemMapper.selectByItemId(1433500495290l);
//        System.out.println(itemParamItem.toString());
//    }
}
