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
     * @param ms 消耗的毫秒数 比如 endTime - startTime
     * @return 将毫秒数换算成消耗的时间 如 : 90061001 换算成 1day 1hour 1min 1second 1ms
     */
    public static String costTimeByMs(long ms) {
        final int s = 1000;
        final int min = s * 60;
        final int h = min * 60;
        final int d = h * 24;
        long day = ms / d;
        long hour = (ms - day * d) / h;
        long minute = (ms - day * d - hour * h) / min;
        long second = (ms - day * d - hour * h - minute * min) / s;
        long milliSecond = ms - day * d - hour * h - minute * min - second * s;
        StringBuilder result = new StringBuilder();
        if (day != 0) {
            result.append(day).append("day ");
        }
        if (hour != 0) {
            result.append(hour).append("hour ");
        }
        if (minute != 0) {
            result.append(minute).append("min ");
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
