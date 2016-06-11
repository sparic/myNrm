package com.myee.niuroumian.service.impl;

import com.myee.niuroumian.dao.OrderDaoImpl;
import com.myee.niuroumian.dao.DishDao;
import com.myee.niuroumian.dao.OrderInfoDao;
import com.myee.niuroumian.domain.DishInfo;
import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.domain.UserInfo;
import com.myee.niuroumian.dto.OrderRelDto;
import com.myee.niuroumian.service.OrderService;
import com.myee.niuroumian.service.RedisKeys;
import com.myee.niuroumian.service.RedisOperation;
import com.myee.niuroumian.util.TimeUtil;
import com.sun.jndi.toolkit.dir.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Condition;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Jelynn on 2016/6/2.
 */
@Service
public class OrderServiceImpl extends RedisOperation implements OrderService{

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private DishDao dishDao;

//    @Autowired
//    private OrderDaoImpl orderDao;

    @PersistenceContext
    private EntityManager entityManager;

    private  Timestamp start = TimeUtil.getTodayStart();
    private   Timestamp end = TimeUtil.getTodayEnd();

    @Autowired
    public OrderServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

//    public OrderServiceImpl() {
//    }

//    public OrderServiceImpl(OrderInfoDao orderInfoDao) {
//        this.orderInfoDao = orderInfoDao;
//    }

    @Transactional
    public OrderInfo createOrder(OrderInfo orderInfo){
        return orderInfoDao.save(orderInfo);
    }

    public int updateOrderState(OrderInfo orderInfo){
        return orderInfoDao.updateOrderState(orderInfo.getShopId(), orderInfo.getOrderId(), orderInfo.getOrderState(), orderInfo.getUpdateTime());
    }

    /**
     * @param orderInfo
     * @return
     */
    @Override
    public int updatePayState(OrderInfo orderInfo) {
        return orderInfoDao.updatePayState(orderInfo.getShopId(), orderInfo.getOrderId(), orderInfo.getPayState(), orderInfo.getPayTime());
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoDao.findOne(orderId);
    }

    @Override
    public int generateWaitNo(Long shopId) {
        List<Integer> list = orderInfoDao.generateWaitNo(shopId, start, end);
        int waitNo;
        if(list == null || list.size() == 0){
            waitNo = 1;
        }else{
            waitNo = list.get(0)+1;
        }
        return waitNo;
    }

    @Override
    public  List<OrderInfo> queryNotRepastOrder(Long shopId) {
        LOG.info("   query not repast order for  "+shopId+"  from "+start+" to   "+end);
        return orderInfoDao.queryNotRepastOrder(shopId, start, end);
    }

    @Override
    public List<Map<String,List<OrderRelDto>>> queryCustomerOrder(String openId, Long shopId, Long start, Long end) {
        //根据条件查询订单表
        List<Long> orderIdList = new ArrayList<Long>();
        List<Map<String,List<OrderRelDto>>> mapList = new ArrayList<>();
        if(shopId != null) {
            //如果店铺ID不为空，则查询某家店铺的顾客的所有已支付的订单
            Integer startInt = start.intValue();
            Integer endInt = end.intValue();
            orderIdList = listContractBorrows(openId,shopId,startInt,endInt);
        } else {
            Integer startInt = start.intValue();
            Integer endInt = end.intValue();
            //如果店铺ID为空，则查询某顾客的所有已支付的订单
            orderIdList = listContractBorrows(openId,shopId,startInt,endInt);
        }
        for (Long orderId : orderIdList) {
            String sql = "SELECT New com.myee.niuroumian.dto.OrderRelDto(oi.orderId,oi.count,oi.orderPrice,ui.nickName,oii.orderItemId,di.dishName,oii.quantity) FROM OrderItemInfo oii,OrderInfo oi,DishInfo di,UserInfo ui where oii.orderId = oi.orderId" +
                    " and oii.dishInfo.dishId = di.dishId and oi.userId = ui.openId" +
                    " and oi.payState = 1 and oi.orderId="+orderId+" and oi.userId = '"+openId+"' and oi.shopId = "+shopId+ " ORDER BY oi.createTime desc";
            Query query = entityManager.createQuery(sql);
            List<OrderRelDto> allInfoList = query.getResultList();
            Map map = new HashMap();
            map.put("order",allInfoList);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public List<Map<String, List<OrderRelDto>>> queryShopOwnerOrder(Long shopId, Long start, Long end) {
        //根据条件查询订单表
        List<Long> orderIdList = new ArrayList<Long>();
        List<Map<String,List<OrderRelDto>>> mapList = new ArrayList<>();
        if(shopId != null) {
            //如果店铺ID不为空，则查询某家店铺的顾客的所有已支付的订单
            Integer startInt = start.intValue();
            Integer endInt = end.intValue();
            orderIdList = listContractBorrows(null,shopId,startInt,endInt);
        } else {
            Integer startInt = start.intValue();
            Integer endInt = end.intValue();
            //如果店铺ID为空，则查询某顾客的所有已支付的订单
            orderIdList = listContractBorrows(null,shopId,startInt,endInt);
        }
        for (Long orderId : orderIdList) {
            String sql = "SELECT New com.myee.niuroumian.dto.OrderRelDto(oi.orderId,oi.count,oi.orderPrice,ui.nickName,oii.orderItemId,di.dishName,oii.quantity) FROM OrderItemInfo oii,OrderInfo oi,DishInfo di,UserInfo ui where oii.orderId = oi.orderId" +
                    " and oii.dishInfo.dishId = di.dishId and oi.userId = ui.openId" +
                    " and oi.payState = 1 and oi.orderId="+orderId+" and oi.shopId = "+shopId+ " ORDER BY oi.createTime desc";
            Query query = entityManager.createQuery(sql);
            List<OrderRelDto> allInfoList = query.getResultList();
            Map map = new HashMap();
            map.put("order",allInfoList);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public DishInfo findById(Long dishId) {
        return dishDao.findOne(dishId);
    }

    @Override
    public void setCurrentRepastNOToRedis(int value,Long shopId) {
        String redisKey = RedisKeys.getCurrentRepastNO(shopId);
        set(redisKey,value);
    }

    @Override
    public int getCurrentRepastNO(Long shopId) {
        String redisKey = RedisKeys.getCurrentRepastNO(shopId);
        return Integer.parseInt(getSimple(redisKey).toString());
    }

    public List<Long> listContractBorrows(String openId, Long shopId,int startNum, int endNum) {
        String sql = null;
        if (shopId != null) {
            sql = "SELECT distinct oi.orderId FROM OrderItemInfo oii,OrderInfo oi,DishInfo di,UserInfo ui where oii.orderId = oi.orderId" +
                    " and oii.dishInfo.dishId = di.dishId and oi.userId = ui.openId" +
                    " and oi.payState = 1 "+
                    (openId==null?"":" and oi.userId = '"+openId+"'")+
                     " and oi.shopId = "+shopId+ " ORDER BY oi.createTime desc";
        } else {
            sql = "SELECT distinct oi.orderId FROM OrderItemInfo oii,OrderInfo oi,DishInfo di,UserInfo ui where oii.orderId = oi.orderId" +
                    " and oii.dishInfo.dishId = di.dishId and oi.userId = ui.openId" +
                    " and oi.payState = 1 " +
                    (openId==null?"":" and oi.userId = '"+openId+"'")+
                    " ORDER BY oi.createTime desc";
        }
        Query query = entityManager.createQuery(sql);
        query.setFirstResult(startNum);
        query.setMaxResults(endNum);
        List<Long> list = query.getResultList();
        return  list;
    }


}
