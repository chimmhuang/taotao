package com.taotao.manager.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.manager.service.ItemService;
import com.taotao.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理Controller
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 通过ID查询商品
     * @param itemId 商品ID
     * @return TbItem
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId){
        //若itemId与requestMapping相同，则可以不用写@PathVariable
        TbItem tbItem =  itemService.getItemById(itemId);
        return tbItem;
    }

    /**
     * 分页查询所有商品
     * @param page startPage
     * @param rows pageSize
     * @return EasyUIDataGridResult
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
        EasyUIDataGridResult result= itemService.getItemList(page,rows);
        return result;
    }
}
