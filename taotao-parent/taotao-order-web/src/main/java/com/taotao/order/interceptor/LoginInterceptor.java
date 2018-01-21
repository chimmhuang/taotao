package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断用户是否登陆的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor{

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${SSO_LOGIN_URL}")
    private String SSO_LOGIN_URL;

    @Autowired
    private UserService userService;


    /**
     * 判断用户是否登陆
     * 执行方法之前，先执行此方法
     * @param request
     * @param response
     * @param o 这个是controller的方法
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //执行方法之前执行此方法
        //1.从cookie中取token信息
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);

        /*2.如果取不到token，跳转sso登陆页面，需要把当前请求的url作为参数传递给sso，
         sso登陆成功之后，跳转回当前请求页面 */
        if (StringUtils.isBlank(token)){
            //取当前的url
            String url = request.getRequestURL().toString();
            //没有token，跳转sso登陆页面
            response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
            //拦截
            return false;
        }

        //3.取到token，调用sso系统的服务，判断用户是否登陆
        TaotaoResult result = userService.getUserByToken(token);

        //4.如果用户未登录，即没取到用户信息，跳转到sso的登陆页面
        if (result.getStatus() != 200){
            //取当前的url
            String url = request.getRequestURL().toString();
            //没有token，跳转sso登陆页面
            response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
            //拦截
            return false;
        }

        //5.如果取到用户信息，即已登陆，放行
        //把用户信息放到request中
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user",user);
        //返回值true：放行，返回false：拦截
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //handler执行之后，ModelAndView返回之前
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //ModelAndView返回之后，执行，很少用到，一般做异常处理
    }
}
