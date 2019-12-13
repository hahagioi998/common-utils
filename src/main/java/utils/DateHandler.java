package utils;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by EalenXie on 2019/6/5 10:42.
 * 日期处理类
 */
public enum DateHandler {

    ;

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    //秒
    private static final int S = 1000;
    //分
    private static final int MIN = S * 60;
    //时
    private static final int H = MIN * 60;
    //天
    private static final int D = H * 24;

    /**
     * 默认 字符串日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static Date ofDate(String date) {
        return ofDate(date, DEFAULT_ZONE_ID, DEFAULT_DATE_TIME_FORMATTER);
    }


    /**
     * @param date   日期
     * @param format 日期格式 (至少要精确到天)
     */
    public static Date ofDate(String date, String format) {
        return ofDate(date, DEFAULT_ZONE_ID, DateTimeFormatter.ofPattern(format));
    }

    public static Date ofDate(String date, ZoneId zoneId, DateTimeFormatter formatter) {
        return Date.from(LocalDate.parse(date, formatter).atStartOfDay().atZone(zoneId).toInstant());
    }

    public static String formatDate(@NonNull Date date) {
        return format(date, DEFAULT_ZONE_ID, DEFAULT_DATE_FORMATTER);
    }

    public static String formatDateTime(@NonNull Date date) {
        return format(date, DEFAULT_ZONE_ID, DEFAULT_DATE_TIME_FORMATTER);
    }

    /**
     * 日期类型按自定义格式进行字符串化
     */
    public static String format(@NonNull Date date, @NonNull String format) {
        return format(date, DEFAULT_ZONE_ID, DateTimeFormatter.ofPattern(format));
    }

    public static String format(@NonNull Date date, ZoneId zoneId, DateTimeFormatter formatter) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId).format(formatter);
    }

    /**
     * 日期是否是今天
     */
    public static boolean isCurrentDate(@NonNull Date date) {
        return date.after(Date.from(LocalDate.now().atStartOfDay(DEFAULT_ZONE_ID).toInstant())) &&
                date.before(Date.from(LocalDate.now().plusDays(1).atStartOfDay(DEFAULT_ZONE_ID).toInstant()));
    }

    /**
     * @param calendarEnum 日期枚举 比如 Calendar.MONTH
     * @param count        任意加减天数
     * @return 获取任意时间
     */
    public static String anyTimeByCurrentDay(int calendarEnum, int count) {
        return LocalDateTime.ofInstant(getYourCalendar(new Date(), calendarEnum, count).toInstant(), DEFAULT_ZONE_ID).format(DEFAULT_DATE_TIME_FORMATTER);
    }

    /**
     * @param calendarEnum 日期枚举 比如 Calendar.MONTH
     * @param count        任意加减天数
     * @return 获取任意日期
     */
    public static Date anyDateByCurrentDay(int calendarEnum, int count) {
        return Date.from(getYourCalendar(new Date(), calendarEnum, count).toInstant());
    }

    /**
     * @param costTime 消耗的毫秒数
     * @return 将毫秒数换算成消耗的时间 如 : 90061001 换算成 1day 1hour 1min 1second 1ms
     */
    public static String costTimeByMs(long costTime) {
        long day = costTime / D;
        long hour = (costTime - day * D) / H;
        long minute = (costTime - day * D - hour * H) / MIN;
        long second = (costTime - day * D - hour * H - minute * MIN) / S;
        long milliSecond = costTime - day * D - hour * H - minute * MIN - second * S;
        StringBuilder result = new StringBuilder();
        if (day != 0) {
            //每年按平均365天算
            if (day >= 365) {
                long year = day / 365;
                result.append(year).append("year ");
                day = day - year * 365;
            }
            //每月按平均30天算
            if (day < 365 && day >= 30) {
                long month = day / 30;
                result.append(month).append("month ");
                day = day - month * 30;
            }
            if (day != 0 && day < 30) {
                result.append(day).append("day ");
            }
        }
        if (hour != 0) {
            result.append(hour).append("hour ");
        }
        if (minute != 0) {
            result.append(minute).append("MIN ");
        }
        if (second != 0) {
            result.append(second).append("second ");
        }
        if (milliSecond != 0) {
            result.append(milliSecond).append("ms ");
        }
        return result.toString();
    }

    /**
     * @param startTime 起始时间
     * @param endTime   终止时间
     * @return 将毫秒数换算成消耗的时间 如 : 90061001 换算成 1day 1hour 1min 1second 1ms
     */
    public static String costTimeByMs(long startTime, long endTime) {
        return costTimeByMs(endTime - startTime);
    }

    /**
     * @param date  日期
     * @param field 域值
     * @param value 附加值
     * @return 获取你想要的Calendar
     */
    private static Calendar getYourCalendar(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, calendar.get(field) + value);
        return calendar;
    }

}
