package com.myee.niuroumian.domain;

/**
 * Created by Jelynn on 2016/6/2.
 */
public enum  OrderState {

    WAITING(1,"等位"), REPASTING(2,"就餐"), SKIP(3,"过号"), CANCEL(4,"取消");

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

    private OrderState(int value, String caption){
        this.caption = caption;
        this.value = value;
    }

}
