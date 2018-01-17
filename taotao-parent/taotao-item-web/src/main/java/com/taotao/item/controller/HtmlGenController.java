package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * 网页静态化处理Controller
 */
@Controller
public class HtmlGenController {

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;//已在springmvc中配置

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //生成静态页面
        Configuration configuration = freeMarkerConfig.getConfiguration();
        //不需要设置路径和编码，因为已经在springmvc中配置好了
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap();
        data.put("hello","spring freemarker test");
        Writer out = new FileWriter(new File("G:/IDEA_workspace/taotao/freemarker/out/test.html"));
        template.process(data,out);
        out.close();
        //返回结果
        return "OK";
    }

}
