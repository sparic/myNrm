package com.myee.niuroumian.response;

/**
 * Created by Jelynn on 2016/4/26.
 */
public class ResponseData {
    private boolean success;
    private int errorCode;
    private Object data;
    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseData() {
        // emtpy
    }

    public ResponseData(boolean success) {
        this.success = success;
    }

    public ResponseData(boolean success, int errorCode, Object data, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
        this.message = message;
    }

    public ResponseData(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static ResponseData successData(Object data) {
        return new ResponseData(true, 0, data, null);
    }

    public static ResponseData errorData(String message) {
        return new ResponseData(false, null, message);
    }

    public static ResponseData errorData(int errorCode, String message) {
        return new ResponseData(false, errorCode, null, message);
    }
}