package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示登陆和注册页面Controller
 */
@Controller
public class PageController {


    /**
     * 显示注册页面
     * @return
     */
    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }


    /**
     * 显示登陆页面
     * @return
     */
    @RequestMapping("/page/login")
    public String showLogin(){
        return "login";
    }
}
