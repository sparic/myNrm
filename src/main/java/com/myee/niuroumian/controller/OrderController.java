package com.myee.niuroumian.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.myee.niuroumian.domain.*;
import com.myee.niuroumian.service.OrderService;
import com.myee.niuroumian.service.UserService;
import com.myee.niuroumian.service.WeixinService;
import com.myee.niuroumian.util.AjaxResultObj;
import com.myee.niuroumian.util.AppPayBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

//import com.myee.niuroumian.domain.PayState;

/**
 * Created by Jelynn on 2016/6/3.
 */
@Controller
@RequestMapping("order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private UserService userService;

    /**
     * 取消订单/传菜
     *
     * @return
     */
    @RequestMapping(value = "/orderCancel",method = RequestMethod.POST)
    @ResponseBody
    public String orderCancel(@RequestParam(value = "message") String message) {
        JSONObject object = JSON.parseObject(message);
        if (StringUtils.isNotBlank(message)) {
            Long orderId = object.getLong("orderId");
            OrderInfo orderInfo = orderService.getOrderInfo(orderId);
            Long shopId = object.getLong("shopId");
            orderInfo.setShopId(shopId);
            int orderState = object.getInteger("orderState");
            orderInfo.setOrderState(orderState);
            Date date = new Date();
            orderInfo.setUpdateTime(new Timestamp(date.getTime()));
            OrderInfo result = orderService.createOrder(orderInfo);
            //将当前就餐等位号存到缓存中

            orderService.setCurrentRepastNOToRedis(result.getWaitNo(),shopId);
            logger.info("=============set currentRepastNO "+result.getWaitNo());
            return JSON.toJSONString(AjaxResultObj.success(result), SerializerFeature.DisableCircularReferenceDetect);
        }
        return JSON.toJSONString(AjaxResultObj.failed());
    }

    /**
     * 下单
     * orderDetail数据格式
     * {"userId":202,"shopId":27,"orderType":7,"order":[{"dishId":2,"quantity":2},{"dishId":2,"quantity":2}]}
     *
     * @return
     */
    @RequestMapping(value = "/orderOffline",method = RequestMethod.POST)
    @ResponseBody
    public String orderOffline(@RequestParam(value = "orderDetail") String orderDetail) {
        JSONObject object = JSONObject.parseObject(orderDetail);
        OrderInfo orderResult = new OrderInfo();

        OrderInfo orderInfo = new OrderInfo();
        String userId = object.getString("userId");
        Long shopId = object.getLong("shopId");
        Integer orderType = object.getInteger("orderType");
        orderInfo.setOrderType(orderType);
        orderInfo.setUserId(userId);
        orderInfo.setShopId(shopId);
        Date date = new Date();
        orderInfo.setCreateTime(new Timestamp(date.getTime()));
        //TODO

        String order = object.getString("order");
        JSONArray jsonArray = JSON.parseArray(order);
        double price=0;
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                OrderItemInfo orderItemInfo = new OrderItemInfo();
//                orderItemInfo.setDishId(item.getLong("dishId"));
                DishInfo dishInfo = orderService.findById(item.getLong("dishId"));
                orderItemInfo.setDishInfo(dishInfo);
                int quantity = item.getInteger("quantity");
                orderItemInfo.setQuantity(quantity);
//                orderItemInfo.setOrderInfo(orderInfo);
                orderInfo.getItems().add(orderItemInfo);
                price +=sumPrice(dishInfo.getDishPrice(),quantity);
            }
            orderInfo.setCount(orderInfo.getItems().size());
        }
        orderInfo.setOrderPrice(new BigDecimal(price).setScale(2,   BigDecimal.ROUND_HALF_UP));

        if(orderType == 6){
            //线上点单
            orderResult = orderService.createOrder(orderInfo);//存数据库
            //调用微信第三方，生成预订单
//            UnifiedorderResult unifiedorderResult = weixinService.payUnifiedorder(orderResult, userId);
            System.out.println("================start6");
            AppPayBean appPayBean = (AppPayBean)weixinService.payUnifiedorder(orderResult, userId);
            System.out.println("================start6appPayBeanappPayBean:"+appPayBean.getPrepay_id());
            appPayBean.setAppId("wxe67244505b4041b6");
            appPayBean.setNonceStr(appPayBean.getNonce_str());
            appPayBean.setSignType("MD5");
            appPayBean.setPaySign(appPayBean.getSign());

            Object o = JSON.toJSON(appPayBean);
            if (o != null) {
                return JSON.toJSONString(AjaxResultObj.success(o), SerializerFeature.DisableCircularReferenceDetect);
            }
        }else{
            //线下点单
            orderInfo.setPayState(PayState.SUCCESS.getValue());
            orderInfo.setPayTime(new Timestamp(date.getTime()));
            orderInfo.setOrderState(OrderState.WAITING.getValue());
            orderInfo.setWaitNo(orderService.generateWaitNo(shopId));
            orderResult = orderService.createOrder(orderInfo);//存数据库
            if (orderResult != null) {
                return JSON.toJSONString(AjaxResultObj.success(orderResult),SerializerFeature.DisableCircularReferenceDetect);
            }
        }
        return JSON.toJSONString(AjaxResultObj.failed());
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/getOrderInfo",method = RequestMethod.POST)
    @ResponseBody
    public String getOrderInfo(@RequestParam(value = "orderId") Long orderId) {
        OrderInfo orderInfo = orderService.getOrderInfo(orderId);
        if (orderInfo != null) {
            return JSON.toJSONString(AjaxResultObj.success(orderInfo),SerializerFeature.DisableCircularReferenceDetect);
        }
        return JSON.toJSONString(AjaxResultObj.failed());
    }

    private double sumPrice(BigDecimal unitPrice,int count){
        return unitPrice.multiply(new BigDecimal(count)).doubleValue();
    }
}

