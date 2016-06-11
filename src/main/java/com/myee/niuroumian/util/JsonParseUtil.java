package com.myee.niuroumian.util;


import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description json格式数据转换工具类
 */
public class JsonParseUtil {

    /**
     *
     * @description 将对象转换成json格式的字符串

     */
    public static String objectToJson(Object obj) {
        JSONObject jsonStr = JSONObject.fromObject(obj);
        return jsonStr.toString();
    }

    /**
     *
     * @description 将json格式的字符串转换成具体对象
     */
    public static Object jsonToObject(String json, Class<?> c) {
        JSONObject jsonObj = JSONObject.fromObject(json);
        return JSONObject.toBean(jsonObj, c);
    }

    /**
     *
     * @description 将数组转换成json格式数据
     */
    public static String listToJson(List<?> list) {
        JSONArray array = JSONArray.fromObject(list);
        String jsonStr = array.toString();
        return jsonStr;
    }

    /**
     *
     * @description 将json格式转换成数组
     */
    public static List<?> jsonToList(String json, Class<?> c) {
        JSONArray array = JSONArray.fromObject(json);
        List<?> list = (List<?>) JSONArray.toCollection(array, c);
        return list;
    }
}
