package com.taotao.cart.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.manager.service.ItemService;
import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理Controller
 */
@Controller
public class CartController {


    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${CART_EXPIER}")
    private Integer CART_EXPIER;

    @Autowired
    private ItemService itemService;



    /**
     * 添加购物车
     * @param itemId 商品ID
     * @param num 数量
     * @return view
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId
            , @RequestParam(defaultValue = "1")Integer num
            , HttpServletRequest request
            , HttpServletResponse response){
        //1.从cookie中取购物车商品列表
        List<TbItem> cartList = getCartList(request);
        //2.判断商品在购物车中是否存在
        boolean hasItem = false;
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()){
                //3.如果存在数量相加
                tbItem.setNum(tbItem.getNum() + num );
                hasItem = true;
                break;
            }
        }
        if (!hasItem){
            //4.如果不存在，添加一个新的商品，根据ID查询商品
            TbItem tbItem = itemService.getItemById(itemId);
            //取一张图片
            String image = tbItem.getImage();
            if (StringUtils.isNotEmpty(image)){
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            //设置商品购买数量
            tbItem.setNum(num);
            //5.把商品添加到购物车列表
            cartList.add(tbItem);
        }
        //6.把商品列表写入cookie
        CookieUtils.setCookie(request,response,CART_KEY,JsonUtils.objectToJson(cartList),CART_EXPIER,true);
        return "cartSuccess";
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

    /**
     * 获取购物车列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request , Model model){
        //取购物车商品列表
        List<TbItem> cartList = getCartList(request);
        //传递给页面
        model.addAttribute("cartList",cartList);
        return "cart";
    }


    /**
     * 修改购物车商品数量
     * @param itemId 商品ID
     * @param num 修改后的商品数量
     * @return TaotaoResult
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num
            ,HttpServletRequest request
            ,HttpServletResponse response){
        //1.从cookie中取购物车列表
        List<TbItem> cartList = getCartList(request);
        //2.遍历商品列表，找到对应的商品
        for (TbItem tbItem : cartList) {
            if (itemId.longValue() == tbItem.getId()){
                //3.更新商品数量
                tbItem.setNum(num);
            }
        }
        //4.把商品列表写入cookie，包含有效期7天
        CookieUtils.setCookie(request,response,CART_KEY,JsonUtils.objectToJson(cartList),CART_EXPIER,true);

        //5.返回成功
        return TaotaoResult.ok();
    }


    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId
            ,HttpServletRequest request
            ,HttpServletResponse response){
        //1.从cookie中取商品列表
        List<TbItem> cartList = getCartList(request);
        //2.遍历列表，找到对应的商品
        for (TbItem tbItem : cartList) {
            if (itemId.longValue() == tbItem.getId()){
                //3.删除商品
                cartList.remove(tbItem);
                break;
            }
        }
        //4.更新cookie信息
        CookieUtils.setCookie(request,response,CART_KEY,JsonUtils.objectToJson(cartList),CART_EXPIER,true);
        //5.返回该网页，重定向
        return "redirect:/cart/cart.html";
    }

}
