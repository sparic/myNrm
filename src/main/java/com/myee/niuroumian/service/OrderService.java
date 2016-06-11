package com.myee.niuroumian.service;

import com.myee.niuroumian.dao.OrderInfoDao;
import com.myee.niuroumian.domain.DishInfo;
import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.domain.OrderItemInfo;
import com.myee.niuroumian.dto.OrderRelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Jelynn on 2016/6/1.
 */

public interface OrderService {

     /**
      *保存订单
      * @param orderInfo
      * @return
      */
     OrderInfo createOrder(OrderInfo orderInfo);

     /**
      * 更新订单状态
      * @param orderInfo
      * @return
      */
     int updateOrderState(OrderInfo orderInfo);

     /**
      * 更新订单支付状态
      * @param orderInfo
      * @return
      */
     int updatePayState(OrderInfo orderInfo);

     /**
      * 获取订单信息
      * @param orderId
      * @return
      */
     OrderInfo getOrderInfo(Long orderId);

     /**
      * 生成订单号
      * @param shopId
      * @return
      */
     int generateWaitNo(Long shopId);

     /**
      * 生成订单号
      * @param shopId
      * @return
      */
     List<OrderInfo> queryNotRepastOrder(Long shopId);

     /**
      * 获取商品信息
      * @param dishId
      * @return
      */
     DishInfo findById(Long dishId);

     List<Map<String,List<OrderRelDto>>> queryCustomerOrder(String openId, Long shopId, Long start, Long end);

     List<Map<String,List<OrderRelDto>>> queryShopOwnerOrder(Long shopId, Long start, Long end);


     /**
      * 将数据插入缓存
      * @param shopId
      *  @param value
      * @return
      */
     void setCurrentRepastNOToRedis(int value,Long shopId);

     int getCurrentRepastNO(Long shopId);
}

