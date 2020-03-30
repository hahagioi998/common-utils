package utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Created by EalenXie on 2019/6/5 10:44.
 * 数据转换类
 * 依赖于 BeanUtils(基于setter方法映射)  , cglib BeanCopier(类型属性名全相同映射)
 */
public class DataConvert {
    private DataConvert() {
    }

    private static final Logger log = LoggerFactory.getLogger(DataConvert.class);

    /**
     * 新增 BeanCopier 池 ,将生成的BeanCopier实例缓存 应对多种Bean之间的转换 提升效率
     */
    private static Map<String, BeanCopier> beanCopierMap = new HashMap<>();

    /**
     * 将 源对象属性值 映射成 目标对象
     *
     * @param source 源对象
     * @param clazz  目标对象类型
     */
    public static <T> T mapping(Object source, Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        mapping(source, t);
        return t;
    }

    /**
     * 将 源对象属性值 映射到 目标对象属性值
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void mapping(Object source, Object target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            BeanUtils.copyProperties(source, target);
        } else {
            log.warn("the source or target is null , mapping will do nothing ! ");
        }
    }

    /**
     * 生成BeanCopier的缓存key
     *
     * @param source 源对象
     * @param target 目标对象
     */
    private static String genCopierKey(Class<?> source, Class<?> target) {
        return source.toString() + target.toString();
    }

    /**
     * 将 源对象属性值 映射到 目标对象属性值 BeanCopier 实现 据说大数据量时效率6倍高于BeanUtils
     * 注意 : BeanCopier 源对象与目标对象属性名称、类型都相同时才进行拷贝
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void beanCopierMapping(Object source, Object target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            String beanKey = genCopierKey(source.getClass(), target.getClass());
            BeanCopier copier;
            if (!beanCopierMap.containsKey(beanKey)) {
                copier = BeanCopier.create(source.getClass(), target.getClass(), false);
                beanCopierMap.put(beanKey, copier);
            } else {
                copier = beanCopierMap.get(beanKey);
            }
            copier.copy(source, target, null);
        } else {
            log.warn("the source or target is null , mapping will do nothing ! ");
        }
    }

    /**
     * 映射(不为null的属性值才会映射) 将给定源bean的属性值(不为null)覆盖到给定目标bean中,只要属性匹配
     *
     * @param sourceBean 源bean
     * @param targetBean 目标bean
     */
    public static void mappingNotNull(Object sourceBean, Object targetBean) {
        if (Objects.nonNull(sourceBean) && Objects.nonNull(targetBean)) {
            BeanWrapper wrapper = new BeanWrapperImpl(sourceBean);
            PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
            Set<String> properties = new HashSet<>();
            for (PropertyDescriptor propertyDescriptor : pds) {
                String propertyName = propertyDescriptor.getName();
                Object propertyValue = wrapper.getPropertyValue(propertyName);
                if (StringUtils.isEmpty(propertyValue)) {
                    wrapper.setPropertyValue(propertyName, propertyValue);
                    properties.add(propertyName);
                }
            }
            BeanUtils.copyProperties(sourceBean, targetBean, properties.toArray(new String[0]));
        } else {
            log.warn("the source or target is null , mappingNotNull will do nothing");
        }
    }

    /**
     * 多个对象进行合并和聚合操作
     *
     * @param source 要进行 合并和聚合的对象
     * @param clazz  聚合结果类(对象)
     * @return 返回 聚合对象
     */
    public static <T> T assembler(Object[] source, Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        assembler(source, t);
        return t;
    }

    /**
     * 多个对象进行合并和聚合操作
     *
     * @param source 要进行 合并和聚合的对象
     * @param target 聚合结果对象
     */
    public static <T> void assembler(Object[] source, T target) {
        if (Objects.nonNull(source)) {
            for (Object s : source) {
                DataConvert.mappingNotNull(s, target);
            }
        } else {
            log.warn("the source or target is null , assembler will do nothing ! ");
        }
    }


}
