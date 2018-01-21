package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 订单服务模块
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;//订单ID的key
    @Value(("${ORDER_ID_BEGIN_VALUE}"))
    private String ORDER_ID_BEGIN_VALUE;//订单ID的初始值
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;//订单明细表ID的key

    /**
     * 订单生成
     * @param orderInfo
     * @return
     */
    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //1.生成订单id
        if (!jedisClient.exists(ORDER_ID_GEN_KEY)){
            //设置初始值
            jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_BEGIN_VALUE);
        }
        String orderID = jedisClient.incr(ORDER_ID_GEN_KEY).toString();//自增
        //2.向订单表插入数据，需要补全pojo属性
        orderInfo.setOrderId(orderID);
        //免邮费
        orderInfo.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //向订单表插入数据
        orderMapper.insert(orderInfo);
        //3.向订单明细表插入数据
        for (TbOrderItem orderItem : orderInfo.getOrderItems()) {
            //获得明细主键
            String orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderID);
            //插入明细数据
            orderItemMapper.insert(orderItem);
        }
        //4.向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderID);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);

        //5.返回订单号
        return TaotaoResult.ok(orderID);
    }
}
