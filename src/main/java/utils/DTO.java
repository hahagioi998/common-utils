package utils;

/**
 * @author EalenXie create on 2020/8/5 16:41
 */
public interface DTO extends Converter {


    default <R> R convert(Class<R> type) {
        return DataConvert.mapping(this, type);
    }

    default <R> void convertNotNull(R result) {
        DataConvert.mappingNotNull(this, result);
    }


    @Override
    default <S, R> R convert(S source, Class<R> type) {
        return DataConvert.mapping(source, type);
    }

    @Override
    default <S, R> void convert(S source, R result) {
        DataConvert.mapping(source, result);
    }


}
