package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 分页查询内容列表
     * @param startPage 起始页数
     * @param pageSize 一页显示数
     * @return List<EasyUIDataGridResult>
     */
    @Override
    public EasyUIDataGridResult getContentList(Long categoryId,Integer startPage, Integer pageSize) {
        //1.在执行查询之前配置分页条件，使用PageHelper的静态方法
        PageHelper.startPage(startPage,pageSize);

        //2.执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> contents = contentMapper.selectByExample(example);

        //3.取分页信息。使用PageInfo对象取
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(contents);

        //4.创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(contents);
        return result;
    }

    /**
     * 内容添加
     * @param content TbContent对象
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult addContent(TbContent content) {
        //补全pojo属性
        content.setCreated(new Date());
        content.setUpdated(new Date());

        //插入到对应的Tb_Content表
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    /**
     * 内容修改
     * @param content TbContent对象
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult editContent(TbContent content) {
        //添加更新时间
        content.setUpdated(new Date());

        //执行更新操作
        contentMapper.updateByPrimaryKeySelective(content);
        return TaotaoResult.ok();
    }

    /**
     * 内容删除
     * @param id 内容id
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult deleteContent(Long id) {
        contentMapper.deleteByPrimaryKey(id);
        return TaotaoResult.ok();
    }
}
