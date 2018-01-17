package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.manager.service.ItemService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品详情Controller
 */
@Controller
public class ItemController {


    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model){
        //根据商品ID查询商品信息
        TbItem tbItem = itemService.getItemById(itemId);
        //根据商品ID查询商品介绍
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);

        //把tbItem转成Item
        Item item = new Item(tbItem);

        //将数据传递给页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);

        return "item";
    }
}
