package com.myee.niuroumian.dao;

import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.util.TimeUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;


/**
 * Created by Jelynn on 2016/6/1.
 */
public interface OrderInfoDao extends JpaRepository<OrderInfo, Long> {

    @Modifying
    @Query("update OrderInfo oi set oi.orderState = ?3,oi.updateTime = ?4 where oi.shopId = ?1 and oi.orderId = ?2")
    @Transactional
    int updateOrderState(Long shopId, Long orderId,int orderState,Timestamp updateTime);

    @Modifying
    @Query("update OrderInfo oi set oi.orderState = ?3,oi.payTime = ?4 where oi.shopId = ?1 and oi.orderId = ?2")
    @Transactional
    int updatePayState(Long shopId,Long orderId,int payState,Timestamp payTime);

    @Query("select waitNo from OrderInfo oi where oi.shopId = ?1 and oi.createTime between ?2 and ?3 order by waitNo desc ")
    List<Integer> generateWaitNo(Long shopId, Timestamp startTime, Timestamp endTime);

//    @Query("select OrderInfo from OrderInfo oi where oi.shopId = ?1 and oi.payState=1 and oi.orderState not in(1,3,4) and oi.createTime between ?2 and ?3 order by oi.waitNo asc ")
//    List<OrderInfo> queryNotRepastOrder(Long shopId, Timestamp startTime, Timestamp endTime);


//    List<OrderInfo> queryCustomerOrderHistory(Long shopId, Timestamp startTime, Timestamp endTime);

//    /**
//     * 查询某一店铺下某位顾客的已支付订单列表
//     * @param openId
//     * @param shopId
//     * @return
//     */
//    @Query("SELECT oi FROM " +
//            " OrderInfo oi where oi.payState = 1 and oi.userInfo.userId = 'oLueSs4LYOyeB2kL4I6VM4UmQnrM' and oi.shopId = 23331" +
//            " ORDER BY oi.createTime desc")
//    List<OrderInfo> queryCustomerOrderByShop(String openId, Long shopId);

//
//    /**
//     * 查询某顾客的所有已支付的订单
//     * @param openId
//     * @param start
//     * @param end
//     * @return
//     */
//    List<Long> queryCustomerAllOrderIds(String openId, Long start, Long end);
//
//    /**
//     * 查询某个订单下所有的所需信息
//     * @param orderId
//     * @param openId
//     * @param shopId
//     * @param start
//     * @param end
//     * @return
//     */
//
//    List<OrderInfo> queryCustomerOrderDetail(Long orderId, String openId, Long shopId, Long start, Long end);
    @Query("select oi from OrderInfo oi where oi.shopId = ?1 and oi.payState=1 and oi.orderState=1 and oi.createTime between ?2 and ?3 order by oi.waitNo asc ")
    List<OrderInfo> queryNotRepastOrder(Long shopId, Timestamp startTime, Timestamp endTime);
}
