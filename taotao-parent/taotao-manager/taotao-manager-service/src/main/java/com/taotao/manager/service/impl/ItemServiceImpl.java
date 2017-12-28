package com.taotao.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.manager.service.ItemService;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * 通过ID查询商品
     *
     * @param itemId 商品ID
     * @return TbItem
     */
    @Override
    public TbItem getItemById(long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 分页查询商品
     *
     * @param startPage 起始页数
     * @param pageSize  一页显示数
     * @return EasyUIDataGridResult
     */
    @Override
    public EasyUIDataGridResult getItemList(int startPage, int pageSize) {
        //1.在执行查询之前配置分页条件，使用PageHelper的静态方法
        PageHelper.startPage(startPage,pageSize);

        //2.执行查询（因为Service层有applicationContext，所以直接new example）
        TbItemExample example = new TbItemExample();
        List<TbItem> itemList = tbItemMapper.selectByExample(example);//不给example赋值，即为查询所有

        //3.取分页信息。使用PageInfo对象取
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);

        //4.创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(itemList);

        return result;
    }
}
