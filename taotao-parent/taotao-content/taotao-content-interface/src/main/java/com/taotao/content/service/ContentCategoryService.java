package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCategoryList(long parentId);
    TaotaoResult addContentCatgory(long parentId,String name);
    TaotaoResult renameContentCatgory(long id,String name);
    TaotaoResult deleteContentCatgory(long id);
}
