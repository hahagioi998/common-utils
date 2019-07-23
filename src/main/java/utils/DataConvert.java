package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by EalenXie on 2019/6/5 10:44.
 * 数据转换类
 * 依赖于 BeanUtils 和 ReflectionUtils
 */
public enum DataConvert {

    ;
    private static final Logger log = LoggerFactory.getLogger(DataConvert.class);

    /**
     * Data To Object
     *
     * @param source the source bean
     * @param clazz  the target bean
     */
    public static <T> T mapping(Object source, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = clazz.newInstance();
        if (Objects.nonNull(source)) BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * Data To Object
     *
     * @param source the source bean
     * @param target the target bean
     */
    public static void mapping(Object source, Object target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) BeanUtils.copyProperties(source, target);
    }

    /**
     * 多个对象进行合并和聚合操作
     *
     * @param source 要进行 合并和聚合的对象
     * @param clazz  聚合结果类(对象)
     * @return 返回 聚合对象
     */
    public static <T> T assembler(Object[] source, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T t = clazz.newInstance();
        for (Object s : source) {
            DataConvert.mergeNotNull(s, t);
        }
        return t;
    }


    /**
     * 设置对象私有属性值
     *
     * @param source    实例对象
     * @param fieldName 成员变量名
     * @param target    目标值
     */
    public static void setFieldValue(Object source, String fieldName, Object target) {
        Field field = ReflectionUtils.findField(source.getClass(), fieldName);
        if (Objects.nonNull(field)) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, source, target);
        }
    }

    /**
     * 调用无参的私有方法
     *
     * @param source     目标类
     * @param methodName 方法名字
     */
    public static Object invokeNoArgsMethod(Object source, String methodName) throws NoSuchMethodException {
        Method method = ReflectionUtils.findMethod(source.getClass(), methodName);
        if (Objects.isNull(method)) throw new NoSuchMethodException();
        ReflectionUtils.makeAccessible(method);
        return ReflectionUtils.invokeMethod(method, source);
    }

    /**
     * 调用有参的私有方法(注意，参数列表和参数类型必须对应)
     *
     * @param instance   目标类
     * @param methodName 方法名字
     * @param args       参数列表
     * @param argsTypes  参数类型
     */
    public static Object invokeArgsMethod(Object instance, String methodName, Object[] args, Class<?>... argsTypes) throws NoSuchMethodException {
        Method method = ReflectionUtils.findMethod(instance.getClass(), methodName, argsTypes);
        if (Objects.isNull(method)) throw new NoSuchMethodException();
        ReflectionUtils.makeAccessible(method);
        return ReflectionUtils.invokeMethod(method, instance, args);
    }

    /**
     * 合并 将给定源bean的属性值(不为null)覆盖到给定目标bean中,只要属性匹配
     *
     * @param sourceBean 源bean
     * @param targetBean 目标bean
     */
    public static void mergeNotNull(Object sourceBean, Object targetBean) {
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
        }
    }

    /**
     * 合并(反射) 将给定源bean的属性值(不为null)覆盖到给定目标bean中,只要属性匹配
     *
     * @param source 源bean
     * @param target 目标bean
     */
    public static void mergeNotNullReflect(Object source, Object target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            Class newClass = target.getClass();
            Field[] oldFields = source.getClass().getDeclaredFields();
            Arrays.stream(oldFields).filter(field -> {
                        field.setAccessible(true);
                        try {
                            return field.get(source) != null;
                        } catch (IllegalAccessException e) {
                            return Boolean.FALSE;
                        }
                    }
            ).forEach(field -> {
                try {
                    Field newField = newClass.getDeclaredField(field.getName());
                    newField.setAccessible(true);
                    newField.set(target, field.get(source));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    log.error("printStackTrace", e);
                }
            });
        }
    }

    /**
     * 检查一个JavaBean的所有属性是否为null
     */
    public static boolean beanAllNull(Object javaBean) {
        if (Objects.isNull(javaBean)) return true;
        BeanWrapper wrapper = new BeanWrapperImpl(javaBean);
        PropertyDescriptor[] pds = getPropertyDescByObject(wrapper, javaBean);
        for (PropertyDescriptor prop : pds) {
            String propName = prop.getName();
            if (StringUtils.isEmpty(propName) && !propName.equals("class")) {
                Object propertyValue = wrapper.getPropertyValue(propName);
                if (Objects.nonNull(propertyValue)) return false;
            }
        }
        return true;
    }

    private static PropertyDescriptor[] getPropertyDescByObject(BeanWrapper wrapper, Object bean) {
        if (Objects.isNull(wrapper)) return new BeanWrapperImpl(bean).getPropertyDescriptors();
        else return wrapper.getPropertyDescriptors();
    }
}
