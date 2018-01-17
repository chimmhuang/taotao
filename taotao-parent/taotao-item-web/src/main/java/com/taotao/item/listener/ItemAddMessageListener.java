package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.manager.service.ItemService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ItemAddMessageListener implements MessageListener{

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品ID
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            Long itemId = Long.parseLong(strId);
            //根据商品ID查询商品信息及商品描述
            //等待事务提交
            Thread.sleep(1000);
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //使用freemarker生成静态页面
            Configuration configuration = freeMarkerConfig.getConfiguration();
            //1.创建模板
            //WEB-INF/ftl，改造jsp为ftl
            //2.加载模板对象
            Template template = configuration.getTemplate("item.ftl");
            //3.准备模板需要的数据
            Map data = new HashMap();
            data.put("item",item);
            data.put("itemDesc",itemDesc);
            //4.指定输出的目录及文件名
            Writer out = new FileWriter(new File(HTML_OUT_PATH + strId + ".html"));
            //5.生成静态页面
            template.process(data,out);
            //6.关闭流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
