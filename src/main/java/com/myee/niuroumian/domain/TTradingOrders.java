/*******************************************************************
 * Copyright (c) 2015 hotrain and others
 * All rights reserved.
 *
 * Contributors:
 * hotrain Systems (Shanghai) Co., Ltd.
 * 
 ******************************************************************/
package com.myee.niuroumian.domain;

/**
 * TODO (description of the Class)
 *
 *
 * 
 */
public class TTradingOrders {
	/**
	 * 主键唯一
	 * 
	 * @pdOid 480d7635-a468-426c-a98e-606442461520
	 */
	public int tradingOrdersId;
	/**
	 * 微信用户标示
	 */
	public String openId;
	/**
	 * 预支付交易会话标识
	 * 
	 * @pdOid 057d9e1d-1889-46f0-ad34-67066d70a3b3
	 */
	public String prepayId;
	/**
	 * 交易状态：1=支付成功;2=支付失败
	 * 
	 * @pdOid 93a39711-8c54-4cb7-9331-6092244068d7
	 */
	public byte tradingStatus;

	/**
	 * 返回消息
	 */
	public String respMsg;

	/**
	 * 商户号
	 */
	public String merchantNo;

	/**
	 * 微信支付订单号
	 */
	private String transactionOrder;

	/**
	 * 交易结果0：未处理 1：支付 2：撤销 3：退货 4：未明 5：失败（待确认）
	 */
	public String orderStatus;
	/**
	 * 订单号
	 * 
	 * @pdOid b24ec1d9-9286-451b-869d-5bfad3b57f03
	 */
	public String ordersNo;

	/**
	 * 支付金额
	 */
	public String payAmount;
	/**
	 * 签名
	 */
	public String signData;

	/**
	 * 时间(可用时间格式的字符串) 支付时间，不可为空例如： "20150130164103"
	 */
	public String payTime;
	/**
	 * 创建时间
	 * 
	 * @pdOid cb1fedc7-19f9-4f15-97eb-de2b9b4eaff1
	 */
	public java.util.Date createTime;
	/**
	 * 创建人ddd
	 * 
	 * @pdOid 4fa3bda0-72e6-4bfd-a701-e616affd2b45
	 */
	public String createUser;
	/**
	 * 更新时间
	 * 
	 * @pdOid 13e733a5-8611-49f3-aca4-e2264caca615
	 */
	public java.util.Date updateTime;
	/**
	 * 更新人
	 * 
	 * @pdOid f6bc4f6c-fc52-4fd9-9dff-1d9f3d6e49ab
	 */
	public String updateUser;

	public int getTradingOrdersId() {
		return tradingOrdersId;
	}

	public void setTradingOrdersId(int tradingOrdersId) {
		this.tradingOrdersId = tradingOrdersId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public byte getTradingStatus() {
		return tradingStatus;
	}

	public void setTradingStatus(byte tradingStatus) {
		this.tradingStatus = tradingStatus;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTransactionOrder() {
		return transactionOrder;
	}

	public void setTransactionOrder(String transactionOrder) {
		this.transactionOrder = transactionOrder;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOrdersNo() {
		return ordersNo;
	}

	public void setOrdersNo(String ordersNo) {
		this.ordersNo = ordersNo;
	}
}
