package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * Created by EalenXie on 2019/6/6 13:50.
 * 依赖FastJson
 */
public class JsonConvert {


    public static <T> T toJavaObject(String target, Class<T> clazz) {
        return JSONObject.parseObject(target, clazz);
    }

    public static <T> List<T> toJavaList(String target, Class<T> clazz) {
        return JSONArray.parseArray(target).toJavaList(clazz);
    }


    public static String toJSONString(Object target) {
        return JSON.toJSONString(target, SerializerFeature.WriteMapNullValue);
    }


}
