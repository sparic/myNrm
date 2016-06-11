package com.myee.niuroumian.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 订单实体
 * Created by Jelynn on 2016/6/1.
 */
@Entity
@Table(name = "r_order_info")
public class
        OrderInfo implements Serializable {

    @Id
    @Column(name = "order_id")
    @TableGenerator(name = "PkGen_120", table = "ad_sequence", pkColumnName = "name", pkColumnValue = "R_order_info", valueColumnName = "currentnextsys", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PkGen_120")
    private Long orderId; //订单ID

    @Column(name = "shop_id")
    private Long shopId;   //商铺ID

    // mappedBy="order": 指明Order类为双向关系维护端，负责外键的更新
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderInfo")
    @OneToMany(targetEntity = OrderItemInfo.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="order_id")
    private Set<OrderItemInfo> items = new HashSet<OrderItemInfo>();

//    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, targetEntity = UserInfo.class)
//    @JoinColumn(name = "user_id")//用户ID
//    private UserInfo userInfo;

    @Column(name = "user_id")
    private String userId;    //用户ID

    @Column(name = "count")
    private int count; //商品数量

    @Column(name = "create_time")
    private Timestamp createTime;   //创建时间

    @Column(name = "update_time")
    private Timestamp updateTime; //更新时间

    @Column(name = "order_state")
    private int orderState; //订单状态 1,"等位", 2,"就餐", 3,"过号", 4,"取消";

    @Column(name = "order_type")
    private int orderType; //订单类型.  6.线上点单  7.线下点单

    @Column(name = "order_price")
    private BigDecimal orderPrice;  //订单金额

    @Column(name = "pay_state")
    private int payState;  //支付状态  SUCCESS(1,"已支付"), FAILED(2,"未支付");

    @Column(name = "pay_time")
    private Timestamp payTime;  //支付状态  SUCCESS(1,"已支付"), FAILED(2,"未支付");

    @Column(name = "remark")
    private String remark;

    @Column(name="wait_no")
    private int waitNo; //等位号

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<OrderItemInfo> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemInfo> items) {
        this.items = items;
    }

    public int getWaitNo() {
        return waitNo;
    }

    public void setWaitNo(int waitNo) {
        this.waitNo = waitNo;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId=" + orderId +
                ", shopId=" + shopId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderState=" + orderState +
                ", orderType=" + orderType +
                ", orderPrice=" + orderPrice +
                ", payState=" + payState +
                ", waitNo=" + waitNo +
                ", payTime=" + payTime +

                '}';
    }

    ////////////////////////////////////
    /**
     * 添加订单项
     *
     */
    public void addOrderItem(OrderItemInfo item) {
        if (!this.items.contains(item)) {
            this.items.add(item);
//            item.setOrderInfo(this);
        }
    }

    /**
     * 删除订单项
     *
     */
    public void removeOrderItem(OrderItemInfo item) {
        if (this.items.contains(item)) {
//            item.setOrderInfo(null);
            this.items.remove(item);
        }
    }
}
