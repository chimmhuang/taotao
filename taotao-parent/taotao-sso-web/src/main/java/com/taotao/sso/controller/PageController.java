package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showLogin(String redirectUrl , Model model){
        //若有页面传过来，则跳转回去
        model.addAttribute("redirect",redirectUrl);
        return "login";
    }
}
