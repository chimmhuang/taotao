package com.taotao.manager.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.manager.service.ItemService;
import com.taotao.manager.utils.SubStringIds;
import com.taotao.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 商品上架
     * @param ids 要上架的商品ID(可以为多个，通过逗号隔开)
     * @return TaotaoResult
     */
    @RequestMapping(value = "/rest/item/reshelf",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult shelfItem(String ids){

        //将商品ID装入list
        List<Long> idList = SubStringIds.getIdList(ids);
        ////商品状态，1-正常，2-下架，3-删除
        TaotaoResult result = itemService.updateItemStatus(idList, (byte) 1);
        return result;
    }

    /**
     * 商品下架
     * @param ids 要下架的商品ID(可以为多个，通过逗号隔开)
     * @return TaotaoResult
     */
    @RequestMapping(value = "/rest/item/instock",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult instockItem(String ids){
        //将商品ID装入list
        List<Long> idList = SubStringIds.getIdList(ids);
        //商品状态，1-正常，2-下架，3-删除
        TaotaoResult result = itemService.updateItemStatus(idList, (byte) 2);
        return result;
    }

    /**
     * 商品删除
     * @param ids 要下架的商品ID(可以为多个，通过逗号隔开)
     * @return TaotaoResult
     */
    @RequestMapping(value = "/rest/item/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteItem(String ids){
        //将商品ID装入list
        List<Long> idList = SubStringIds.getIdList(ids);

        TaotaoResult result = itemService.deleteItem(idList);
        return result;
    }


    /**
     * 商品添加功能
     * @param item TbItem对象
     * @param desc 商品描述
     * @return TaotaoResult
     */
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(TbItem item,String desc){
        TaotaoResult result = itemService.addItem(item,desc);
        return result;
    }

}
