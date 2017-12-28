package com.taotao.manager.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;

import java.util.List;

public interface ItemService {
    /**
     * 通过ID查询商品
     * @param itemId 商品ID
     * @return TbItem
     */
    public TbItem getItemById(long itemId);


    /**
     * 分页查询商品
     * @param  startPage 起始页数
     * @param  pageSize 一页显示数
     * @return
     */
    public EasyUIDataGridResult getItemList(int startPage,int pageSize);
}
