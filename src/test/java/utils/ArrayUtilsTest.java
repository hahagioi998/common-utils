package utils;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by EalenXie on 2019/7/23 22:14.
 */
public class ArrayUtilsTest {


    @Test
    public void concat() {
        String[] arr1 = {"111", "222"};
        String[] arr2 = {"2ss", "1SS"};
        String[] arr3 = ArrayUtils.concat(arr1, arr2);
        System.out.println(Arrays.toString(arr3));
    }

    @Test
    public void add() {
        Object[] arr1 = {"ss", "SSSS"};
        ArrayUtils.addByValue(arr1, "kkkk");
        System.out.println(Arrays.toString(arr1));
    }

    @Test
    public void intersection() {
        String[] arr1 = {"111", "222", "333"};
        String[] arr2 = {"222", "1SS"};
        String[] result = ArrayUtils.intersection(arr1, arr2);
        System.out.println(Arrays.toString(result));
    }


}
