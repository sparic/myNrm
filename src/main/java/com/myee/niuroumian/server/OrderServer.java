package com.myee.niuroumian.server;

import com.myee.niuroumian.protocol.Message;
import com.myee.niuroumian.util.AjaxResultObj;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by Jelynn on 2016/6/3.
 */
public interface OrderServer extends WebSocketHandler {

    AjaxResultObj handleMessage(Message message, WebSocketSession session);

    AjaxResultObj handleMessage(Message message);
}
