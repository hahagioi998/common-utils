package utils;

import org.junit.Test;
import utils.model.TargetModel;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by EalenXie on 2019/7/22 13:38.
 */
public class MapBeanConvertTest {


    @Test
    public void beanToMap() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
        TargetModel dto = new TargetModel();
        dto.setName("6220000863");
        //默认不忽略null，默认不处理驼峰,默认类型为HashMap
        Map map1 = MapBeanConvert.beanToMap(dto);
        System.out.println("map1 : " + map1 + "  -> type : " + map1.getClass());
        //忽略null，不处理驼峰
        Map map2 = MapBeanConvert.beanToMap(true, dto);
        System.out.println("map2 : " + map2 + "  -> type : " + map2.getClass());
        //指定Map的类型
        Map map3 = MapBeanConvert.beanToMap(dto, HashMap.class);
        System.out.println("map3 : " + map3 + "  -> type : " + map3.getClass());
        //处理驼峰
        Map map4 = MapBeanConvert.beanToMap(dto, true);
        System.out.println("map4 : " + map4 + "  -> type : " + map4.getClass());
        //忽略null,指定Map类型
        Map map5 = MapBeanConvert.beanToMap(true, dto, TreeMap.class);
        System.out.println("map5 : " + map5 + "  -> type : " + map5.getClass());
        //指定Map类型,指定处理驼峰
        Map map6 = MapBeanConvert.beanToMap(dto, HashMap.class, true);
        System.out.println("map6 : " + map6 + "  -> type : " + map6.getClass());
        //均可指定
        Map map7 = MapBeanConvert.beanToMap(true, dto, Hashtable.class, true);
        System.out.println("map7 : " + map7 + "  -> type : " + map7.getClass());
    }

    @Test
    public void mapToBean() throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        map.put("dh", "6220000863");
        TargetModel dto1 = MapBeanConvert.mapToBean(map, TargetModel.class);
        System.out.println(dto1);

        map.put("tx_code", "sssss");
        TargetModel dto2 = MapBeanConvert.mapToBean(map, TargetModel.class, true);  //识别下划线 ,转成驼峰
        System.out.println(dto2);

        map.put("txCode", "xxxxxx");
        TargetModel dto3 = MapBeanConvert.mapToBean(map, TargetModel.class, false);
        System.out.println(dto3);

        map.put("source_track_number","100");
        TargetModel dto4 = MapBeanConvert.mapToBean(map, TargetModel.class, false);
        System.out.println(dto4);

        map.put("source_track_number","100");
        TargetModel dto5 = MapBeanConvert.mapToBean(map, TargetModel.class, true);
        System.out.println(dto5);
    }



    @Test
    public void underlineToCamel() {
        String s1 = "user_id";
        System.out.println(s1 + " -> " + MapBeanConvert.underlineToCamel(s1));
        String s2 = "_user_id";
        System.out.println(s2 + " -> " + MapBeanConvert.underlineToCamel(s2));
        String s3 = "__user__id";
        System.out.println(s3 + " -> " + MapBeanConvert.underlineToCamel(s3));
        String s4 = "user__id";
        System.out.println(s4 + " -> " + MapBeanConvert.underlineToCamel(s4));
        String s5 = "source_track_number";
        System.out.println(s5 + " -> " + MapBeanConvert.underlineToCamel(s5));
    }

    @Test
    public void camelToUnderline() {
        String s1 = "userId";
        System.out.println(s1 + " -> " + MapBeanConvert.camelToUnderline(s1));
        String s2 = "UserId";
        System.out.println(s2 + " -> " + MapBeanConvert.camelToUnderline(s2));
        String s3 = "User_Id";
        System.out.println(s3 + " -> " + MapBeanConvert.camelToUnderline(s3));
        String s4 = "source_track_Number";
        System.out.println(s4 + " -> " + MapBeanConvert.camelToUnderline(s4));
    }
}
