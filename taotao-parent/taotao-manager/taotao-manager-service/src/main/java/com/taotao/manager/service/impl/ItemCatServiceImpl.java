package com.taotao.manager.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.manager.service.ItemCatService;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 查询商品分类列表
     *
     * @param parentId 父节点
     * @return List<EasyUITreeNode>
     */
    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        //根据父节点ID查询子节点列表，设置查询条件
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        //执行查询
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        //转换成EasyUITreeNode列表
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat itemCat : list) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(itemCat.getId());
            //如果节点下有子节点"closed"，如果没有子节点"open"
            easyUITreeNode.setState(itemCat.getIsParent()?"closed":"open");
            easyUITreeNode.setText(itemCat.getName());
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }
}
