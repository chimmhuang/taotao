package com.taotao.pageHelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {

    /**
     * 测试分页
     * @throws Exception
     */
    @Test
    public void testPageHelper() throws Exception{
        //1.在mybatis的配置文件中配置分页插件
        //2.在执行查询之前配置分页条件，使用PageHelper的静态方法
        PageHelper.startPage(1,10);
        //3.执行查询
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        //创建example对象
        TbItemExample example = new TbItemExample();
//        TbItemExample.Criteria criteria =  example.createCriteria();
        //没有条件,criteria可以不创建
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //4.取分页信息。使用PageInfo对象取
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
        System.out.println("总记录数："+pageInfo.getTotal());
        System.out.println("总页数："+pageInfo.getPages());
    }
}
