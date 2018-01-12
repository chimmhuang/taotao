package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.utils.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;

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

        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
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

        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    /**
     * 内容删除
     * @param id 内容id
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult deleteContent(Long id) {
        TbContent content = contentMapper.selectByPrimaryKey(id);
        contentMapper.deleteByPrimaryKey(id);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    /**
     * 通过分类id查询内容
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentByCid(long cid) {
        /****************day05-查询缓存*********************/
        //查询内容前，先查询缓存
        //添加缓存不能影响正常逻辑，所以应该try catch
        try {
            //查询缓存
            String contentJson = jedisClient.hget(INDEX_CONTENT, cid + "");
            if (StringUtils.isNotEmpty(contentJson)){
                //查询到结果，将结果转化为List返回
                List<TbContent> list = JsonUtils.jsonToList(contentJson, TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        /****************day05-查询缓存*********************/
        /****************day04-查询内容*********************/
        //缓存中没有命中，需要查询数据库
        //设置查询条件
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);

        //执行查询
        List<TbContent> contents = contentMapper.selectByExample(example);
        /****************day04-查询内容*********************/
        /****************day05-添加缓存*********************/
        try {
            jedisClient.hset(INDEX_CONTENT,cid+"", JsonUtils.objectToJson(contents));
        }catch (Exception e){
            e.printStackTrace();
        }
        /****************day05-添加缓存*********************/
        return contents;
    }
}
