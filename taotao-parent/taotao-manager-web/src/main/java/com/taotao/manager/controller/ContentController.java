package com.taotao.manager.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.manager.utils.SubStringIds;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容Controller
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;


    /**
     * 分页查询内容列表
     * @param categoryId 分类ID
     * @param page 起始页数
     * @param rows 一页显示数
     * @return EasyUIDataGridResult
     */
    @RequestMapping(value = "/content/query/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows){
        EasyUIDataGridResult contentList = contentService.getContentList(categoryId, page, rows);
        return contentList;
    }


    /**
     * 内容添加
     * @param content TbContent对象
     * @return TaotaoResult
     */
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content) {
        TaotaoResult result = contentService.addContent(content);
        return result;
    }


    /**
     * 内容修改
     * @param content TbContent对象
     * @return TaotaoResult
     */
    @RequestMapping("/content/edit")
    @ResponseBody
    public TaotaoResult editContent(TbContent content){
        TaotaoResult result = contentService.editContent(content);
        return  result;
    }


    @RequestMapping(value = "/content/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContent(String ids){
        List<Long> idList = SubStringIds.getIdList(ids);
        for (Long id : idList) {
            contentService.deleteContent(id);
        }
        return TaotaoResult.ok();
    }
}
