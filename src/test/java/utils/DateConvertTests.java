package utils;


import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by EalenXie on 2019/6/5 15:18.
 */
public class DateConvertTests {


    @Test
    public void format() {
        System.out.println(DateConvert.formatDate(new Date()));
        System.out.println(DateConvert.formatDateTime(new Date()));
        System.out.println(DateConvert.format(new Date(), "yyyy"));
        System.out.println(DateConvert.format(new Date(), "yyyyMMdd"));
        System.out.println(DateConvert.format(new Date(), "yyyy~MM~dd HHmmss"));
        System.out.println(DateConvert.format(new Date(), "yyyyMMdd HHmmss SSS"));
        System.out.println(DateConvert.format(new Date(), "yyyyMMdd hhmmss"));
        System.out.println(DateConvert.format(new Date(), "yyyyMMdd hhmmssSSS"));
    }

    @Test
    public void byCurrentDay() {


        System.out.println("5分钟后 : " + DateConvert.anyTimeByCurrentDay(Calendar.MINUTE, 5));


        System.out.println("日期是否是今天 : " + DateConvert.isCurrentDate(DateConvert.anyDateByCurrentDay(Calendar.DATE, 0)));
        System.out.println("日期是否是今天 : " + DateConvert.isCurrentDate(DateConvert.anyDateByCurrentDay(Calendar.DATE, -1)));
        System.out.println("日期是否是今天 : " + DateConvert.isCurrentDate(DateConvert.anyDateByCurrentDay(Calendar.DATE, 1)));
        System.out.println("昨天 : " + DateConvert.formatDateTime(DateConvert.anyDateByCurrentDay(Calendar.DATE, -1)));
        System.out.println("今天 : " + DateConvert.formatDateTime(DateConvert.anyDateByCurrentDay(Calendar.DATE, 0)));
        System.out.println("明天 : " + DateConvert.formatDateTime(DateConvert.anyDateByCurrentDay(Calendar.DATE, 1)));
        System.out.println("后天 : " + DateConvert.formatDateTime(DateConvert.anyDateByCurrentDay(Calendar.DATE, 2)));
        System.out.println("上个月 : " + DateConvert.anyTimeByCurrentDay(Calendar.MONTH, -1));
        System.out.println("下个月 : " + DateConvert.anyTimeByCurrentDay(Calendar.MONTH, 1));
        System.out.println("一周前 : " + DateConvert.anyTimeByCurrentDay(Calendar.WEEK_OF_MONTH, -1));
        System.out.println("一周后 : " + DateConvert.anyTimeByCurrentDay(Calendar.WEEK_OF_MONTH, 1));
        System.out.println("两周后 : " + DateConvert.anyTimeByCurrentDay(Calendar.WEEK_OF_MONTH, 2));
    }

    @Test
    public void cost() {
        System.out.println(DateConvert.format(new Date(), "yyMM"));
    }

    @Test
    public void costMs() {
        System.out.println(DateConvert.costTimeByMs(System.currentTimeMillis()));
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(3669);
            //do nothing
        } catch (InterruptedException e) {
            //ig
        } finally {

            long endTime = System.currentTimeMillis();
            System.out.println(DateConvert.costTimeByMs(startTime, endTime));
        }

    }


    @Test
    public void getEndOfDay() {

//        Date endOfDay = DateConvert.getEndOfDay(new Date());

//        System.out.println(DateConvert.format(endOfDay,"yyyy-MM-dd HH:mm:ss:SSS"));


        System.out.println(DateConvert.format(new Date(0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateConvert.format(new Date(11122813988980L),"yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void getStartOfDate() {
        Date endOfDay = DateConvert.getStartOfDate(new Date());

        System.out.println(DateConvert.format(endOfDay,"yyyy-MM-dd HH:mm:ss:SSS"));

    }

}
