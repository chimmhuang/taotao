package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户处理Controller
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;


    /**
     * 检查数据是否可用
     * @param param 要检验的数据
     * @param type 校验的数据类型 // 1.username   2.phone   3.email
     * @return TaotaoResult
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param,@PathVariable Integer type){
        TaotaoResult result = userService.checkData(param, type);
        return result;
    }


    /**
     * 用户注册
     * @param user TbUser对象
     * @return TaotaoResult
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user){
        TaotaoResult result = userService.createUser(user);
        return result;
    }


    /**
     * 用户登陆
     * @param username 用户名
     * @param password 密码
     * @return TaotaoResult
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username , String password
            , HttpServletRequest request
            , HttpServletResponse response){
        TaotaoResult result = userService.login(username, password);
        if (result.getStatus() == 200){
            //登陆成功写入cookie
            //把token写入cookie
            CookieUtils.setCookie(request,response,TOKEN_KEY,result.getData().toString());
        }
        return result;
    }


    /**
     * 通过token查询用户信息
     * @param token SessionID
     * @return TaotaoResult
     */
//    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET,
//            //指定返回响应数据的content-type
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String getUserByToken(@PathVariable String token, String callback){
//        TaotaoResult result = userService.getUserByToken(token);
//        //判断是否为Jsonp请求
//        if (StringUtils.isNotEmpty(callback)){
//            return callback + "(" + JsonUtils.objectToJson(result) + ");";
//        }
//        return JsonUtils.objectToJson(result);
//    }
    //jsonp的第二种方法，spring4.1以上版本使用
    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback){
        TaotaoResult result = userService.getUserByToken(token);
        //判断是否为Jsonp请求
        if (StringUtils.isNotEmpty(callback)){
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            //设置回调方法
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return JsonUtils.objectToJson(result);
    }


    @RequestMapping("/user/logout/{token}")
    @ResponseBody
    public TaotaoResult logout(@PathVariable String token){
        TaotaoResult result = userService.logout(token);
        return result;
    }


}
