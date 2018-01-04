package com.taotao.manager.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.manager.service.ItemDescService;
import com.taotao.pojo.TbItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemDescController {

    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping("/rest/item/desc/{id}")
    @ResponseBody
    public TaotaoResult queryItemDesc(@PathVariable("id") Long id){
        TbItemDesc itemDesc = itemDescService.getItemDescById(id);
        TaotaoResult result = TaotaoResult.ok(itemDesc);
        return result;
    }
}
