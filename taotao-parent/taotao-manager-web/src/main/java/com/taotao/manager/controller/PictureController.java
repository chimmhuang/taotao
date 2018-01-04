package com.taotao.manager.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.manager.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile) {
        //此方法原可以返回一个pojo，但此处展示使用map
        try {
            //接收上传的文件
            //获取扩展名
            String originalFilename = uploadFile.getOriginalFilename();//取全名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1 );

            //上传图片到服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.config");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url = IMAGE_SERVER_URL + url;

            //回写响应上传图片的url
            Map result = new HashMap();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);
        }catch (Exception ex){
            ex.printStackTrace();
            Map result = new HashMap();
            result.put("error",1);
            result.put("message","图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}
