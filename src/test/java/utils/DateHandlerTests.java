package utils;


import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by EalenXie on 2019/6/5 15:18.
 */
public class DateHandlerTests {


    @Test
    public void format() {
        System.out.println(DateHandler.formatDate(new Date()));
        System.out.println(DateHandler.formatDateTime(new Date()));
        System.out.println(DateHandler.format(new Date(), "yyyy"));
        System.out.println(DateHandler.format(new Date(), "yyyyMMdd"));
        System.out.println(DateHandler.format(new Date(), "yyyy~MM~dd HHmmss"));
        System.out.println(DateHandler.format(new Date(), "yyyyMMdd HHmmss SSS"));
        System.out.println(DateHandler.format(new Date(), "yyyyMMdd hhmmss"));
        System.out.println(DateHandler.format(new Date(), "yyyyMMdd hhmmssSSS"));
    }

    @Test
    public void byCurrentDay() {
        System.out.println("日期是否是今天 : " + DateHandler.isCurrentDate(DateHandler.anyDateByCurrentDay(Calendar.DATE, 0)));
        System.out.println("日期是否是今天 : " + DateHandler.isCurrentDate(DateHandler.anyDateByCurrentDay(Calendar.DATE, -1)));
        System.out.println("日期是否是今天 : " + DateHandler.isCurrentDate(DateHandler.anyDateByCurrentDay(Calendar.DATE, 1)));
        System.out.println("昨天 : " + DateHandler.formatDateTime(DateHandler.anyDateByCurrentDay(Calendar.DATE, -1)));
        System.out.println("今天 : " + DateHandler.formatDateTime(DateHandler.anyDateByCurrentDay(Calendar.DATE, 0)));
        System.out.println("明天 : " + DateHandler.formatDateTime(DateHandler.anyDateByCurrentDay(Calendar.DATE, 1)));
        System.out.println("后天 : " + DateHandler.formatDateTime(DateHandler.anyDateByCurrentDay(Calendar.DATE, 2)));
        System.out.println("上个月 : " + DateHandler.anyTimeByCurrentDay(Calendar.MONTH, -1));
        System.out.println("下个月 : " + DateHandler.anyTimeByCurrentDay(Calendar.MONTH, 1));
        System.out.println("一周前 : " + DateHandler.anyTimeByCurrentDay(Calendar.WEEK_OF_MONTH, -1));
        System.out.println("一周后 : " + DateHandler.anyTimeByCurrentDay(Calendar.WEEK_OF_MONTH, 1));
        System.out.println("两周后 : " + DateHandler.anyTimeByCurrentDay(Calendar.WEEK_OF_MONTH, 2));
    }

    @Test
    public void cost() {
        System.out.println(DateHandler.format(new Date(), "yyMM"));
    }

    @Test
    public void costMs() {
        System.out.println(DateHandler.costTimeByMs(System.currentTimeMillis()));
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(3669);
            //do nothing
        } catch (InterruptedException e) {
            //ig
        } finally {

            long endTime = System.currentTimeMillis();
            System.out.println(DateHandler.costTimeByMs(startTime, endTime));
        }

    }

}
