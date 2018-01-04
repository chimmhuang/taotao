package com.taotao.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.manager.service.ItemService;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

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

    /**
     * 添加商品功能
     * @param item TbItem对象
     * @param desc 商品描述
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //生成商品ID
        final long itemId = IDUtils.getItemId();
        //补全item的属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        //向商品表插入item数据
        tbItemMapper.insert(item);

        //创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());

        //向商品描述表插入desc数据
        tbItemDescMapper.insert(itemDesc);

        return TaotaoResult.ok();
    }

    /**
     * 上/下架商品（更新商品状态）
     * @param ids 商品ID集合
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult updateItemStatus(List<Long> ids,Byte status) {
        //商品状态，1-正常，2-下架，3-删除
        for (Long id : ids) {
            tbItemMapper.updateStatusById(id,new Date(),status);
        }
        return TaotaoResult.ok();
    }

    /**
     * 删除商品
     * @param ids 商品ID集合
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult deleteItem(List<Long> ids) {
        for (Long id : ids) {
            tbItemMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok();
    }


}
