package com.taotao.search.controller;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue = "1")Integer page, Model model) throws Exception{

            //需要对请求参数进行转码处理
            queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
            SearchResult search = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
            //传递给页面
            model.addAttribute("query",queryString);
            model.addAttribute("totalpage",search.getPageCount());
            model.addAttribute("itemList",search.getItemList());
            model.addAttribute("page",page);
//            int i = 1;
//            int j = 0;
//            int k = 1/0;
            return "search";

    }
}
