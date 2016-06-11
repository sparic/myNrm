package com.myee.niuroumian.service;

import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.util.AppPayBean;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;

import java.util.Map;

/**
 * Created by Ray.Fu on 2016/6/1.
 */
public interface WeixinService {
    Map findQuickOrderMenu(Long storeId);

    /**
     * 微信支付统一
     * @param orderInfo
     * @param openId
     * @return
     */
    public AppPayBean payUnifiedorder(OrderInfo orderInfo,String openId);

    /**
     * 往消费者推送排号信息
     * @param tokenNum
     */
    public void pushTokenToCustomer(int tokenNum, String openId, String fromUser);
}
