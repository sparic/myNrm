package com.myee.niuroumian.domain;

/**
 * Created by Jelynn on 2016/6/2.
 */
public enum ErrorCode {

    SHOPNOTONLINE(601,"商户不在线");

    /**
     * 状态编号
     */
    private int value;

    private String caption;

    public int getValue() {
        return value;
    }

    public String getCaption(){
        return caption;
    }

    private ErrorCode(int value, String caption){
        this.caption = caption;
        this.value = value;
    }

}
