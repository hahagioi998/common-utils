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

}
