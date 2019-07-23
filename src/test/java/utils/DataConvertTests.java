package utils;

import org.junit.Test;
import utils.model.AssemblerModel;
import utils.model.SourceModel;
import utils.model.TargetExtends;
import utils.model.TargetModel;

/**
 * Created by EalenXie on 2019/6/6 10:01.
 */
public class DataConvertTests {

    private SourceModel source = new SourceModel("张三", 21, "男");

    @Test
    public void mapping() throws InstantiationException, IllegalAccessException {

        TargetModel target = DataConvert.mapping(source, TargetModel.class); //映射

        System.out.println("target : " + target);
        target.setAge(22);
        target.setName("李四");
        target.setEmail("lisi@qq.com");
        target.setPassword("123444");
        target.setTelephone("1234456678");

        DataConvert.mapping(target, source);                        //映射

        System.out.println("source : " + source);
        System.out.println("target : " + target);

        Object[] objects = {source, target};

        AssemblerModel assembler = DataConvert.assembler(objects, AssemblerModel.class);    //聚合

        System.out.println("assembler : " + assembler);

    }

    @Test
    public void reflect() throws InstantiationException, IllegalAccessException, NoSuchMethodException {

        TargetModel target = DataConvert.mapping(source, TargetModel.class);         //DTO
        System.out.println("target : " + target);
        target.setAge(null);
        target.setName(null);
        target.setEmail("lisi@qq.com");
        target.setPassword("123444");
        target.setTelephone("1234456678");
        AssemblerModel assembler = DataConvert.mapping(target, AssemblerModel.class); //DTO
        System.out.println("source : " + source);
        System.out.println("assembler : " + assembler);
        DataConvert.mergeNotNullReflect(source, assembler);
        System.out.println("assembler : " + DataConvert.invokeNoArgsMethod(assembler, "toString")); //调用无参的toString方法
        Object[] args = {null};             //参数值
        Class[] argsType = {Integer.class}; //参数类型
        DataConvert.invokeArgsMethod(assembler, "setAge", args, argsType);     //调用assembler 的setAge方法 传参为null
        System.out.println("assembler : " + assembler);
    }


    @Test
    public void compareMergeNotNull() throws InstantiationException, IllegalAccessException {
        TargetModel target = DataConvert.mapping(source, TargetModel.class); //映射
        DataConvert.mergeNotNull(source, target);
        DataConvert.mergeNotNullReflect(source, target);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            DataConvert.mergeNotNull(source, target);

        }
        long endTime = System.currentTimeMillis();
        System.out.println("mergeNotNull costs time : " + (endTime - startTime) + " ms");


        long startTimeReflect = System.currentTimeMillis();
        for (int j = 0; j < 1000; j++) {
            DataConvert.mergeNotNullReflect(source, target);
        }
        long endTimeReflect = System.currentTimeMillis();
        System.out.println("mergeNotNullReflect costs time : " + (endTimeReflect - startTimeReflect) + " ms");

    }


    @Test
    public void beanAllNull() {
        System.out.println(DataConvert.beanAllNull(new TargetExtends()));
    }

}
