package com.taotao.manager.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.manager.service.ItemParamService;
import com.taotao.pojo.TbItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/rest/item/param/item/{id}")
    @ResponseBody
    public TaotaoResult getItemParamById(@PathVariable("id") Long id){
        TbItemParamItem itemParamItem = itemParamService.getItemParamById(id);
        return TaotaoResult.ok(itemParamItem);
    }
}
