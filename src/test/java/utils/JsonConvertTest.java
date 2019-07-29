package utils;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import utils.model.AssemblerModel;

import java.util.HashMap;
import java.util.List;
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

    @Test
    public void toJSONString() {
        AssemblerModel model = new AssemblerModel();
        model.setTelephone("12323221");
        model.setAge(22);
        model.setEmail("ealenxie@qq.com");
        model.setGender("男");
        model.setName("ealenxie");
        model.setPassword("admin");
        System.out.println(JsonConvert.toJSONString(model));
    }

    @Test
    public void toJavaObject() {

        String json = "{\n" +
                "    \"age\": 22,\n" +
                "    \"email\": \"ealenxie@qq.com\",\n" +
                "    \"gender\": \"男\",\n" +
                "    \"name\": \"ealenxie\",\n" +
                "    \"password\": \"admin\",\n" +
                "    \"telephone\": \"12323221\"\n" +
                "  }";
        AssemblerModel model = JsonConvert.toJavaObject(json, AssemblerModel.class);
        System.out.println(model);
        String jsonList = "[\n" + "{\n" + "\"age\": 22,\n" + "\"email\": \"ealenxie@qq.com\",\n" + "\"gender\": \"男\",\n" +
                "    \"name\": \"ealenxie\",\n" + "\"password\": \"admin\",\n" +
                "    \"telephone\": \"12323221\"\n" + "},\n" + "{\n" +
                "    \"age\": 24,\n" + "    \"email\": \"admin@qq.com\",\n" +
                "    \"gender\": \"男\",\n" + "\"name\": \"admin\",\n" +
                "    \"password\": \"12233\",\n" + "\"telephone\": \"12323221\"\n" + "  }\n" + "]";
        List<AssemblerModel> modelList = JsonConvert.toJavaList(jsonList, AssemblerModel.class);
        for (AssemblerModel assemblerModel : modelList) {
            System.out.println(assemblerModel);
        }
    }
}
