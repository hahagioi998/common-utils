package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * @author EalenXie create on 2020/7/8 10:34
 */
public class JacksonConvert {


    private static ObjectMapper mapper;


    static {
        mapper = new ObjectMapper();

        // 转换为格式化的json
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJsonString(Object target) throws JsonProcessingException {
        return mapper.writeValueAsString(target);
    }

    public static <T> T parseObject(String json, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(json, type);
    }


}
