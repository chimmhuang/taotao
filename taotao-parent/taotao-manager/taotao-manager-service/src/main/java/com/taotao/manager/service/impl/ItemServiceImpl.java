package com.taotao.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.manager.service.ItemService;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "itemAddTopic")
    private Destination destination;//java的注解

    @Autowired
    private JedisClient jedisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${TIEM_EXPIRE}")
    private Integer TIEM_EXPIRE;

    /**
     * 通过ID查询商品
     *
     * @param itemId 商品ID
     * @return TbItem
     */
    @Override
    public TbItem getItemById(long itemId) {

        /**第9天---从redis缓存查询商品信息**/
        try {
            String itemJson = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
            if (StringUtils.isNotEmpty(itemJson)){
                //把itemJson转换为TbItem对象
                TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
                return tbItem;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        /**第9天---从redis缓存查询商品信息**/

        //从数据库中查询TbItem商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);

        /**第9天---将数据保存到redis缓存**/
        try {
            //把数据保存到缓存
            jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
            //设置缓存的有效期
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE",TIEM_EXPIRE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        /**第9天---将数据保存到redis缓存**/
        return tbItem;
    }

    /**
     * 分页查询商品
     *
     * @param startPage 起始页数
     * @param pageSize  一页显示数
     * @return EasyUIDataGridResult
     */
    @Override
    public EasyUIDataGridResult getItemList(int startPage, int pageSize) {
        //1.在执行查询之前配置分页条件，使用PageHelper的静态方法
        PageHelper.startPage(startPage,pageSize);

        //2.执行查询（因为Service层有applicationContext，所以直接new example）
        TbItemExample example = new TbItemExample();
        List<TbItem> itemList = tbItemMapper.selectByExample(example);//不给example赋值，即为查询所有

        //3.取分页信息。使用PageInfo对象取
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);

        //4.创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(itemList);

        return result;
    }

    /**
     * 添加商品功能
     * @param item TbItem对象
     * @param desc 商品描述
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //生成商品ID
        final long itemId = IDUtils.getItemId();
        //补全item的属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        //向商品表插入item数据
        tbItemMapper.insert(item);

        //创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());

        //向商品描述表插入desc数据
        tbItemDescMapper.insert(itemDesc);

        /*******第8天----向activemq发送商品添加事件*******/
        //向activemq发送商品添加消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //要发送商品ID
                TextMessage message = session.createTextMessage(itemId+"");
                return message;
            }
        });
        /*******第8天----向activemq发送商品添加事件*******/
        return TaotaoResult.ok();
    }

    /**
     * 上/下架商品（更新商品状态）
     * @param ids 商品ID集合
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult updateItemStatus(List<Long> ids,Byte status) {
        //商品状态，1-正常，2-下架，3-删除
        for (Long id : ids) {
            tbItemMapper.updateStatusById(id,new Date(),status);
        }
        return TaotaoResult.ok();
    }

    /**
     * 删除商品
     * @param ids 商品ID集合
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult deleteItem(List<Long> ids) {
        for (Long id : ids) {
            tbItemMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok();
    }


    /**
     * 通过ID查询商品介绍
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc getItemDescById(long itemId) {


        /**第9天---从redis缓存查询商品描述**/
        try {
            String itemDescJson = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
            if (StringUtils.isNotEmpty(itemDescJson)){
                //将json转换为对象
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(itemDescJson, TbItemDesc.class);
                return itemDesc;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        /**第9天---从redis缓存查询商品描述**/

        //从数据库查询商品描述
        TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

        /**第9天---将数据保存到redis缓存**/
        try {
            //将商品描述添加redis缓存
            jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC",JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC",TIEM_EXPIRE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        /**第9天---将数据保存到redis缓存**/
        return itemDesc;
    }


}
