package com.taotao.manager.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;


    @RequestMapping(value = "index/import",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult importAllItems(){
        try {
            TaotaoResult result = searchItemService.imporItemsToIndex();
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            return TaotaoResult.build(500,"导入数据失败");
        }
    }
}
