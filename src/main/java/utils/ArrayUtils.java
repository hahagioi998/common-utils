package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by EalenXie on 2019/6/10 13:37.
 */
public class ArrayUtils {

    //删除数组中其中一个元素
    public static Object[] removeByValue(Object[] array, Object value) {
        List<Object> list = new ArrayList<>(Arrays.asList(array));
        list.remove(value);
        return list.toArray(new Object[1]);
    }

    //在数组中增加一个元素
    public static Object[] addByValue(Object[] array, Object value) {
        List<Object> list = new ArrayList<>(Arrays.asList(array));
        list.add(value);
        return list.toArray(new Object[1]);
    }
}
