package com.example.visit_record.generate;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
//mysql\建表之后开始写这个generator函数,用来建立dao层.




public class MyBatisPlusGenerator {






@Test
    public  void codeGenerator() throws IOException {
//    生成之后得到文件夹: controller, entity ,mapper, serveic ,并且service用接口写的.






        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();




    Properties p = new Properties();
    p.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));








    // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
    System.out.println(projectPath);//就是项目所在的目录
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zhagnbo284");//Mapper,Service类注解中显示创建人信息
        //gc.setBaseColumnList(true); //在Mapper.xml文件中是否生成公用SQL代码段
        //gc.setBaseResultMap(true);  //在Mapper.xml文件中是否生成公用返回集合ResultMap
        gc.setOpen(false);  //文件生成完毕后，是否需要打开所在路径
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();









//用配置文件写进去
dsc.setUrl(p.getProperty("spring.datasource.url"));



        dsc.setDriverName(p.getProperty("spring.datasource.driver-class-name"));
        dsc.setUsername(p.getProperty("spring.datasource.username"));
        dsc.setPassword(p.getProperty("spring.datasource.password"));
        mpg.setDataSource(dsc);
    System.out.println(dsc);
//最后都写到mpg里面.




        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.example.visit_record");  //父级公用包名，就是自动生成的文件放在项目路径下的那个包中
        mpg.setPackageInfo(pc);









        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };



        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义Mapper.xml文件存放的路径
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);  //Entity文件名称命名规范
        strategy.setColumnNaming(NamingStrategy.underline_to_camel); //Entity字段名称
        strategy.setEntityLombokModel(true); //是否使用lombok完成Entity实体标注Getting Setting ToString 方法//因为mysql里面不区分大小写,所以需要用下划线.
        //strategy.setRestControllerStyle(true); //Controller注解使用是否RestController标注,否则是否开启使用Controller标注
        strategy.entityTableFieldAnnotationEnable(true); //是否在Entity属性上通过注解完成对数据库字段的映射
        strategy.setControllerMappingHyphenStyle(true);  //Controller注解名称，不使用驼峰，使用连字符
        strategy.setTablePrefix("test");  //表前缀，添加该表示，则生成的实体，不会有表前缀，比如sys_dept 生成就是Dept
        //strategy.setFieldPrefix("sys_");  //字段前缀
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}


