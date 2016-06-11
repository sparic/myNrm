package com.myee.niuroumian.domain;

/**
 * Created by Jelynn on 2016/6/2.
 */
public enum PayState {

    SUCCESS(1,"已支付"), FAILED(2,"未支付");

    /**
     * 状态编号
     */
    private int value;

    /**
     * 操作名
     */
    private String caption;

    public int getValue() {
        return value;
    }

    public String getCaption(){
        return caption;
    }

    private PayState(int value, String caption){
        this.caption = caption;
        this.value = value;
    }
}
