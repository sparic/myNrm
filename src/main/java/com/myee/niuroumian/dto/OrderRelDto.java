package com.myee.niuroumian.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Ray.Fu on 2016/6/8.
 */
@SuppressWarnings("serial")
public class OrderRelDto implements Serializable {

    private Long orderId;

    private Integer count;

    private BigDecimal orderPrice;

    private String nickName;

    private Long orderItemId;

    private String dishName;

    private Integer quantity;

    public OrderRelDto(Long orderId, Integer count, BigDecimal orderPrice, String nickName, Long orderItemId, String dishName, Integer quantity) {
        this.orderId = orderId;
        this.count = count;
        this.orderPrice = orderPrice;
        this.nickName = nickName;
        this.orderItemId = orderItemId;
        this.dishName = dishName;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
