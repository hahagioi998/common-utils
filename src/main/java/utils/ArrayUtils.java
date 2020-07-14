package utils;

import java.util.*;

/**
 * Created by EalenXie on 2019/6/10 13:37.
 */
public class ArrayUtils {
    private ArrayUtils() {
    }

    /**
     * 删除数组中其中一个元素
     *
     * @param array 目标数组
     * @param value 目标元素
     * @return 删除目标元素后的数组
     */
    public static Object[] removeByValue(Object[] array, Object value) {
        List<Object> list = new ArrayList<>(Arrays.asList(array));
        list.remove(value);
        return list.toArray(new Object[1]);
    }

    /**
     * 在数组中增加一个元素
     *
     * @param array 目标数组
     * @param value 目标元素
     * @return 增加目标元素后的数组
     */
    public static Object[] addByValue(Object[] array, Object value) {
        List<Object> list = new ArrayList<>(Arrays.asList(array));
        list.add(value);
        return list.toArray(new Object[1]);
    }

    /**
     * 数组 合并
     */
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    /**
     * 获取目标数组的不同之处
     */
    public static String[] getArrayTargetDifference(String[] target, String[] contains) {
        if (target.length == 0) return target;
        if (contains.length == 0) return contains;
        List<String> listOne = new ArrayList<>(Arrays.asList(target));
        List<String> listTwo = new ArrayList<>(Arrays.asList(contains));
        List<String> contain = new ArrayList<>(listOne);
        contain.retainAll(listTwo);
        listOne.removeAll(contain);
        return listOne.toArray(new String[1]);
    }

    /**
     * 获取目标数组的相同之处 求交集
     */
    public static String[] intersection(String[] target, String[] contains) {
        HashSet<String> set1 = new HashSet<>(Arrays.asList(target));
        HashSet<String> set2 = new HashSet<>(Arrays.asList(contains));
        set1.retainAll(set2);
        String[] output = new String[set1.size()];
        int idx = 0;
        for (String s : set1) output[idx++] = s;
        return output;
    }


    /**
     * 为null 或 为空
     */
    public static boolean nullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 不为null 且 不为空
     */
    public static boolean notNullAndEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 不为null 且 不为空
     */
    public static boolean notNullAndEmpty(Collection<?>... collections) {
        for (Collection<?> c : collections) {
            if (nullOrEmpty(c)) return false;
        }
        return true;
    }

}
