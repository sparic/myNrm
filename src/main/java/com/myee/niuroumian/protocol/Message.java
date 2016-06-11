package com.myee.niuroumian.protocol;

import com.alibaba.fastjson.JSON;

public class Message {
    private           MessageType messageType;
    private           Object      body;
    private String userId;
    private String shopId;

    public Message() {
    }

    public Message(MessageType messageType,Object body, String userId, String shopId) {
        this.messageType = messageType;
        this.body = body;
        this.userId = userId;
        this.shopId = shopId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean is(MessageType messageType) {
        return this.messageType == messageType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public static Message parse(String text) throws InvalidMessageException {
        try {
            return JSON.parseObject(text, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidMessageException();
        }
    }

    public static String toRawString(Message message) {
        return JSON.toJSONString(message);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private MessageType messageType = MessageType.NOTIFICATION;

        private Object body;
        private String userId;
        private String shopId;

        public Builder body(Object data) {
            this.body = data;
            return this;
        }

        public Builder messageType(MessageType type) {
            this.messageType = type;
            return this;
        }

        public Builder json(Object data) {
            this.body = JSON.toJSONString(data);
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setMessageType(messageType);
            message.setUserId(userId);
            message.setShopId(shopId);
            message.setBody(body);
            return message;
        }
    }
}
