package com.taotao.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面展示Controller
 */
@Controller
public class PageController {

    /**
     * 展示首页
     * @return
     */
    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

    /**
     * 展示指定的页面
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public String showPage(String page){
        return  page;
    }
}
