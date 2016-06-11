package com.myee.niuroumian.service;

/**
 * Created by Martin on 2016/1/20.
 */
public final class RedisKeys {
    private static final String KEY_STORE_OF_CLIENT    = "shopOfClient:";
    private static final String KEY_WAIT_OF_TABLE_TYPE = "waitOfTableType:";
    private static final String KEY_DRAW_OF_STORE      = "drawOfStore:";
    private static final String KEY_OPENID_TO_TABLE_TYPE      = "openIdToTableType:";

    private static final String REPAST_NO      = "currentRepastNO_";
//    private static final String KEY_IDENTITY_CODE      = "identityCode:";
//    private static final String WX_OPENID_USERINFO      = "wxUserInfo:";

    public static String shopOfClient(Long clientId) {
        return KEY_STORE_OF_CLIENT + clientId;
    }

    public static String waitOfTableType(Long orgId,long tableTypeId) {
        return KEY_WAIT_OF_TABLE_TYPE + orgId + "-" + tableTypeId;
    }

    public static String drawOfStore(long storeId) {
        return KEY_DRAW_OF_STORE + storeId;
    }

    public static String openIdToTableType(Long orgID,String openId) {
        return KEY_OPENID_TO_TABLE_TYPE + orgID + "-" + openId;
    }

//    public static String getIdentityCode(Long shopId) {
//        return KEY_IDENTITY_CODE + shopId;
//    }
//
//    public static String wxUserInfo(String openId) {
//        return WX_OPENID_USERINFO + openId;
//    }

    public static String getCurrentRepastNO(Long shopId){
        return  REPAST_NO+shopId;
    }
}
