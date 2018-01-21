package com.taotao.order.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单确认页面处理Controller
 */
@Controller
public class OrderController {

    @Value("${CART_KEY}")
    private String CART_KEY;
    @Autowired
    private OrderService orderService;

    /**
     * 展示订单确认页面
     * @param request
     * @return
     */
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //用户必须是登陆状态
        //取用户id
        TbUser user = (TbUser) request.getAttribute("user");
        //根据用户信息取收获地址列表，使用静态数据。
        //把收获地址列表取出来，传递给页面
        //从cookie中取购物车商品列表，展示到页面
        List<TbItem> cartList = getCartList(request);
        //返回逻辑视图
        request.setAttribute("cartList",cartList);
        return "order-cart";
    }


    /**
     * 生成订单
     * @param orderInfo OrderInfo对象
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo ,HttpServletRequest request){
        //1.补全orderInfo
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());

        //2.调用service创建订单
        TaotaoResult result = orderService.createOrder(orderInfo);
        //取订单号
        String orderId = result.getData().toString();

        //3.返回逻辑视图
        request.setAttribute("orderId",orderId);
        request.setAttribute("payment",orderInfo.getPayment());

        //设置预计到达时间  3天
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date",dateTime.toString("yyyy-MM-dd"));
        return "success";
    }




    /**
     * 从cookie中取购物车列表
     * @param request
     * @return List<TbItem>
     */
    private List<TbItem> getCartList(HttpServletRequest request){
        //取购物车列表
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        //判断json是否为null
        if (StringUtils.isNotEmpty(json)){
            //将json转换为pojo
            List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
            return tbItems;
        }
        return new ArrayList<>();
    }
}
