package com.myee.niuroumian.util;

public class AjaxResultObj {
	public static final int CODE_SUCCESS = 0;
	public static final int CODE_FAILED = 1;
	private int code;
	private String message;
	private Object data;
//	private String deviceId;
//	private String deviceName;

//	public AjaxResultObj(int code, Object data, String message, String deviceId, String deviceName) {
//		this.code = code;
//		this.message = message;
//		this.data = data;
//		this.deviceId = deviceId;
//		this.deviceName = deviceName;
//	}


	public AjaxResultObj() {
	}

	public AjaxResultObj(int code, Object data, String message) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public int getCode(){return code;}

	public boolean isSuccess() {
		return CODE_SUCCESS == code;
	}

	public static int getCodeSuccess() {
		return CODE_SUCCESS;
	}

//	public String getDeviceName() {
//		return deviceName;
//	}
//
//	public String getDeviceId() {
//		return deviceId;
//	}

	public static final AjaxResultObj success() {
		return new AjaxResultObj(CODE_SUCCESS, null, null);
	}

	public static final AjaxResultObj success(Object data) {
		return new AjaxResultObj(CODE_SUCCESS, data, null);
	}

	public static final AjaxResultObj success(Object data, String message) {
		return new AjaxResultObj(CODE_SUCCESS, data, message);
	}

	public static final AjaxResultObj failed() {
		return new AjaxResultObj(CODE_FAILED, null, null);
	}

	public static final AjaxResultObj failed(String message) {
		return new AjaxResultObj(CODE_FAILED, null, message);
	}

	public static final AjaxResultObj failed(Object data, String message) {
		return new AjaxResultObj(CODE_FAILED, data, message);
	}

	public static final AjaxResultObj failed(int code, String message) {
		return new AjaxResultObj(CODE_FAILED, code, message);
	}

	public static final AjaxResultObj failed(Object data) {
		return new AjaxResultObj(CODE_FAILED, data, "");
	}
	
}
