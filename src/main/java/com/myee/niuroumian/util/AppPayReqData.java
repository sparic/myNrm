package com.myee.niuroumian.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class AppPayReqData {

    /**
     * 公众账号ID
     */
    private String appid            = "wxe67244505b4041b6"; //支付

    /**
     * 商户号
     */
    private String mch_id           = "1295359601";        //支付

    /**
     * 设备号
     */
    private String device_info;

    /**
     * 随机字符串
     */
    private String nonce_str        = "";

    /**
     * 签名
     */
    private String sign             = "";

    /**
     * 商品描述
     */
    private String body             = "";

    /**
     * 商户订单号
     */
    private String out_trade_no     = "";

    /**
     * 总金额
     */
    private int    total_fee        = 0;

    /**
     * 终端IP
     */
    private String spbill_create_ip = "";

    /**
     * 回调url
     */
    private String notify_url       = "";

    /**
     * 交易类型
     */
    private String trade_type;

    /**
     * 商家数据包
     */
    private String attach;

    /**
     * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
     */
    private String openid;

    private String ordersNo = "";//商品订单号

    private String goods_tag = "";//订单类型(商品标记)


    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goodsTag) {
        goods_tag = goodsTag;
    }

    public String getOrdersNo() {
        return ordersNo;
    }

    public void setOrdersNo(String ordersNo) {
        this.ordersNo = ordersNo;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<String,Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
