package com.taotao.manager.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.List;

public interface ItemService {

    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int startPage,int pageSize);
    TaotaoResult addItem(TbItem item,String desc);
    TaotaoResult updateItemStatus(List<Long> ids,Byte status);
    TaotaoResult deleteItem(List<Long> ids);

}
