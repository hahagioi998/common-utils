package utils;


/**
 * @author EalenXie create on 2020/8/5 15:48
 * Everything in the world will eventually Convert
 * 世间万物皆转换
 */
@FunctionalInterface
public interface Converter {

    /**
     * Everything convert
     *
     * @param source convert source 转换源
     * @param type   result type (结果类型)
     * @return result 转换结果
     */
    <S, R> R convert(S source, Class<R> type);

    /**
     * Everything convert
     *
     * @param source convert source 转换源
     * @param result convert result 结果对象
     */
    default <S, R> void convert(S source, R result) {
    }
}
