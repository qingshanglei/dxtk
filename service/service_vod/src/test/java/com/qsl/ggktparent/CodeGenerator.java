package com.qsl.ggktparent;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {
    public static void main(String[] args) {
        //1.获取代码生成器的对象
        AutoGenerator autoGenerator = new AutoGenerator();

        //设置数据库相关配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/glkt_live?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        autoGenerator.setDataSource(dataSource);

        //设置全局配置
        GlobalConfig globalConfig = new GlobalConfig();

        // 这个好像才行 ，下面的路径配置不行globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java");
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/service/service_live/src/main/java");    //设置代码生成位置
        globalConfig.setOpen(false);    //设置生成完毕后是否打开生成代码所在的目录
        globalConfig.setAuthor("青衫泪");    //设置作者
        globalConfig.setFileOverride(true);     //设置是否覆盖原始生成的文件
        globalConfig.setMapperName("%sMapper");    // 设置数据层接口名，%s为占位符，指代模块名称
        globalConfig.setServiceName("%sService"); //  去掉Service接口的首字母I, 例:IUserService
        globalConfig.setIdType(IdType.ASSIGN_ID);   //设置Id生成策略
        autoGenerator.setGlobalConfig(globalConfig);

        // 4、包配置     com.qsl.system...
        PackageConfig packageInfo = new PackageConfig();
        packageInfo.setParent("com.qsl.ggktparent");   //设置生成的包名，与代码所在位置不冲突，二者叠加组成完整路径
        packageInfo.setModuleName("live"); // 模块名     最终生成：com.qsl.ggktparent.user
        packageInfo.setEntity("pojo");//设置实体类包名
        packageInfo.setMapper("mapper");//mapper层
        packageInfo.setService("service");//service层
        packageInfo.setController("controller");// controller层
        autoGenerator.setPackageInfo(packageInfo);

        // 5、策略设置（根据数据库生成表）
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("live_course","live_course_account","live_course_config","live_course_description","live_course_goods","live_visitor"); // 指定要参与生成的表名(可多个)
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略  注意：使用这个可能不需要setTablePrefix。
//        strategyConfig.setTablePrefix("tb_","springboot_"); // 生成的类，去除数据库表的前缀（可多个）
        // 名称，模块名 = 数据库表名 - 前缀名 例如：User = tb_user - tbl
        strategyConfig.setRestControllerStyle(true); //是否启用Rest风格
        strategyConfig.setVersionFieldName("version"); //根据乐观锁名-开启乐观锁
        strategyConfig.setLogicDeleteFieldName("del"); //根据逻辑删除名-开启逻辑删除(值要自己设置)
        strategyConfig.setEntityLombokModel(true); //是否要使用lombok
        autoGenerator.setStrategy(strategyConfig);
        //2.执行生成操作
        autoGenerator.execute();
    }
}
