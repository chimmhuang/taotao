package com.taotao.manager.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品分类管理Service
 */
public interface ItemCatService {

    /**
     * 查询商品分类列表
     * @param parentId 父节点
     * @return List<EasyUITreeNode>
     */
    List<EasyUITreeNode> getItemCatList(long parentId);
}
