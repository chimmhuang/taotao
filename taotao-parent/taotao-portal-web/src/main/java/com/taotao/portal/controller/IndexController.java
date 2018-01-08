package com.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页展示Controller
 */
@Controller
public class IndexController {

    /**
     * 首页展示
     * @return
     */
    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }
}
