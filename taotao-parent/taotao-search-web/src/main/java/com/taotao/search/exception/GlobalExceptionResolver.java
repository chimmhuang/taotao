package com.taotao.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler, Exception ex) {
        logger.info("进入全局异常处理器。。。。");
        logger.debug("测试handler的类型:"+handler.getClass());
        //控制台打印异常
        ex.printStackTrace();
        //向日志文件中写入异常
        logger.error("系统发生异常",ex);
        //发邮件
        //jmail
        //发短信
        //有第三方接口，比如短信群发平台
        //展示错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message","出错啦，攻城狮正在努力抢修中~");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
