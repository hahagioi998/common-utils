package utils;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by EalenXie on 2019/7/23 22:06.
 */
public class JsonConvertTest {

    @Test
    public void jsonFormat() {
        String json = "{\n" + "  \"name\": null\n" + "}";
        HashMap hashMap = JSONObject.parseObject(json, HashMap.class);
        System.out.println(hashMap);

        //会抛异常  因为ConcurrentHashMap的value不能为空
        ConcurrentHashMap concurrentHashMap = JSONObject.parseObject(json, ConcurrentHashMap.class);
        System.out.println(concurrentHashMap);

    }
}
