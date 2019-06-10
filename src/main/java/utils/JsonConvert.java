package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by EalenXie on 2019/6/6 13:50.
 * 依赖FastJson
 */
public class JsonConvert {


    public static <T> T toJavaObject(String result, Class<T> clazz) {
        return JSONObject.toJavaObject(JSONObject.parseObject(result), clazz);
    }

    public static <T> List<T> toJavaList(String resultList, Class<T> clazz) {
        return JSONArray.parseArray(resultList).toJavaList(clazz);
    }



}
