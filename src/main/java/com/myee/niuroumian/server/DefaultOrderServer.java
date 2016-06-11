package com.myee.niuroumian.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.myee.niuroumian.domain.ErrorCode;
import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.domain.OrderItemInfo;
import com.myee.niuroumian.protocol.Message;
import com.myee.niuroumian.protocol.MessageType;
import com.myee.niuroumian.service.OrderService;
import com.myee.niuroumian.util.AjaxResultObj;
import com.myee.niuroumian.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Create a WebSocketHandler
 * Created by Jelynn on 2016/6/3.
 */
public class DefaultOrderServer extends AbstractWebSocketHandler implements OrderServer {

    private static final Logger LOG;
    private static final Map<String, WebSocketSession> connections;
    private static final ConcurrentMap<String, String> sessionKeys;

    @Autowired
    private OrderService orderService;

    private static ThreadLocal<StringBuffer> msgTheadLocal = new ThreadLocal<StringBuffer>();

    static {
        connections = Maps.newConcurrentMap();
        LOG = LoggerFactory.getLogger(DefaultOrderServer.class);
        sessionKeys = Maps.newConcurrentMap();
    }

    /**
     * message TYPE
     * 注册消息
     * {"messageType":"REGISTER","body":{},"userId":"1231231231231","shopId":1100}
     * 通知消息
     * {"messageType":"NOTIFICATION","body":[{"dishId":12,"quantity":1},{"dishId":12,"quantity":1}],"userId":"123112","shopId":"233331"}
     *
     * @param session
     * @param text
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage text) throws Exception {
        try {
            Message inMessage = Message.parse(text.getPayload());
            AjaxResultObj outMessage = handleMessage(inMessage, session);
            if (null != outMessage) {
                sendMessage(session, outMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AjaxResultObj handleMessage(Message message) {
        String shopId = message.getShopId();
        WebSocketSession session  = connections.get(shopId);
        return handleMessage(message,session);
    }

    @Override
    public AjaxResultObj handleMessage(Message message, WebSocketSession session) {
        LOG.info("============handleMessage===================");
        AjaxResultObj outMessage = null;
        if (message.is(MessageType.REGISTER)) {
            outMessage = handleRegister(session, message);
        } else if (message.is(MessageType.NOTIFICATION)) {
            outMessage = handleNotification(session, message);
        } else if (message.is(MessageType.UNREGISTER)) {
            outMessage = handleUnregister(message);
        }
        return outMessage;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        if (StringUtils.isNotBlank(sessionId)) {
            String idKey = sessionKeys.get(sessionId);
            if (idKey != null && connections.containsKey(idKey)) {
                connections.remove(idKey);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        LOG.debug("websocket connection closed......");
        connections.remove(session.getAttributes().get(Constant.SESSION_USERID));
    }

    //注册只有连接时才能写入session和Cache
    public AjaxResultObj handleRegister(WebSocketSession session, final Message register) {
        String userId = register.getUserId();
        String shopId = register.getShopId();
        LOG.info("================sessionID============="+session.getId());
        WebSocketSession shopSession  = connections.get(shopId);
        if(StringUtils.isNotBlank(shopId) && (shopSession == null || !shopSession.isOpen())){
            connections.put(shopId, session);
            sessionKeys.put(session.getId(), shopId);
            LOG.info(shopId+" is Registered");
            //查询未就餐订单
        }
        List<OrderInfo> lists = orderService.queryNotRepastOrder(Long.parseLong(shopId));

        if (StringUtils.isNotBlank(userId) && !connections.containsKey(userId)) {
            connections.put(userId, session);
            sessionKeys.put(session.getId(), userId);
            LOG.info(userId + " is Registered");
        }

        return AjaxResultObj.success(new Message(MessageType.NOTIFICATION,lists,  userId,  shopId));
    }

    /**
     * 向餐厅点单员手机推送下单信息
     *
     * @param wss
     * @param notification
     * @return
     */
    @SuppressWarnings("rawtypes")
    public AjaxResultObj handleNotification(WebSocketSession wss, final Message notification) {
        AjaxResultObj result = new AjaxResultObj();
        String shopId = notification.getShopId();
        LOG.debug("============= send Notification to ......"+shopId);
        WebSocketSession shopSession = connections.get(shopId);
        if(shopSession == null || !shopSession.isOpen()){
            result.failed(ErrorCode.SHOPNOTONLINE.getValue(),"shop is not online");
        }else{
            try {
                sendMessage(shopSession, AjaxResultObj.success(notification));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            result.success();
        }
        return result ;
    }

    public AjaxResultObj handleUnregister(final Message unregister) {
        // TODO Auto-generated method stub
        return null;
    }

    void sendMessage(WebSocketSession wss, AjaxResultObj responseData) throws IOException {
        String rawString = JSON.toJSONString(responseData, SerializerFeature.DisableCircularReferenceDetect);
        TextMessage txtMessge = new TextMessage(rawString);
        wss.sendMessage(txtMessge);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

//    private OrderInfo getOrderInfo(String userId, String shopId, String orderDetail) {
//        JSONArray jsonArray = JSON.parseArray(orderDetail);
//        OrderInfo orderInfo = new OrderInfo();
//        orderInfo.setShopId(Long.parseLong(shopId));
//        orderInfo.setUserId(userId);
//
//        if (jsonArray.size() > 0) {
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject object = jsonArray.getJSONObject(i);
//                OrderItemInfo orderItemInfo = new OrderItemInfo();
//                orderInfo.setUserId(userId);
//                orderInfo.setShopId(Long.parseLong(shopId));
//                orderItemInfo.setDishId(object.getLong("dishId"));
//                orderItemInfo.setQuantity(object.getInteger("quantity"));
//                orderItemInfo.setOrderInfo(orderInfo);
//                orderInfo.getItems().add(orderItemInfo);
//            }
//            orderInfo.setCount(orderInfo.getItems().size());
//        }
//        return orderInfo;
//    }

}
