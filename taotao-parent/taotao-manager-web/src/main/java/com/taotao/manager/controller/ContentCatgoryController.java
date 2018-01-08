package com.taotao.manager.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCatgoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;


    /**
     * 内容列表展示
     * @param id//父节点
     * @return list
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCatgoryList(@RequestParam(value = "id",defaultValue = "0") Long id){
        List<EasyUITreeNode> categoryList = contentCategoryService.getContentCategoryList(id);
        return categoryList;
    }


    /**
     * 添加内容分类
     * @param parentId 父节点
     * @param name 分类名称
     * @return TaotaoResult
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addContentCatgory(Long parentId,String name){
        TaotaoResult result = contentCategoryService.addContentCatgory(parentId, name);
        return result;
    }


    /**
     * 重命名内容分类
     * @param id 分类id
     * @param name 分类名称
     * @return TaotaoResult
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult renameContentCatgory(Long id,String name){
        TaotaoResult result = contentCategoryService.renameContentCatgory(id, name);
        return result;
    }


    /**
     * 删除内容分类
     * @param id 分类ID
     * @return TaotaoResult
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCatgory(Long id){
        TaotaoResult result = contentCategoryService.deleteContentCatgory(id);
        return result;
    }

}
