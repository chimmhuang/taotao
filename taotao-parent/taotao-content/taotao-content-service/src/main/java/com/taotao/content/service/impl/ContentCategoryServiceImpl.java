package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 通过id查询内容分类列表
     * @param parentId
     * @return List<EasyUITreeNode>
     */
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //设置查询条件
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> resultList = new ArrayList<>();
        //将结果转换为EasyUITreeNode
        for (TbContentCategory contentCategory : list) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setText(contentCategory.getName());
            easyUITreeNode.setState(contentCategory.getIsParent()?"closed":"open");
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }


    /**
     * 添加内容分类
     * @param parentId 父节点
     * @param name 分类名称
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult addContentCatgory(long parentId, String name) {
        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //补全对象属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态。可选值：1(正常)，2(删除)
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        //添加内容列表
        contentCategoryMapper.insert(contentCategory);//插入完成后，contentCategory的新id被写入

        //判断父节点的isParent是否为true
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        if (!parent.getIsParent()){
            //如果父节点为叶子节点应该改为父节点
            parent.setIsParent(true);
            //更新父节点
            contentCategoryMapper.updateByPrimaryKey(parent);
        }

        return TaotaoResult.ok(contentCategory);
    }


    /**
     * 重命名内容分类
     * @param id 内容分类id
     * @param name 要修改的名称
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult renameContentCatgory(long id, String name) {
        //创建更新条件
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);

        //创建更新的内容
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        //执行更新
        contentCategoryMapper.updateByExampleSelective(contentCategory,example);

        return TaotaoResult.ok();
    }

    /**
     * 内容分类删除
     * @param id 分类id
     * @return
     */
    @Override
    public TaotaoResult deleteContentCatgory(long id) {

        //通过id查询该分类信息
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if(contentCategory.getIsParent()){
            //如果此分类下面有子分类，查询这些子分类的ID
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            List<TbContentCategory> categories = contentCategoryMapper.selectByExample(example);

            if (categories.size() != 0 ){
                //遍历这些子分类，一个一个删除
                for (TbContentCategory category : categories) {
                    deleteContentCatgory(category.getId());
                }
            }else{
                //查询不到子分类了，需要更改isParent
                contentCategory.setIsParent(false);
                //写入数据库
                contentCategoryMapper.updateByPrimaryKey(contentCategory);
            }
        }

        //删除该分类信息
        contentCategoryMapper.deleteByPrimaryKey(id);

        //删完之后，检查父节点的信息，是否需要更改isParent
        TbContentCategoryExample parentExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria parentCriteria = parentExample.createCriteria();
        parentCriteria.andParentIdEqualTo(contentCategory.getParentId());
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(parentExample);
        if (list.size() == 0){
            TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }

        return TaotaoResult.ok();
    }
}
