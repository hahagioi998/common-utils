package utils;

import org.junit.Test;
import utils.model.AssemblerModel;
import utils.model.SourceModel;
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

        TargetModel target = DataConvert.dto(source, TargetModel.class);         //DTO

        System.out.println("target : " + target);
        target.setAge(null);
        target.setName(null);
        target.setEmail("lisi@qq.com");
        target.setPassword("123444");
        target.setTelephone("1234456678");

        AssemblerModel assembler = DataConvert.dto(target, AssemblerModel.class); //DTO

        System.out.println("source : " + source);
        System.out.println("assembler : " + assembler);

        DataConvert.mergeNotNull(source, assembler);                            //合并不为空

        System.out.println("assembler : " + DataConvert.invokeNoArgsMethod(assembler, "toString")); //调用无参的toString方法


        Object[] args = {null};             //参数值
        Class[] argsType = {Integer.class}; //参数类型

        DataConvert.invokeArgsMethod(assembler, "setAge", args, argsType);     //调用assembler 的setAge方法 传参为null

        System.out.println("assembler : " + assembler);


    }
}
